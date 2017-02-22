package com.tuzi80.holahola.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Task engine.
 * Created by betsy on 2/20/17.
 */
public class TaskEngine {
    private Logger mLogger = LogManager.getLogger();
    private ConcurrentLinkedQueue<Task> mQueue = new ConcurrentLinkedQueue<Task>();
    private Lock mLock = new ReentrantLock();
    private Condition mCondition = mLock.newCondition();
    private long mStartTime = 0;
    private long mCostTime = 0;

    private Exception mInterruptException = null;
    private String mTplLoggerMessage = "[task_engine] {}";

    private TaskThreadPool mPool;

    private Set<Task> mRootTasks = new HashSet<Task>();
    private Map<String, Task> mTaskDict = new HashMap<String, Task>();
    private Map<Integer, Set<Task>> mTaskDepDict = new HashMap<Integer, Set<Task>>();
    private ArrayList<Task> mSortedTask = new ArrayList<Task>();

    public TaskEngine() {
        init();
    }

    public void init() {
        mPool = new TaskThreadPool();
    }

    private void initAttr() {
        mRootTasks.clear();
        mTaskDepDict.clear();
        mTaskDict.clear();
        mSortedTask.clear();
    }


    public void debug(String message) {
        mLogger.debug(message);
    }

    public void addRootTask(HashSet<Task> tasks) {
        for (Iterator it = tasks.iterator(); it.hasNext(); ) {
            addRootTask((Task) it.next());
        }
    }

    public void addRootTask(Task task) {
        if (!mRootTasks.contains(task)) {
            mRootTasks.add(task);
        }
    }

    public void start() throws Exception {
        mStartTime = System.currentTimeMillis();
        mInterruptException = null;
        prepare();
        waitCondition();
        if (mInterruptException != null) {
            throw mInterruptException;
        }
    }

    public void finish() {
        mCostTime = System.currentTimeMillis() - mStartTime;
        mLogger.debug(String.format("it takes task engine %s to execute tasks.", mCostTime / 1000));
        initAttr();
        notifyCondition();
    }

    public boolean isAllTasksFinished() {
        for (Task task : mTaskDict.values()) {
            if (task.getmStatus() != Task.STATUS.SUCCESS && task.getmStatus() != Task.STATUS.FAILED) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Task> getRunningTask() {
        ArrayList<Task> res = new ArrayList<Task>();
        for (Task task : mTaskDict.values()) {
            if (task.getmStatus() != Task.STATUS.SUCCESS && task.getmStatus() != Task.STATUS.FAILED) {
                res.add(task);
            }
        }
        return res;
    }

    private void prepare() throws Exception {
        mQueue.clear();
        for (Iterator it = mRootTasks.iterator(); it.hasNext(); ) {
            mQueue.add((Task) it.next());
        }

        Set<Task> hasAddedTasks = new HashSet<Task>();

        while (!mQueue.isEmpty()) {
            Task task = mQueue.poll();
            hasAddedTasks.add(task);

            if (!mTaskDict.containsKey(task.getmName())) {
                mTaskDict.put(task.getmName(), task);
            }

            for (Iterator it = task.getmChildTask().iterator(); it.hasNext(); ) {
                if (!hasAddedTasks.contains((Task) it.next()))
                    mQueue.add(task);
            }
        }

        ArrayList<Integer> depthArray = new ArrayList<Integer>();

        for (Task task : mTaskDict.values()) {
            Integer depth = calculateTaskDepth(task);
            if (mTaskDepDict.containsKey(depth)) {
                mTaskDepDict.get(depth).add(task);
            } else {
                Set<Task> tmp = new HashSet<Task>();
                tmp.add(task);
                mTaskDepDict.put(depth, tmp);
                depthArray.add(depth);
            }
        }

        Collections.sort(depthArray, new SortInteger());

        for (int i = 0; i < depthArray.size(); i++) {
            Set<Task> tasks = mTaskDepDict.get(depthArray.get(i));
            for (Task task : tasks) {
                mLogger.debug(String.format("depth: %s, task: %s", depthArray.get(i), task));
                mSortedTask.add(task);
            }
        }

//        self._logger.set_sorted_tasks(self.sorted_tasks)

        for (int i = 0; i < mSortedTask.size(); i++) {
            Task task = mSortedTask.get(i);
            ExecutableTask executableTask = new ExecutableTask();
            executableTask.init(task, this);
            mPool.addTask(executableTask);
        }

        //start the task thread.
        mPool.start();

    }

    private void waitCondition() {
        mLock.lock();
        try {
            mCondition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mLock.unlock();
    }

    private void notifyCondition() {
        mLock.lock();
        mCondition.notify();
        mLock.unlock();
    }

    public void interrupt(Exception exception) {
        mInterruptException = exception;
        mLogger.debug("task engine occurs exception, engine will exit.");
    }

    private int calculateTaskDepth(Task task) {
        ArrayList<String> depth = new ArrayList<String>();
        ConcurrentLinkedQueue<Task> tasks = new ConcurrentLinkedQueue<Task>();
        tasks.add(task);
        while (!tasks.isEmpty()) {
            Task parentTask = tasks.poll();
            if (!depth.contains(parentTask.getmName())) {
                depth.add(parentTask.getmName());
            }

            for (Iterator it = task.getmParentTask().iterator(); it.hasNext(); ) {
                Task tmp = (Task) it.next();
                if (!depth.contains(tmp.getmName())) {
                    tasks.add(task);
                }
            }
        }

        return depth.size();
    }

    private class SortInteger implements Comparator {
        public int compare(Object o1, Object o2) {
            Integer i1 = (Integer) o1;
            Integer i2 = (Integer) o2;
            if (i1 > i2) {
                return 1;
            } else {
                return 0;
            }
        }
    }


}
