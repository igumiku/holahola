package com.tuzi80.holahola.command;

import java.util.ArrayList;

/**
 * Macro command.
 * Created by betsy on 2/20/17.
 */
public class MacroCommand extends Command {
    private ArrayList<Command> mCommandList = new ArrayList<Command>();

    public MacroCommand() {
        super();
        super.init("macro command");
    }

    public void execute() {

    }

    public void addCommand(Command command) {
        if (command != null) {
            mCommandList.add(command);
        }
    }

    public void removeCommand(Command command) {
        mCommandList.remove(command);
    }
}
