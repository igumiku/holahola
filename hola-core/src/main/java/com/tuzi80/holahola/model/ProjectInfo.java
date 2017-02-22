package com.tuzi80.holahola.model;

import java.util.HashMap;

/**
 * Project info.
 * Created by betsy on 2/21/17.
 */
public class ProjectInfo {
    private HashMap<String,ModuleInfo> mModuleInfos=new HashMap<String, ModuleInfo>();

    public HashMap<String, ModuleInfo> getmModuleInfos() {
        return mModuleInfos;
    }

    public void setmModuleInfos(HashMap<String, ModuleInfo> mModuleInfos) {
        this.mModuleInfos = mModuleInfos;
    }
}
