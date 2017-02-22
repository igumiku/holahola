package com.tuzi80.holahola.command;

import com.sun.javafx.tk.Toolkit;
import com.tuzi80.holahola.model.HolaProjectDescription;
import com.tuzi80.holahola.task.TaskEngine;

/**
 * Hola build command.
 * Created by betsy on 2/20/17.
 */
public class HolaBuildCommand extends Command {
    private HolaProjectDescription mProjectDescription;
    private TaskEngine mTaskEngine;

    public HolaBuildCommand(HolaProjectDescription description, TaskEngine engine) {
        mProjectDescription = description;
        mTaskEngine = engine;
    }

    public void execute() {

    }
}
