package com.tuzi80.holahola.task;

import com.tuzi80.holahola.model.HolaProjectDescription;
import com.tuzi80.holahola.model.Preference;
import com.tuzi80.holahola.utils.CommonUtil;
import com.tuzi80.holahola.utils.ExecScriptUtils;
import com.tuzi80.holahola.utils.ScriptResult;

import java.util.HashMap;

/**
 * install apk.
 * Created by betsy on 2/27/17.
 */
public class InstallApkTask extends Task {
    private String mAdb;
    private boolean mWaitForDebug;

    private HolaProjectDescription mDescription;

    private String mApkPath;
    private String mLauncher;
    private String mCacheDir;
    private String mPakage;


    public InstallApkTask(String adb, boolean waitForDebug) {
        super();
        super.init("install_apk_task");
        mAdb = adb;
        mWaitForDebug = waitForDebug;
    }

    private void initAttributes() {
        mDescription = Preference.getmInstance().getmProjectDescription();
        mApkPath = mDescription.getApk_path();
        mLauncher = mDescription.getLauncher();
        mCacheDir = mDescription.getBuild_cache_dir();
        mPakage = mDescription.getPackageName();
        if (!CommonUtil.isEmpty(mDescription.getDebug_package())) {
            mPakage = mDescription.getDebug_package();
        }

    }


    public void execute() {
        initAttributes();
        checkConnection();
        installApk();
        debugApp();
        launchApplication();
    }

    private void checkConnection() {
        debug("check device\\' connection...");

        StringBuilder cmd = new StringBuilder();
        cmd.append(mAdb);
        cmd.append(" ");
        cmd.append("devices");
        ScriptResult res = ExecScriptUtils.exec(cmd.toString());
        if (res.getCode() == 1) {
            int len = res.getOutput().split("\n").length;
            if (len < 2) {

            } else {

            }
        }
    }

    private void installApk() {
        StringBuilder cmd = new StringBuilder();
        cmd.append(mAdb);
        cmd.append(" ");
        cmd.append("install");
        cmd.append(" ");
        cmd.append("-r");
        cmd.append(" ");
        cmd.append(mApkPath);

        ScriptResult res = ExecScriptUtils.exec(cmd.toString());
        if (res.getOutput().contains("Failure")) {
            debug("install apk failed, start to retry.");
            res = ExecScriptUtils.exec(cmd.toString());
            if (res.getOutput().contains("Failure")) {
                throw new ExceptionInInitializerError("install apk to device failed.");
            }
        }
    }

    private void debugApp(){
        if(mWaitForDebug){
            String[] cmds={mAdb,"shell","am","set-debug-app","-w",mPakage};
            debug("make application wait for debugger");
            ExecScriptUtils.exec(cmds);
        }
    }

    private  void launchApplication(){
        if(!CommonUtil.isEmpty(mPakage)&&!CommonUtil.isEmpty(mLauncher)){
            String[] cmds={mAdb,"shell","am","start","-n",mPakage+"/"+mLauncher};
            debug("start to launch application");
            ExecScriptUtils.exec(cmds);
        }
    }

}
