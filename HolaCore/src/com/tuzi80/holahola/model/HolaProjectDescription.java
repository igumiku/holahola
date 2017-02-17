package com.tuzi80.holahola.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by betsy on 2/17/17.
 */
public class HolaProjectDescription {
    private String project_type;
    private String android_gradle_plugin_version;

    private String freeline_gradle_plugin_version;
    private String java_home;
    private String freeline_cache_dir;
    private boolean auto_dependency;
    private String product_flavor;
    private String build_script;
    private String build_script_work_directory;
    private String root_dir;
    private String main_project_name;
    private String main_project_dir;
    private String build_directory;
    private String build_cache_dir;
    private String build_tools_version;
    private String sdk_directory;
    private String build_tools_directory;
    private String compile_sdk_version;
    private String compile_sdk_directory;
    private String packageName;//TODO:需要修改gadle脚本中对应的pakage为这个名字
    private String debug_package;
    private String main_manifest_path;
    private String launcher;
    private String apk_path;
    private ArrayList<String> extra_dep_res_paths;

    private ArrayList<String> exclude_dep_res_paths;
    private String main_r_path;
    private boolean use_jdk8;
    private ArrayList<String> main_src_directory;
    private ArrayList<String> main_res_directory;
    private ArrayList<String> main_assets_directory;
    private ArrayList<String> main_jni_directory;
    private ArrayList<String> main_jniLibs_directory;
    private HashMap<String, ModuleSourceSet> project_source_sets;
    private ArrayList<Module> modules;
    private HashMap<String, ArrayList<String>> module_dependencies;

    public String getProject_type() {
        return project_type;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
    }

    public String getAndroid_gradle_plugin_version() {
        return android_gradle_plugin_version;
    }

    public void setAndroid_gradle_plugin_version(String android_gradle_plugin_version) {
        this.android_gradle_plugin_version = android_gradle_plugin_version;
    }

    public String getFreeline_gradle_plugin_version() {
        return freeline_gradle_plugin_version;
    }

    public void setFreeline_gradle_plugin_version(String freeline_gradle_plugin_version) {
        this.freeline_gradle_plugin_version = freeline_gradle_plugin_version;
    }

    public String getJava_home() {
        return java_home;
    }

    public void setJava_home(String java_home) {
        this.java_home = java_home;
    }

    public String getFreeline_cache_dir() {
        return freeline_cache_dir;
    }

    public void setFreeline_cache_dir(String freeline_cache_dir) {
        this.freeline_cache_dir = freeline_cache_dir;
    }

    public boolean isAuto_dependency() {
        return auto_dependency;
    }

    public void setAuto_dependency(boolean auto_dependency) {
        this.auto_dependency = auto_dependency;
    }

    public String getProduct_flavor() {
        return product_flavor;
    }

    public void setProduct_flavor(String product_flavor) {
        this.product_flavor = product_flavor;
    }

    public String getBuild_script() {
        return build_script;
    }

    public void setBuild_script(String build_script) {
        this.build_script = build_script;
    }

    public String getBuild_script_work_directory() {
        return build_script_work_directory;
    }

    public void setBuild_script_work_directory(String build_script_work_directory) {
        this.build_script_work_directory = build_script_work_directory;
    }

    public String getMain_project_name() {
        return main_project_name;
    }

    public void setMain_project_name(String main_project_name) {
        this.main_project_name = main_project_name;
    }

    public String getRoot_dir() {
        return root_dir;
    }

    public void setRoot_dir(String root_dir) {
        this.root_dir = root_dir;
    }

    public String getMain_project_dir() {
        return main_project_dir;
    }

    public void setMain_project_dir(String main_project_dir) {
        this.main_project_dir = main_project_dir;
    }

    public String getBuild_directory() {
        return build_directory;
    }

    public void setBuild_directory(String build_directory) {
        this.build_directory = build_directory;
    }

    public String getBuild_cache_dir() {
        return build_cache_dir;
    }

    public void setBuild_cache_dir(String build_cache_dir) {
        this.build_cache_dir = build_cache_dir;
    }

    public String getBuild_tools_version() {
        return build_tools_version;
    }

