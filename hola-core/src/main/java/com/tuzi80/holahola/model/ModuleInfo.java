package com.tuzi80.holahola.model;

import java.util.ArrayList;

/**
 * moduleInfo
 * Created by betsy on 2/21/17.
 */
public class ModuleInfo extends Module{
    private String relative_dir;
    private ArrayList<String> dep_jar_path;
    private String packagename;
    private ArrayList<String> local_module_dep;

    public void setModule(Module module){
        setName(module.getName());
        setPath(module.getPath());
        setRelative_dir(module.getPath());
    }

    public String getRelative_dir() {
        return relative_dir;
    }

    public void setRelative_dir(String relative_dir) {
        this.relative_dir = relative_dir;
    }

    public ArrayList<String> getDep_jar_path() {
        return dep_jar_path;
    }

    public void setDep_jar_path(ArrayList<String> dep_jar_path) {
        this.dep_jar_path = dep_jar_path;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public ArrayList<String>  getLocal_module_dep() {
        return local_module_dep;
    }

    public void setLocal_module_dep(ArrayList<String>  local_module_dep) {
        this.local_module_dep = local_module_dep;
    }
}
