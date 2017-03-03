package com.tuzi80.holahola.task;

import com.tuzi80.holahola.utils.CommonUtil;
import com.tuzi80.holahola.utils.ExecScriptUtils;
import com.tuzi80.holahola.utils.ScriptResult;

/**
 * checkBeforeCleanBuild.
 * Created by betsy on 2/28/17.
 */
public class GradleReadProjectInfoTask extends Task{
    public GradleReadProjectInfoTask(){
        super();
        super.init("read_project_info_task");
    }
    public void execute() {
        String cmd="./gradlew -q checkBeforeCleanBuild";
        if(CommonUtil.isWindows()){
            cmd="gradlew.bat -q checkBeforeCleanBuild";
        }

        ScriptResult result= ExecScriptUtils.exec(cmd);

        if(result.getCode()!=0){
            throw new ExceptionInInitializerError("freeline failed when read project info with script");
        }
    }
}