    public void setBuild_tools_version(String build_tools_version) {
        this.build_tools_version = build_tools_version;
    }

    public String getSdk_directory() {
        return sdk_directory;
    }

    public void setSdk_directory(String sdk_directory) {
        this.sdk_directory = sdk_directory;
    }

    public String getBuild_tools_directory() {
        return build_tools_directory;
    }

    public void setBuild_tools_directory(String build_tools_directory) {
        this.build_tools_directory = build_tools_directory;
    }

    public String getCompile_sdk_version() {
        return compile_sdk_version;
    }

    public void setCompile_sdk_version(String compile_sdk_version) {
        this.compile_sdk_version = compile_sdk_version;
    }

    public String getCompile_sdk_directory() {
        return compile_sdk_directory;
    }

    public void setCompile_sdk_directory(String compile_sdk_directory) {
        this.compile_sdk_directory = compile_sdk_directory;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDebug_package() {
        return debug_package;
    }

    public void setDebug_package(String debug_package) {
        this.debug_package = debug_package;
    }

    public String getMain_manifest_path() {
        return main_manifest_path;
    }

    public void setMain_manifest_path(String main_manifest_path) {
        this.main_manifest_path = main_manifest_path;
    }

    public String getLauncher() {
        return launcher;
    }

    public void setLauncher(String launcher) {
        this.launcher = launcher;
    }

    public String getApk_path() {
        return apk_path;
    }

    public void setApk_path(String apk_path) {
        this.apk_path = apk_path;
    }

    public ArrayList<String> getExtra_dep_res_paths() {
        return extra_dep_res_paths;
    }

    public void setExtra_dep_res_paths(ArrayList<String> extra_dep_res_paths) {
        this.extra_dep_res_paths = extra_dep_res_paths;
    }

    public ArrayList<String> getExclude_dep_res_paths() {
        return exclude_dep_res_paths;
    }

    public void setExclude_dep_res_paths(ArrayList<String> exclude_dep_res_paths) {
        this.exclude_dep_res_paths = exclude_dep_res_paths;
    }

    public String getMain_r_path() {
        return main_r_path;
    }

    public void setMain_r_path(String main_r_path) {
        this.main_r_path = main_r_path;
    }

    public boolean isUse_jdk8() {
        return use_jdk8;
    }

    public void setUse_jdk8(boolean use_jdk8) {
        this.use_jdk8 = use_jdk8;
    }

    public ArrayList<String> getMain_src_directory() {
        return main_src_directory;
    }

    public void setMain_src_directory(ArrayList<String> main_src_directory) {
        this.main_src_directory = main_src_directory;
    }

    public ArrayList<String> getMain_res_directory() {
        return main_res_directory;
    }

    public void setMain_res_directory(ArrayList<String> main_res_directory) {
        this.main_res_directory = main_res_directory;
    }

    public ArrayList<String> getMain_assets_directory() {
        return main_assets_directory;
    }

    public void setMain_assets_directory(ArrayList<String> main_assets_directory) {
        this.main_assets_directory = main_assets_directory;
    }

    public ArrayList<String> getMain_jni_directory() {
        return main_jni_directory;
    }

    public void setMain_jni_directory(ArrayList<String> main_jni_directory) {
        this.main_jni_directory = main_jni_directory;
    }

    public ArrayList<String> getMain_jniLibs_directory() {
        return main_jniLibs_directory;
    }

    public void setMain_jniLibs_directory(ArrayList<String> main_jniLibs_directory) {
        this.main_jniLibs_directory = main_jniLibs_directory;
    }

    public HashMap<String, ModuleSourceSet> getProject_source_sets() {
        return project_source_sets;
    }

    public void setProject_source_sets(HashMap<String, ModuleSourceSet> project_source_sets) {
        this.project_source_sets = project_source_sets;
    }

    public HashMap<String, ArrayList<String>> getModule_dependencies() {
        return module_dependencies;
    }

    public void setModule_dependencies(HashMap<String, ArrayList<String>> module_dependencies) {
        this.module_dependencies = module_dependencies;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }

    public void setModules(ArrayList<Module> modules) {
        this.modules = modules;
    }
}
