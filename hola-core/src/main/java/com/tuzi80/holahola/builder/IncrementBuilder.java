package com.tuzi80.holahola.builder;

import com.tuzi80.holahola.model.HolaProjectDescription;
import com.tuzi80.holahola.task.TaskEngine;

import java.util.HashMap;

/**
 * Increment builder.
 * Created by betsy on 2/20/17.
 */
public abstract class IncrementBuilder extends Builder {
    private HashMap<String, String> mChangeFiels;

    public void init(HashMap<String, String> changeFiles, String builderName, HolaProjectDescription description, TaskEngine engine) {
        super.init(builderName, description, engine);
    }

    public abstract void checkBuildEnvironment();
}
