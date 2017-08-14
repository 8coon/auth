package org.minecraftshire.auth.workers;


public interface WorkerFailCallback<T> {
    void call(Worker<T> worker, Throwable error);
}
