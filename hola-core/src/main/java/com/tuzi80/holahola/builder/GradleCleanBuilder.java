package com.tuzi80.holahola.builder;

import com.tuzi80.holahola.model.HolaProjectDescription;
import com.tuzi80.holahola.model.ProjectInfo;
import com.tuzi80.holahola.task.*;
import com.tuzi80.holahola.task.clean.GradleCleanBuildTask;

import java.util.HashSet;

/**
 * Gradle clean builder.
 * Created by betsy on 2/20/17.
 */
public class GradleCleanBuilder extends CleanBuilder {
    private boolean mWaitForDebug = false;
    private ProjectInfo mProjectInfo;
    private TaskEngine mTaskEngine;
    private HashSet<Task> mRootTask=new HashSet<Task>();

    public void init(String builderName, HolaProjectDescription description, TaskEngine engine
            , ProjectInfo projectInfo, boolean waitForDebug) {
        super.init("gradle_clean_builder", description, engine);
        mWaitForDebug = waitForDebug;
        mProjectInfo = projectInfo;
        mTaskEngine = engine;
    }

    public void checkBuildEnvironment() {
        super.checkBuildEnvironment();
//        if (mProjectInfo == null) {
//            String projectInfoPath = Paths.get(mDescription.getBuild_cache_dir(), "project_info_cache.json").toString();
//            if (CommonUtil.isFileExists(projectInfoPath)) {
//                String jsonProInfo = JsonUtils.readJsonFromFile(projectInfoPath);
//                mProjectInfo = new Gson().fromJson(jsonProInfo, ProjectInfo.class);
//            }else{
//
//            }
//        }
    }

    public void findDependencies() {
    }

    public void generateSortedBuildTasks() {
//        tasks' order:
//        1. generate file stat / check before clean build
//        2. clean build
//        3. install / clean cache
//        4. build base res / generate project info cache
        Task buildTask = new GradleCleanBuildTask();
        InstallApkTask installTask = new InstallApkTask(Builder.getAdb(mDescription), mWaitForDebug);
        String[] ignores = {"stat_cache.json"
                , "apktime", "jar_dependencies.json", "resources_dependencies.json", "public_keeper.xml",
                "assets_dependencies.json", "freeline_annotation_info.json"};
        CleanAllCacheTask cleanAllCacheTask = new CleanAllCacheTask(mDescription.getBuild_cache_dir(), ignores);

        BuildBaseResourceTask buildBaseResourceTask = new BuildBaseResourceTask();
        GenerateFileStatTask generateStatTask = new GenerateFileStatTask(false);
//        append_stat_task = GenerateFileStatTask(self._config, is_append = True)
        GradleReadProjectInfoTask readProjectInfoTask = new GradleReadProjectInfoTask();
        GradleGenerateProjectInfoTask generateProjectnfoTask = new GradleGenerateProjectInfoTask();
        GenerateAptFilesStatTask generateAptFileStatTask = new GenerateAptFilesStatTask();

        //generate_stat_task.add_child_task(read_project_info_task)
        buildTask.addChildTask(cleanAllCacheTask);
        buildTask.addChildTask(installTask);
        cleanAllCacheTask.addChildTask(buildBaseResourceTask);//资源基线包，用于资源增量更新
        cleanAllCacheTask.addChildTask(generateProjectnfoTask);
//        clean_all_cache_task.add_child_task(append_stat_task)#更新
        cleanAllCacheTask.addChildTask(generateAptFileStatTask);
        readProjectInfoTask.addChildTask(buildTask);

        mRootTask.add(generateStatTask);
        mRootTask.add(readProjectInfoTask);
    }

    public void cleanBuild() {
        mTaskEngine.addRootTask(mRootTask);
        try {
            mTaskEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
