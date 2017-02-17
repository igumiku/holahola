package com.tuzi80.holahola.model;

import java.util.ArrayList;

/**
 * Created by betsy on 2/17/17.
 */
public class ModuleSourceSet {
    private ArrayList<String> main_src_directory;
    private ArrayList<String> main_res_directory;
    private ArrayList<String> main_assets_directory;
    private ArrayList<String> main_jni_directory;
    private ArrayList<String> main_jniLibs_directory;
    private String main_manifest_path;

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

    public String getMain_manifest_path() {
        return main_manifest_path;
    }

    public void setMain_manifest_path(String main_manifest_path) {
        this.main_manifest_path = main_manifest_path;
    }
}