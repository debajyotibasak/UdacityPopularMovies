package com.debajyotibasak.udacitypopularmovies.utils;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

/**
 * Created by debajyotibasak on 06/03/18.
 */
@Singleton
public class AppExecutor {

    private final Executor diskIO;
    private final Executor mainThread;

    public AppExecutor(Executor diskIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }
}
