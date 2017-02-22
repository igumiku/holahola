package com.tuzi80.holahola.task.clean;

import com.tuzi80.holahola.model.HolaProjectDescription;
import com.tuzi80.holahola.model.Preference;
import com.tuzi80.holahola.task.Task;
import com.tuzi80.holahola.utils.CommonUtil;
import com.tuzi80.holahola.utils.ExecScriptUtils;

/**
 * Gradle clean build task.
 * Created by betsy on 2/22/17.
 */
public class GradleCleanBuildTask extends Task {
    public GradleCleanBuildTask(){
        init("gradle_clean_build_task");
    }
    public void execute() {
        HolaProjectDescription description= Preference.getmInstance().getmProjectDescription();

        String cmd=description.getBuild_script_work_directory();
        if(!CommonUtil.isFileExists(cmd)||CommonUtil.isFile(cmd)){
            cmd=null;
        }

        StringBuilder command=new StringBuilder(description.getBuild_script());
        command.append(" -P freelineBuild=true");

        if(!description.isAuto_dependency()){
            command.append(" -PdisableAutoDependency=true");
        }

        command.append(" --stacktrace");
        debug("Gradle build task is running, please wait a minute...");

        ExecScriptUtils.exec(command.toString());
    }
}
