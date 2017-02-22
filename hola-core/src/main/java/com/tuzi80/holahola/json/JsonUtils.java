package com.tuzi80.holahola.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Read json or write json.
 * Created by betsy on 2/20/17.
 */
public class JsonUtils {

    public static String readJsonFromFile(String path) {
        return getFilecontent(path);
    }

    private static String getFilecontent(String settingsPath) {
        StringBuffer list = new StringBuffer();
        try {
            String encoding = "GBK";
            File file = new File(settingsPath);
            if (file.isFile() && file.exists()) { // 判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;

                while ((lineTxt = bufferedReader.readLine()) != null) {
                    list.append(lineTxt);
                }
                bufferedReader.close();
                read.close();
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

        return list.toString();
    }
}
