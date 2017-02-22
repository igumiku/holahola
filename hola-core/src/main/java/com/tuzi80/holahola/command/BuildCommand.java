package com.tuzi80.holahola.command;

import com.tuzi80.holahola.builder.Builder;

/**
 * Abstract build command.
 * Created by betsy on 2/20/17.
 */
public abstract class BuildCommand extends Command {
    public Builder mBuilder;

    public BuildCommand(Builder builder) {
        this.mBuilder = builder;
        super.init("build command");
    }
}
