package com.tuzi80.holahola.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tuzi80.holahola.builder.Builder;
import com.tuzi80.holahola.json.JsonUtils;
import com.tuzi80.holahola.model.HolaProjectDescription;
import com.tuzi80.holahola.model.Module;
import com.tuzi80.holahola.model.ModuleInfo;
import com.tuzi80.holahola.model.ProjectInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.InitialContext;
import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Files option.
 * Created by betsy on 2/21/17.
 */
public class FileuUtils {

    /**
     * Write into file.
     *
     * @param path    file path.
     * @param content file content.
     */
    public static void writeFileContent(String path, String content) throws IOException {
        File file = new File(path);
        if (!file.exists())
            file.createNewFile();
        FileOutputStream out = new FileOutputStream(file, true);

        StringBuffer sb = new StringBuffer();
        sb.append(content);
        out.write(sb.toString().getBytes("utf-8"));
        out.close();
    }

    public static String getFilecontent(String settingsPath) {
        StringBuffer list = new StringBuffer();
        try {
            String encoding = "UTF-8";
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

    public static void getProjectInfo(HolaProjectDescription description) {
        LogManager.getLogger(FileuUtils.class).debug("collecting project info, please wait a while...");
        ArrayList<Module> modules = description.getModules();
        ProjectInfo projectInfo=new ProjectInfo();
        if (modules == null) {
            modules = AndroidTools.getAllModule(System.getProperty("user.dir").toString());
        }

        String jarDependenciesPath = Paths.get(description.getBuild_cache_dir(), "jar_dependencies.json").toString();

        ArrayList<String> jarDependencies = new ArrayList<String>();
        if (CommonUtil.isFileExists(jarDependenciesPath)) {
            String json = JsonUtils.readJsonFromFile(jarDependenciesPath);
            jarDependencies = new Gson().fromJson(json, new TypeToken<List<String>>() {
            }.getType());
        }

        for (Module module : modules) {
            if (description.getProject_source_sets().get(module.getName()) != null) {
                ModuleInfo moduleInfo = new ModuleInfo();
                moduleInfo.setModule(module);
                moduleInfo.setDep_jar_path(jarDependencies);
                moduleInfo.setPackagename(AndroidTools.getPackageName(description.getProject_source_sets()
                        .get(module.getName()).getMain_manifest_path()));

                if(description.getModule_dependencies()!=null){
                    moduleInfo.setLocal_module_dep(description.getModule_dependencies().get(module.getName()));
                }

                projectInfo.getmModuleInfos().put(module.getName(),moduleInfo);

            }
        }

    }
}
