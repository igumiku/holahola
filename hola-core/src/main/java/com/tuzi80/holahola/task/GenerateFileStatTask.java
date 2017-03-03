package com.tuzi80.holahola.task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuzi80.holahola.builder.Builder;
import com.tuzi80.holahola.json.JsonUtils;
import com.tuzi80.holahola.model.HolaProjectDescription;
import com.tuzi80.holahola.model.Module;
import com.tuzi80.holahola.model.Preference;
import com.tuzi80.holahola.utils.AndroidTools;
import com.tuzi80.holahola.utils.CommonUtil;
import com.tuzi80.holahola.utils.FileuUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Generate file stat task.
 * Created by betsy on 2/27/17.
 */
public class GenerateFileStatTask extends Task {
    private boolean mAppend = false;
    private HolaProjectDescription mDescription = Preference.getmInstance().getmProjectDescription();
    private String mCachePath = Paths.get(mDescription.getBuild_cache_dir(), "stat_cache.json").toString();

    private HashMap<String, HashMap<String, FileInfo>> mCacheStat = new HashMap<String, HashMap<String, FileInfo>>();

    public GenerateFileStatTask(boolean isAppend) {
        super();
        String name = isAppend ? "append_file_stat_task" : "generate_file_stat_task";
        super.init(name);
    }

    public void execute() {
        if (mAppend) {
            debug("generate_file_stat_task in append mode");
            String json = JsonUtils.readJsonFromFile(mCachePath);

        }

        ArrayList<Module> modules = mDescription.getModules();
        if (modules == null) {
            String projectDir = System.getProperty("user.dir");
            modules = AndroidTools.getAllModule(projectDir);
        }

        if (mAppend && CommonUtil.isFileExists(mCachePath)) {

        } else {
            fillCacheMap(modules);
            saveCache();
        }
    }

    /**
     * init stat_cache.json.
     *
     * @param modules all modules info.
     */
    private void fillCacheMap(ArrayList<Module> modules) {
        for (Module module : modules) {
            debug(String.format("save %s module file stat", module.getName()));
            saveModuleStat(module.getName(), module.getPath());
        }
    }

    private void saveModuleStat(String moduleName, String modulePath) {
        if (mCacheStat.get(moduleName) == null) {
            HashMap<String, FileInfo> info = new HashMap<String, FileInfo>();
            mCacheStat.put(moduleName, info);
        }
        //scan build.gradle
        saveStat(moduleName, Paths.get(modulePath, "build.gradle").toString());

        //scan libs dir
        String[] libDirs = {"libs", "lib"};
        scanDir(moduleName, libDirs, ".jar");


        if (mDescription.getProject_source_sets().containsKey(moduleName)) {
            //scan manifest
            saveStat(moduleName, mDescription.getProject_source_sets().get(moduleName).getMain_manifest_path());

            //scan native so
            ArrayList<String> nativeSoDirs = mDescription.getProject_source_sets().get(moduleName).getMain_jniLibs_directory();
            scanDir(moduleName, libDirs, ".so");

            //scan assets
            ArrayList<String> assetsDirs = mDescription.getProject_source_sets().get(moduleName).getMain_assets_directory();
            scanDir(moduleName, assetsDirs, "");

            //scan res
            ArrayList<String> resDirs = mDescription.getProject_source_sets().get(moduleName).getMain_res_directory();
            scanDir(moduleName, assetsDirs, "");

            //scan source
            ArrayList<String> srcDirs = mDescription.getProject_source_sets().get(moduleName).getMain_src_directory();
            for (String dir : srcDirs) {
                File d = new File(dir);
                if (d.exists() && d.isDirectory()) {
                    ArrayList<String> allfiles = CommonUtil.getallFile(d);
                    for (String filePath : allfiles) {
                        saveStat(moduleName, Paths.get(dir, filePath).toString());
                    }
                }
            }


        }

    }

    private void saveCache(){
        if(CommonUtil.isFileExists(mCachePath)){
            CommonUtil.deleteFile(mCachePath);
        }
        Type collectionType = new TypeToken<HashMap<String, HashMap<String, FileInfo>>>(){}.getType();
        try {
            FileuUtils.writeFileContent(mCachePath,new Gson().toJson(mCacheStat,collectionType));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scanDir(String moduleName, ArrayList<String> dirs, String type) {
        if (dirs != null) {
            for (String dirPath : dirs) {
                scanFile(moduleName, dirPath, type);
            }
        }
    }

    private void scanDir(String moduleName, String[] dirs, String type) {
        if (dirs != null) {
            for (String dirPath : dirs) {
                scanFile(moduleName, dirPath, type);
            }
        }
    }

    private void scanFile(String moduleName, String dirPath, String type) {
        if (CommonUtil.isDir(dirPath)) {
            File dir = new File(dirPath);
            for (File subdir : dir.listFiles()) {
                if (subdir.isDirectory()) {
                    for (File file : subdir.listFiles())
                        if (!CommonUtil.isEmpty(type) && file.getName().endsWith(type)) {
                            saveStat(moduleName, Paths.get(dirPath, file.getName()).toString());
                        } else {
                            saveStat(moduleName, Paths.get(dirPath, file.getName()).toString());
                        }
                }
            }
        }
    }

    /**
     * save file stat.
     *
     * @param moduleName module name of this file.
     * @param filePath   file path.
     */
    private void saveStat(String moduleName, String filePath) {
        if (CommonUtil.isFileExists(filePath)) {
            FileInfo fileInfo = new FileInfo();
            File file = new File(filePath);
            fileInfo.setmTime(file.lastModified());
            fileInfo.setmSize(file.length());
            if (mCacheStat.get(moduleName) == null) {
                mCacheStat.put(moduleName, new HashMap<String, FileInfo>());
            }
            mCacheStat.get(moduleName).put(filePath, fileInfo);
        }
    }


    private class FileInfo {
        private long mTime;
        private long mSize;

        public long getmTime() {
            return mTime;
        }

        public void setmTime(long mTime) {
            this.mTime = mTime;
        }

        public long getmSize() {
            return mSize;
        }

        public void setmSize(long mSize) {
            this.mSize = mSize;
        }
    }
}
