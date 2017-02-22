package com.tuzi80.holahola.builder;

import com.tuzi80.holahola.model.HolaProjectDescription;
import com.tuzi80.holahola.task.TaskEngine;
import com.tuzi80.holahola.utils.CommonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Abstract builder.
 * Created by betsy on 2/20/17.
 */
public abstract class Builder {
    private String mBuilderName = "";
    protected HolaProjectDescription mProjectDescription;
    private TaskEngine mTaskEngine;

    private static Logger mLogger = LogManager.getLogger(Builder.class);

    public void debug() {

    }

    public void init(String builderName, HolaProjectDescription description, TaskEngine engine) {
        mBuilderName = builderName;
        mProjectDescription = description;
        mTaskEngine = engine;
    }

    public String getAndroidSdkDir(HolaProjectDescription description) {
        String sdkDir = description.getSdk_directory();
        if (!CommonUtil.isEmpty(sdkDir)) {
            return sdkDir;
        }


        sdkDir = System.getenv("ANDROID_HOME");
        File file = new File(sdkDir);
        if (!CommonUtil.isEmpty(sdkDir) && file.isDirectory()) {
            return sdkDir;
        }

        sdkDir = System.getenv("'ANDROID_SDK'");
        file = new File(sdkDir);
        if (!CommonUtil.isEmpty(sdkDir) && file.isDirectory()) {
            return sdkDir;
        }

        mLogger.debug("[ERROR] config[sdk_directory]、ANDROID_HOME、ANDROID_SDK not found, " +
                "Build.get_android_sdk_dir() return None.");
        return null;
    }

    public String getAdb(HolaProjectDescription description) {
        String sdkDir = getAndroidSdkDir(description);
        String adbExeName = CommonUtil.isWindows() ? "adb.exe" : "adb";
        Path path = Paths.get(sdkDir, "platform-tools", adbExeName);
        File file = new File(path.toString());
        if (file.canExecute()) {
            return path.toString();
        }
        mLogger.debug("[ERROR] Builder.get_adb() return None.");
        return null;
    }


    public String getMavenHomeDir() {
        String mavenDir = System.getenv("M2_HOME");
        if (CommonUtil.isEmpty(mavenDir)) {
            mavenDir = System.getenv("MAVEN_HOME");
        }
        return mavenDir;
    }

    public String getAapt() {
        String aapt = Paths.get("freeline", "release-tools", "FreelineAapt").toString();
        if (CommonUtil.isWindows()) {
            aapt = Paths.get("freeline", "release-tools", "FreelineAapt.exe").toString();
        } else {
            Paths.get("freeline", "release-tools", "FreelineAapt_").toString();
        }
        if (CommonUtil.isFileExists(aapt)) {
            return aapt;
        }
        return null;
    }

    @SuppressWarnings("Since15")
    public String getJavaC(HolaProjectDescription description) {
        String javaHome = System.getenv("JAVA_HOME");
        String home = description.getJava_home();
        if (!CommonUtil.isEmpty(home)) {
            javaHome = home;
        }
        String exeNAME = "";
        if (CommonUtil.isWindows()) {
            exeNAME = "javac.exe";
        } else {
            exeNAME = "javac";
        }

        if (CommonUtil.isFileExists(javaHome) && CommonUtil.isExcutable(javaHome)) {
            return Paths.get(javaHome, "bin", exeNAME).toString();
        }

        mLogger.debug("[ERROR] Builder.get_javac() return None.");
        return null;
    }

    public String getDx(HolaProjectDescription description) {
        String buildToolDir = description.getBuild_tools_directory();
        String exeName = "";
        if (CommonUtil.isWindows()) {
            exeName = "dx.bat";
        } else {
            exeName = "dx";
        }

        return Paths.get(buildToolDir, exeName).toString();
    }

    public String getDataBindingCli(HolaProjectDescription description) {
        String dbcli = Paths.get("freeline", "release-tools", "databinding-cli.jar").toString();
        if (description.isUse_jdk8()) {
            dbcli = Paths.get("freeline", "release-tools", "databinding-cli8.jar").toString();
        }

        return dbcli;
    }
}
