package com.tuzi80.holahola.command;

/**
 * Base command
 * Created by betsy on 2/20/17.
 */
public abstract class Command {
    private String mCommandName = "";

    protected void init(String name){
        mCommandName = name;
    }

    public abstract void execute();

    public void debug(String message) {

    }
}
