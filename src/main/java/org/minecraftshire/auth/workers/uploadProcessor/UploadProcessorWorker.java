package org.minecraftshire.auth.workers.uploadProcessor;


import org.minecraftshire.auth.workers.Worker;


public class UploadProcessorWorker extends Worker<UploadProcessorWorkerPayload> {

    @Override
    protected void process(UploadProcessorWorkerPayload payload) throws Throwable {
        payload.getInfo().invokeCallback(this, payload);
    }

}
