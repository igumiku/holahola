package com.tuzi80.holahola.builder;

import com.tuzi80.holahola.model.HolaProjectDescription;
import com.tuzi80.holahola.task.TaskEngine;
import com.tuzi80.holahola.utils.AndroidTools;
import com.tuzi80.holahola.utils.BuildDirUtils;
import com.tuzi80.holahola.utils.FileuUtils;

import java.io.IOException;

/**
 * Clean builder.
 * Created by betsy on 2/20/17.
 */
public abstract class CleanBuilder extends Builder {
    private String mAdb = "";
    private boolean mIsArt = false;
    protected HolaProjectDescription mDescription;

    @Override
    public void init(String builderName, HolaProjectDescription description, TaskEngine engine) {
        super.init(builderName, description, engine);
        mDescription = description;
    }

    public void checkBuildEnvironment() {
        mAdb = this.getAdb(mProjectDescription);
        mIsArt = AndroidTools.getDeviceSdkVersionByAdb(mAdb) > 20;
    }

    public void updateApkCreatedTime() {
        String apktime = BuildDirUtils.getApktimePath(mDescription);
        try {
            FileuUtils.writeFileContent(apktime, System.currentTimeMillis() + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void findDependencies();


    public abstract void generateSortedBuildTasks();


    public abstract void cleanBuild();


}
