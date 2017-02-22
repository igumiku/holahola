package com.tuzi80.holahola.utils;

import com.tuzi80.holahola.model.Module;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tools to get android info.
 * Created by betsy on 2/21/17.
 */
public class AndroidTools {
    /**
     * Get sdk version.
     *
     * @param adbPath adb path.
     * @return
     */
    public static int getDeviceSdkVersionByAdb(String adbPath) {
        int sdkVersion = 0;
        StringBuilder cmd = new StringBuilder();
        cmd.append(adbPath);
        cmd.append(" shell");
        cmd.append("getprop ro.build.version.sdk");
        String output = ExecScriptUtils.exec(cmd.toString());
        if (!CommonUtil.isEmpty(output)) {
            return Integer.parseInt(output);
        }
        return sdkVersion;
    }

    public static ArrayList<Module> getAllModule(String projectDir) {
        ArrayList<Module> result = new ArrayList<Module>();
        String settingsPath = projectDir + "settings.gradle";
        File file = new File(settingsPath);

        if (file.exists() && file.isFile()) {
            String filetext = FileuUtils.getFilecontent(settingsPath);
            Pattern p = Pattern.compile("\\:(.*?)\\|'");//正则表达式，取=和|之间的字符串，不包括=和|
            Matcher m = p.matcher(filetext);
            while (m.find()) {
                Module module = new Module();
                module.setName(m.group(1));
                module.setPath(Paths.get(projectDir, m.group(1)).toString());
                result.add(module);
            }
        }
        return result;
    }

    public static String getPackageName(String manifest) {
        if (!CommonUtil.isEmpty(manifest) && CommonUtil.isFile(manifest)) {
            String filetext = FileuUtils.getFilecontent(manifest);
            Pattern p = Pattern.compile("package=\"(.*)\"");//正则表达式，取=和|之间的字符串，不包括=和|
            Matcher m = p.matcher(filetext);
            while (m.find()) {
                return m.group(1);
            }

        }
        return "";
    }

}
