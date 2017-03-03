package com.tuzi80.holahola.task;

import com.tuzi80.holahola.utils.FileuUtils;

import java.io.File;
import java.nio.file.Paths;

/**
 * Clean all build cache.
 * Created by betsy on 2/27/17.
 */
public class CleanAllCacheTask extends Task {
    private String mCacheDir;
    private String[] mIgnores;

    public CleanAllCacheTask(String cacheDir, String[] ignore) {
        super();
        super.init("clean_all_cache_task");
        mCacheDir = cacheDir;
        mIgnores = ignore;
    }

    public void execute() {
        remove(mCacheDir);
    }

    private void remove(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                if (mIgnores.length > 0) {
                    for (int i = 0; i < mIgnores.length; i++) {
                        if (file.getName().equals(mIgnores[i])) {
                            break;
                        }
                    }
                    file.delete();
                } else {
                    file.delete();
                }
            } else {
                remove(Paths.get(dirPath, file.getName()).toString());
            }

        }
    }
}
