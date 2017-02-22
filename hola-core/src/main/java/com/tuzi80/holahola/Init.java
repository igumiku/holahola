package com.tuzi80.holahola;

import com.tuzi80.holahola.model.Module;
import com.tuzi80.holahola.utils.AndroidTools;
import com.tuzi80.holahola.utils.CommonUtil;
import com.tuzi80.holahola.utils.ExecScriptUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * com.tuzi80.holahola.Init.
 * Created by betsy on 2/17/17.
 */
public class Init {
    public static void init() {
        String projectDir = System.getProperty("user.dir");

        ArrayList<Module> modules = AndroidTools.getAllModule(projectDir);
        boolean mainModule = false;
        for (int i = 0; i < modules.size(); i++) {
//            if (isMainProject(modules.get(i))) {
//                break;
//            }
        }

        if (mainModule) {
            throw new ExceptionInInitializerError("com.tuzi80.holahola.main module not found', 'set com.tuzi80.holahola.main module first");
        }

        StringBuilder cmd = new StringBuilder();

        if (CommonUtil.isWindows()) {
            cmd.append("gradlew.bat");
        } else {
            cmd.append("./gradlew");
        }
        cmd.append(" checkBeforeCleanBuild");
        System.out.println("hola is reading project info, please wait a moment...");

        ExecScriptUtils.exec(cmd.toString());
    }

    private static boolean isMainProject(String moduleName) {
        return false;
    }
}
