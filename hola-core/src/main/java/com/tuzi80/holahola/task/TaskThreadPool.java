package com.tuzi80.holahola.task;

import org.apache.logging.log4j.LogManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Thread pool to excute task.
 * Created by betsy on 2/22/17.
 */
public class TaskThreadPool {
    private BlockingQueue<ExecutableTask> mQueue = new LinkedBlockingDeque<ExecutableTask>();

    ExecutorService mExecutor = Executors.newFixedThreadPool(6);

    public void start() {
        while (!mQueue.isEmpty()) {
            try {
                ExecutableTask task = mQueue.take();
                mExecutor.execute(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
                LogManager.getLogger().debug("executor thread catch exception");
            }
        }
        mExecutor.shutdown();
    }

    public void addTask(ExecutableTask task) {
        try {
            mQueue.put(task);//always blocking.
        } catch (InterruptedException e) {
            e.printStackTrace();
            LogManager.getLogger().debug("add task  catch exception");
        }
    }
}
