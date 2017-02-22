package com.tuzi80.holahola.builder;

import com.tuzi80.holahola.model.HolaProjectDescription;
import com.tuzi80.holahola.model.ProjectInfo;
import com.tuzi80.holahola.task.clean.GradleCleanBuildTask;
import com.tuzi80.holahola.task.Task;
import com.tuzi80.holahola.task.TaskEngine;

/**
 * Gradle clean builder.
 * Created by betsy on 2/20/17.
 */
public class GradleCleanBuilder extends CleanBuilder {
    private boolean mWaitForDebug = false;
    private ProjectInfo mProjectInfo;

    public void init(String builderName, HolaProjectDescription description, TaskEngine engine
            , ProjectInfo projectInfo, boolean waitForDebug) {
        super.init("gradle_clean_builder", description, engine);
        mWaitForDebug = waitForDebug;
        mProjectInfo = projectInfo;
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
        install_task = InstallApkTask(self._adb, self._config, wait_for_debugger=self._wait_for_debugger)
        clean_all_cache_task = CleanAllCacheTask(self._config['build_cache_dir'], ignore=[
                'stat_cache.json', 'apktime', 'jar_dependencies.json', 'resources_dependencies.json', 'public_keeper.xml',
                'assets_dependencies.json', 'freeline_annotation_info.json'])
        build_base_resource_task = BuildBaseResourceTask(self._config, self._project_info)
        generate_stat_task = GenerateFileStatTask(self._config)
        append_stat_task = GenerateFileStatTask(self._config, is_append=True)
        read_project_info_task = GradleReadProjectInfoTask()
        generate_project_info_task = GradleGenerateProjectInfoTask(self._config)
        generate_apt_file_stat_task = GenerateAptFilesStatTask()

        # generate_stat_task.add_child_task(read_project_info_task)
        build_task.add_child_task(clean_all_cache_task)
        build_task.add_child_task(install_task)
        clean_all_cache_task.add_child_task(build_base_resource_task)#资源基线包，用于资源增量更新
        clean_all_cache_task.add_child_task(generate_project_info_task)
        clean_all_cache_task.add_child_task(append_stat_task)#更新
        clean_all_cache_task.add_child_task(generate_apt_file_stat_task)
        read_project_info_task.add_child_task(build_task)
        self._root_task = [generate_stat_task, read_project_info_task]
    }

    public void cleanBuild() {

    }
}
