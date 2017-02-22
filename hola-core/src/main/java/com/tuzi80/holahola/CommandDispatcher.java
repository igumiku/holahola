package com.tuzi80.holahola;

import com.google.gson.Gson;
import com.tuzi80.holahola.builder.Builder;
import com.tuzi80.holahola.builder.GradleCleanBuilder;
import com.tuzi80.holahola.command.*;
import com.tuzi80.holahola.json.JsonUtils;
import com.tuzi80.holahola.model.HolaProjectDescription;
import com.tuzi80.holahola.model.Preference;
import com.tuzi80.holahola.task.TaskEngine;
import com.tuzi80.holahola.utils.CommonUtil;

/**
 * Dispatch build command.
 * Created by betsy on 2/17/17.
 */
public class CommandDispatcher {

    /**
     * All description info about the whole project.
     */
    private static HolaProjectDescription mProjectDescription;

    private static String[] mArgs;

    private Command mBuildCommand;

    private Builder mBuilder;

    private TaskEngine mTaskEngine = new TaskEngine();

    /**
     * Build project according to args.
     *
     * @param args args.
     *             1.full clean build.
     *             2.fast build.
     */
    public void buildProject(String[] args) {
        mArgs = args;
        mProjectDescription = new Gson().fromJson(JsonUtils.readJsonFromFile(System.getProperty("user.dir")
                + "freeline_project_description.json"), HolaProjectDescription.class);
        Preference.getmInstance().setmProjectDescription(mProjectDescription);
        if (CommonUtil.isContain(args, "--cleanBuild")) {
            boolean isBuildAllProject = CommonUtil.isContain(args, "--all");
            boolean waitForDebug = CommonUtil.isContain(args, "--wait");
            setUpCleanBuildCommand(isBuildAllProject, waitForDebug);
        } else if (CommonUtil.isContain(args, "--version")) {

        } else if (CommonUtil.isContain(args, "clean")) {

        } else {
            mBuildCommand = new HolaBuildCommand(mProjectDescription, mTaskEngine);
        }

        try {
            mBuildCommand.execute();
        } catch (Exception e) {
            //TODO:do something about all types of exception.
            e.printStackTrace();
        }
    }

    private void setUpCleanBuildCommand(boolean all, boolean wait) {
        if ("gradle".equals(mProjectDescription.getProject_type())) {
            mBuilder = new GradleCleanBuilder();
        }
        mBuildCommand = new GradleCleanBuildCommand(mBuilder);
    }
}
