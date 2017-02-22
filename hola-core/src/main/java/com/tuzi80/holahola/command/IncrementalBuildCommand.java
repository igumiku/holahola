package com.tuzi80.holahola.command;

import com.tuzi80.holahola.builder.Builder;

/**
 * IncrementalBuildCommand
 * Created by betsy on 2/20/17.
 */
public abstract class IncrementalBuildCommand extends BuildCommand {
    public IncrementalBuildCommand(Builder builder) {
        super(builder);
        super.init("abstract increment build command");
    }
}
