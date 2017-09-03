package org.minecraftshire.auth.storages.upload;


import org.minecraftshire.auth.data.session.SessionData;
import org.minecraftshire.auth.workers.Worker;
import org.minecraftshire.auth.workers.WorkerDoneCallback;
import org.minecraftshire.auth.workers.uploadProcessor.UploadProcessorWorkerPayload;


public class UploadInfo {

    public static final int STATUS_AWAITING_CLIENT = 0;
    public static final int STATUS_NOT_FOUND = 1;
    public static final int STATUS_PROCESSING = 2;
    public static final int STATUS_FINISHED = 3;


    private WorkerDoneCallback<UploadProcessorWorkerPayload> onDone;
    private long token;
    private int status;
    private SessionData sessionData;


    public UploadInfo(
            WorkerDoneCallback<UploadProcessorWorkerPayload> onDone,
            long token,
            SessionData sessionData
    ) {
        this.onDone = onDone;
        this.token = token;
        this.status = STATUS_AWAITING_CLIENT;
        this.sessionData = sessionData;
    }


    public void invokeCallback(Worker<UploadProcessorWorkerPayload> worker, UploadProcessorWorkerPayload payload) {
        onDone.call(worker, payload);
    }

    public long getToken() {
        return token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public WorkerDoneCallback<UploadProcessorWorkerPayload> getDoneCallback() {
        return onDone;
    }

    public SessionData getSessionData() {
        return sessionData;
    }
}
