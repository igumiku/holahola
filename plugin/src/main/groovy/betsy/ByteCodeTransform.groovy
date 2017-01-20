package betsy

import com.android.annotations.NonNull
import com.android.build.api.transform.*
import com.android.build.gradle.AndroidConfig
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project



public class ByteCodeTransform extends Transform {
    private Project project;
    private ArrayList<String> jarList = new ArrayList<String>();

    ByteCodeTransform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "ByteCode"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(
            @NonNull TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        //super.transform(transformInvocation)
        Logger.d("Lede ByteCode Transform begin")
        jarList.clear()
        def rootDir = project.rootDir
        def androidJarPath = ""
        def apacheHttp = ""
        def localProperties = new File(rootDir, "local.properties")
        if (localProperties.exists()) {
            Properties properties = new Properties()
            localProperties.withInputStream { instr ->
                properties.load(instr)
            }
            def sdkDir = properties.getProperty('sdk.dir')
            androidJarPath = sdkDir + File.separator + "platforms" + File.separator + project.getExtensions().findByType(AndroidConfig).compileSdkVersion + File.separator + "android.jar"
            apacheHttp = sdkDir + File.separator + "platforms" + File.separator + project.getExtensions().findByType(AndroidConfig).compileSdkVersion + File.separator + "optional" + File.separator + "org.apache.http.legacy.jar"
        }
        String mappingLocation = project.getProjectDir().getAbsolutePath() + File.separator + "release" + File.separator + "mapping.txt"
        Logger.d("Lede ByteCode Transform Current Task" + project.gradle.startParameter.taskNames.toString())
        transformInvocation.inputs.each { TransformInput input ->
            //对类型为“文件夹”的input进行遍历
            input.directoryInputs.each { DirectoryInput directoryInput ->
                //文件夹里面包含的是我们手写的类以及R.class、BuildConfig.class以及R$XXX.class等
                String patchPackage = project.extensions.patch.patchPackage
                JavassistInject.injectDir(directoryInput.file.absolutePath, project.getExtensions().findByType(AndroidConfig).defaultConfig.applicationId, androidJarPath, apacheHttp, jarList, patchPackage, mappingLocation)
                // 获取output目录
                def dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                // 将input的目录复制到output指定目录
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
            //对类型为jar文件的input进行遍历
            input.jarInputs.each { JarInput jarInput ->
                //jar文件一般是第三方依赖库jar文件
                // 重命名输出文件（同目录copyFile会冲突）
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }
                //生成输出路径
                def dest = transformInvocation.outputProvider.getContentLocation(jarName + md5Name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                //将输入内容复制到输出
                jarList.add(dest.getAbsolutePath())
                FileUtils.copyFile(jarInput.file, dest)
            }
        }
    }
}