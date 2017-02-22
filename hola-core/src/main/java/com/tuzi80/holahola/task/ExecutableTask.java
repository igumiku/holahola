package com.tuzi80.holahola.task;

import org.apache.logging.log4j.LogManager;

import static com.tuzi80.holahola.task.Task.STATUS.SUCCESS;
import static com.tuzi80.holahola.task.Task.STATUS.WORKING;
import static org.ietf.jgss.GSSException.FAILURE;

/**
 * Executable task.
 * Created by betsy on 2/21/17.
 */
public class ExecutableTask implements Runnable{
    private Task mTask;
    private TaskEngine mTaskEngine;
    private String mTplDebugMessage;

    public void init(Task task,TaskEngine engine){
        mTask=task;
        mTaskEngine=engine;
    }

    public void execute(){

        mTask.mStartTime = System.currentTimeMillis();
        mTask.setmStatus(Task.STATUS.WAITING);
        while(!mTask.isAllParentFinished()){
            mTask.waitCondition();
        }

        mTask.mRunStartTime=System.currentTimeMillis();
        mTask.setmStatus(WORKING);
        LogManager.getLogger().debug(String.format("start to run after waiting %ss"
                ,mTask.getmName(),(mTask.mRunStartTime-mTask.mStartTime)/1000));
        // check if task need to interrupt before being executing
        if(mTask.mInterruptedException!=null){
            mTask.setmStatus(Task.STATUS.FAILED);
            passInterruptedException();
            return;
        }

        try {
            mTask.execute();
            mTask.setmStatus(SUCCESS);
        }catch (Exception e){
            mTask.mInterruptedException= e;
            mTask.setmStatus(Task.STATUS.FAILED);
        }

        mTask.mCostTime=System.currentTimeMillis()-mTask.mRunStartTime;
        LogManager.getLogger().debug(String.format("%s finish in %ss"
                ,mTask.getmName(),mTask.mCostTime/1000));

//      check if task need to interrupt after being executing
        if(mTask.mInterruptedException!=null){
            passInterruptedException();
            return;
        }

        for(Task task:mTask.getmChildTask()){
            task.notifyCondition();
        }

        checkEngineFinished();
    }

    private void passInterruptedException(){
        for(Task childTask:mTaskEngine.getRunningTask()){
            childTask.mInterruptedException=mTask.mInterruptedException;
            childTask.notifyCondition();
        }

        if(mTaskEngine.isAllTasksFinished()){
            mTaskEngine.interrupt(mTask.mInterruptedException);
            mTaskEngine.finish();
        }
    }

    private void checkEngineFinished(){
        if(mTaskEngine.isAllTasksFinished()){
            mTaskEngine.finish();
        }
    }


    public void debug(){}

    public void run() {
        execute();
    }
}
