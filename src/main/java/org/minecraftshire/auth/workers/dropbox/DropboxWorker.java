package org.minecraftshire.auth.workers.dropbox;


import org.minecraftshire.auth.utils.logging.Logger;
import org.minecraftshire.auth.workers.Worker;


public class DropboxWorker extends Worker<DropboxWorkerPayload> {

    @Override
    protected void process(DropboxWorkerPayload payload) throws Throwable {
        Logger.getLogger().info("HELLO WORLD FROM WORKER! ", payload.getId());
    }

}
