package betsy

import groovy.io.FileType
import groovy.json.JsonBuilder
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.apache.commons.io.FileUtils

public class PatchPlugin implements Plugin<Project> {

    void apply(Project project) {
        System.out.println("hello gradle plugin!");
        def forceLowerVersion = false
        List<String> excludeHackClasses = []
        project.afterEvaluate {
            project.android.applicationVariants.each { variant ->
                if (!"debug".equalsIgnoreCase(variant.buildType.name as String)) {
                    println "variant ${variant.name} is not debug, skip hack process."
                    return
                }

                println "find variant ${variant.name} start hack process..."

                if (isProguardEnable(project, variant)) {
                    throw new RuntimeException("Hola doesn't support proguard now, please disable proguard config then re-run.")
                }

                def addtionalJars = []
                def projectAptConfig = [:]
                def projectRetrolambdaConfig = [:]
                def aptLibraries = ['dagger': false, 'butterknife': false]

                project.rootProject.allprojects.each { pro ->
                    if (pro.plugins.hasPlugin("com.android.application") || pro.plugins.hasPlugin("com.android.library")) {
                        // find additional jars
                        if (pro.android.hasProperty("libraryRequests")) {
                            pro.android.libraryRequests.each { p ->
                                def jar_path = FreelineUtils.joinPath(
                                        pro.android.sdkDirectory.toString(),
                                        'platforms',
                                        pro.android.compileSdkVersion.toString(),
                                        'optional',
                                        p.name + ".jar")
                                def f = new File(jar_path)
                                if (f.exists() && f.isFile()) {
                                    addtionalJars.add(jar_path)
                                    println "find additional jar: ${jar_path}"
                                }
                            }
                        }

                        // find apt config
                        findAptConfig(pro, variant, projectAptConfig)
                    }
                }

                // modify .class file
                def isLowerVersion = false
                if (!forceLowerVersion) {
                    project.rootProject.buildscript.configurations.classpath.resolvedConfiguration.firstLevelModuleDependencies.each {
                        if (it.moduleGroup == "com.android.tools.build" && it.moduleName == "gradle") {
                            if (!it.moduleVersion.startsWith("1.5")
                                    && !it.moduleVersion.startsWith("2")) {
                                isLowerVersion = true
                                return false
                            }
                        }
                    }
                } else {
                    isLowerVersion = true
                }

                def classesProcessTask
                def preDexTask
                def multiDexListTask
                boolean multiDexEnabled = variant.apkVariantData.variantConfiguration.isMultiDexEnabled()
                if (isLowerVersion) {
                    if (multiDexEnabled) {
                        classesProcessTask = project.tasks.findByName("packageAll${variant.name.capitalize()}ClassesForMultiDex")
                        multiDexListTask = project.tasks.findByName("create${variant.name.capitalize()}MainDexClassList")
                    } else {
                        classesProcessTask = project.tasks.findByName("dex${variant.name.capitalize()}")
                        preDexTask = project.tasks.findByName("preDex${variant.name.capitalize()}")
                    }
                } else {
                    String manifest_path = project.android.sourceSets.main.manifest.srcFile.path
                    if (getMinSdkVersion(variant.mergedFlavor, manifest_path) < 21 && multiDexEnabled) {
                        classesProcessTask = project.tasks.findByName("transformClassesWithJarMergingFor${variant.name.capitalize()}")
                        multiDexListTask = project.tasks.findByName("transformClassesWithMultidexlistFor${variant.name.capitalize()}")
                    } else {
                        classesProcessTask = project.tasks.findByName("transformClassesWithDexFor${variant.name.capitalize()}")
                    }
                }

                if (classesProcessTask == null) {
                    println "Skip ${project.name}'s hack process"
                    return
                }

                classesProcessTask.outputs.upToDateWhen { false }
                String backUpDirPath = FreelineUtils.getBuildBackupDir(project.buildDir.absolutePath)
                def modules = [:]
                project.rootProject.allprojects.each { pro ->
                    modules[pro.name] = "exploded-aar" + File.separator + pro.group + File.separator + pro.name + File.separator
                }

                if (preDexTask) {
                    preDexTask.outputs.upToDateWhen { false }
                    def hackClassesBeforePreDex = "hackClassesBeforePreDex${variant.name.capitalize()}"

                    project.task(hackClassesBeforePreDex) << {
                        def jarDependencies = []
                        println "before inject"
                        preDexTask.inputs.files.files.each { f ->
                            if (f.path.endsWith(".jar")) {
                                FreelineInjector.inject(excludeHackClasses, f as File, modules.values())
                                jarDependencies.add(f.path)
                            }
                        }
                        println "after inject"
                        def json = new JsonBuilder(jarDependencies).toPrettyString()
                        project.logger.info(json)
                        FreelineUtils.saveJson(json, FreelineUtils.joinPath(FreelineUtils.getBuildCacheDir(project.buildDir.absolutePath), "jar_dependencies.json"), true);
                    }

                    def hackClassesBeforePreDexTask = project.tasks[hackClassesBeforePreDex]
                    hackClassesBeforePreDexTask.dependsOn preDexTask.taskDependencies.getDependencies(preDexTask)
                    preDexTask.dependsOn hackClassesBeforePreDexTask
                }

                def hackClassesBeforeDex = "hackClassesBeforeDex${variant.name.capitalize()}"
                def backupMap = [:]
                project.task(hackClassesBeforeDex) << {
                    def jarDependencies = []
                    classesProcessTask.inputs.files.files.each { f ->
                        if (f.isDirectory()) {
                            f.eachFileRecurse(FileType.FILES) { file ->
                                backUpClass(backupMap, file as File, backUpDirPath as String, modules.values())
                                FreelineInjector.inject(excludeHackClasses, file as File, modules.values())
                                if (file.path.endsWith(".jar")) {
                                    jarDependencies.add(file.path)
                                }
                            }
                        } else {
                            backUpClass(backupMap, f as File, backUpDirPath as String, modules.values())
                            FreelineInjector.inject(excludeHackClasses, f as File, modules.values())
                            if (f.path.endsWith(".jar")) {
                                jarDependencies.add(f.path)
                            }
                        }
                    }

                    if (preDexTask == null) {
                        jarDependencies.addAll(addtionalJars)
                        // add all additional jars to final jar dependencies
                        def json = new JsonBuilder(jarDependencies).toPrettyString()
                        project.logger.info(json)
                        FreelineUtils.saveJson(json, FreelineUtils.joinPath(FreelineUtils.getBuildCacheDir(project.buildDir.absolutePath), "jar_dependencies.json"), true);
                    }
                }

                if (classesProcessTask) {
                    def hackClassesBeforeDexTask = project.tasks[hackClassesBeforeDex]
                    hackClassesBeforeDexTask.dependsOn classesProcessTask.taskDependencies.getDependencies(classesProcessTask)
                    classesProcessTask.dependsOn hackClassesBeforeDexTask
                }

                if (multiDexEnabled && applicationProxy) {
                    def mainDexListFile = new File("${project.buildDir}/intermediates/multi-dex/${variant.dirName}/maindexlist.txt")
                    if (multiDexListTask) {
                        multiDexListTask.outputs.upToDateWhen { false }
                        multiDexListTask.doLast {
                            Constants.FREELINE_CLASSES.each { clazz ->
                                mainDexListFile << "\n${clazz}"
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isProguardEnable(Project project, def variant) {
        def proguardTask = project.tasks.findByName("transformClassesAndResourcesWithProguardFor${variant.name.capitalize()}")
        if (proguardTask == null) {
            proguardTask = project.tasks.findByName("proguard${variant.name.capitalize()}")
        }
        return proguardTask != null
    }

    private static int getMinSdkVersion(def mergedFlavor, String manifestPath) {
        if (mergedFlavor.minSdkVersion != null) {
            return mergedFlavor.minSdkVersion.apiLevel
        } else {
            return FreelineParser.getMinSdkVersion(manifestPath)
        }
    }

    private static void backUpClass(def backupMap, File file, String backUpDirPath, def modules) {
        String path = file.absolutePath
        if (!FreelineUtils.isEmpty(path)) {
            if (path.endsWith(".class")
                    || (path.endsWith(".jar") && FreelineInjector.checkInjection(file, modules as Collection))) {
                File target = new File(backUpDirPath, "${file.name}-${System.currentTimeMillis()}")
                FileUtils.copyFile(file, target)
                backupMap[file.absolutePath] = target.absolutePath
                println "back up ${file.absolutePath} to ${target.absolutePath}"
            }
        }
    }

    private static def findAptConfig(Project project, def variant, def projectAptConfig) {
        def javaCompile = getJavaCompileTask(variant, project)

        def aptConfiguration = project.configurations.findByName("apt")
        def isAptEnabled = project.plugins.hasPlugin("android-apt") && aptConfiguration != null && !aptConfiguration.empty

        def annotationProcessorConfig = project.configurations.findByName("annotationProcessor")
        def isAnnotationProcessor = annotationProcessorConfig != null && !annotationProcessorConfig.empty

        if ((isAptEnabled || isAnnotationProcessor) && javaCompile) {
            println "Freeline found ${project.name} apt plugin enabled."
            javaCompile.outputs.upToDateWhen { false }
            javaCompile.doFirst {
                def aptOutputDir
                if (project.plugins.hasPlugin("com.android.application")) {
                    aptOutputDir = new File(project.buildDir, "generated/source/apt/${variant.dirName}").absolutePath
                } else {
                    aptOutputDir = new File(project.buildDir, "generated/source/apt/release").absolutePath
                }

                def configurations = javaCompile.classpath
                if (isAptEnabled) {
                    configurations += aptConfiguration
                }
                if (isAnnotationProcessor) {
                    configurations += annotationProcessorConfig
                }

                def processorPath = configurations.asPath

                boolean disableDiscovery = javaCompile.options.compilerArgs.indexOf('-processorpath') == -1

                int processorIndex = javaCompile.options.compilerArgs.indexOf('-processor')
                def processor = null
                if (processorIndex != -1) {
                    processor = javaCompile.options.compilerArgs.get(processorIndex + 1)
                }

                def aptArgs = []
                javaCompile.options.compilerArgs.each { arg ->
                    if (arg.toString().startsWith('-A')) {
                        aptArgs.add(arg)
                    }
                }

                def aptConfig = ['enabled': true,
                                 'disableDiscovery': disableDiscovery,
                                 'aptOutput': aptOutputDir,
                                 'processorPath': processorPath,
                                 'processor': processor,
                                 'aptArgs': aptArgs]
                projectAptConfig[project.name] = aptConfig
            }
        } else {
            println "Freeline doesn't found apt plugin for $project.name"
        }
    }

    private static def getJavaCompileTask(def variant, Project pro) {
        def javaCompile
        if (pro.plugins.hasPlugin("com.android.application")) {
            javaCompile = variant.hasProperty('javaCompiler') ? variant.javaCompiler : variant.javaCompile
        } else {
            pro.android.libraryVariants.each { libraryVariant ->
                if ("release".equalsIgnoreCase(libraryVariant.buildType.name as String)) {
                    javaCompile = libraryVariant.hasProperty('javaCompiler') ? libraryVariant.javaCompiler : libraryVariant.javaCompile
                    return false
                }
            }
        }
        return javaCompile
    }
}