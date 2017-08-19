package org.minecraftshire.auth.workers.uploadProcessor;


import org.minecraftshire.auth.workers.ScheduledWorker;
import org.minecraftshire.auth.workers.Worker;


@ScheduledWorker(2000)
public class UploadProcessorWorker extends Worker<UploadProcessorWorkerPayload> {

    @Override
    protected void process(UploadProcessorWorkerPayload payload) throws Throwable {
        payload.getInfo().invokeCallback(this, payload);
    }

}
