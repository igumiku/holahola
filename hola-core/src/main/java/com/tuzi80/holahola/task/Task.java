package com.tuzi80.holahola.task;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Base task.
 * Created by betsy on 2/21/17.
 */
public abstract class Task {
    private String mName;

    private Set<Task> mParentTask = new HashSet<Task>();
    private Set<Task> mChildTask = new HashSet<Task>();

    public STATUS getmStatus() {
        return mStatus;
    }

    public void setmStatus(STATUS mStatus) {
        this.mStatus = mStatus;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    enum STATUS {
        READY, FAILED, NOT_START, WAITING, WORKING, SUCCESS
    }

    private STATUS mStatus = STATUS.READY;

    public long mStartTime = 0;
    public long mRunStartTime = 0;
    public long mCostTime = 0;
    public String mRunningMessage = "running";
    public String mFinishedMessage = "finished";
    private Lock mLock=new ReentrantLock();
    public Condition mCondition=mLock.newCondition();
    public Exception mInterruptedException;

    public void init(String name) {
        mName = name;
    }

    public void addParentTask(Task task) {
        if (!mParentTask.contains(task)) {
            mParentTask.add(task);
            task.addChildTask(this);
        }
    }


    private void addChildTask(Task task) {
        if (!mChildTask.contains(task)) {
            mChildTask.add(task);
            task.addParentTask(this);
        }
    }

    public boolean isAllParentFinished() {
        for (Task task : mParentTask) {
            if (task.getmStatus() != STATUS.FAILED && task.getmStatus() != STATUS.SUCCESS) {
                return false;
            }
        }

        return true;
    }

    public void waitCondition() {
        mLock.lock();
        try {
            mCondition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mLock.unlock();
    }

    public void notifyCondition() {
        mLock.lock();
        mCondition.notify();
        mLock.unlock();
    }

    public abstract void execute();

    public boolean canShowLog() {
        return mStatus == STATUS.SUCCESS || mStatus == STATUS.WAITING || mStatus == STATUS.FAILED;
    }

    public void debug(String message) {
    }

    public Set<Task> getmChildTask(){
        return mChildTask;
    }

    public Set<Task> getmParentTask(){
        return mParentTask;
    }

//    public  find_root_tasks(task_list):
//            return filter(lambda task: len(task.parent_tasks) == 0, task_list)
//
//
//    def find_last_tasks(task_list):
//            return filter(lambda task: len(task.child_tasks) == 0, task_list)
}
