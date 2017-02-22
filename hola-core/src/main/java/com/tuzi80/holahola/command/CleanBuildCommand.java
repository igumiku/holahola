package com.tuzi80.holahola.command;

import com.tuzi80.holahola.builder.Builder;

/**
 * Clean build command.
 * Created by betsy on 2/20/17.
 */
public abstract class CleanBuildCommand extends BuildCommand {
    public CleanBuildCommand(Builder builder) {
        super(builder);
        super.init("clean build command");
    }
}
