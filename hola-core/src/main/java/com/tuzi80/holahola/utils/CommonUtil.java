package com.tuzi80.holahola.utils;

import java.io.File;

/**
 * Common util.
 * Created by betsy on 2/20/17.
 */
public class CommonUtil {
    public static boolean isContain(String[] list, String find) {
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(find)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(String s) {
        if (s == null || "".equals(s)) {
            return true;
        }
        return false;
    }

    public static boolean isWindows() {
        return System.getProperties().getProperty("os.name").toUpperCase()
                .contains("WINDOWS");
    }

    public static boolean isFileExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }

        return false;
    }

    public static boolean isFile(String path) {
        File file = new File(path);
        return file.isFile();
    }

    public static void mkdir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    @SuppressWarnings("Since15")
    public static boolean isExcutable(String path) {
        File file = new File(path);
        if (file.canExecute()) {
            return true;
        }

        return false;
    }

}
