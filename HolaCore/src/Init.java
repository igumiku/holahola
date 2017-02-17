import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jdk.nashorn.internal.objects.Global.print;

/**
 * Created by betsy on 2/17/17.
 */
public class Init {
    public static void init() {
        String projectDir = System.getProperty("user.dir");

        ArrayList<String> modules = getAllModule(projectDir);
        boolean mainModule = false;
        for (int i = 0; i < modules.size(); i++) {
            if (isMainProject(modules.get(i))) {
                break;
            }
        }

        if (mainModule) {
            throw new ExceptionInInitializerError("main module not found', 'set main module first");
        }

        StringBuilder cmd = new StringBuilder();
        boolean isWindows = System.getProperties().getProperty("os.name").toUpperCase()
                .contains("WINDOWS");
        if (isWindows) {
            cmd.append("gradlew.bat");
        } else {
            cmd.append("./gradlew");
        }
        cmd.append(" checkBeforeCleanBuild");
        System.out.println("hola is reading project info, please wait a moment...");

        ExecScriptUtils.exec(cmd.toString());
    }

    private static ArrayList<String> getAllModule(String projectDir) {
        ArrayList<String> result = new ArrayList<>();
        String settingsPath = projectDir + "settings.gradle";
        File file = new File(settingsPath);

        if (file.exists() && file.isFile()) {
            String filetext = getFilecontent(settingsPath);
            Pattern p = Pattern.compile("\\:(.*?)\\|'");//正则表达式，取=和|之间的字符串，不包括=和|
            Matcher m = p.matcher(filetext);
            while (m.find()) {
                result.add(m.group(1));
            }
        }
        return result;
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

    private static boolean isMainProject(String moduleName) {
        return false;
    }
}
