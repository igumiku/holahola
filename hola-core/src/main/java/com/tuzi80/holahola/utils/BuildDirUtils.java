package com.tuzi80.holahola.utils;

import com.tuzi80.holahola.model.HolaProjectDescription;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Get types of build files or dirs paths.
 * Created by betsy on 2/21/17.
 */
public class BuildDirUtils {
    public static String getApktimePath(HolaProjectDescription description) {
        String apkTimeDir = "";
        apkTimeDir = Paths.get(description.getBuild_cache_dir(), "freeline-assets").toString();
        if (!CommonUtil.isFileExists(apkTimeDir)) {
            CommonUtil.mkdir(apkTimeDir);
        }
        String apktime = Paths.get(apkTimeDir, "apktime").toString();
        if (!CommonUtil.isFileExists(apktime)) {
            try {
                FileuUtils.writeFileContent(apktime, "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return apktime;
    }


}
