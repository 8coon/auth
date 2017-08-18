package org.minecraftshire.auth.workers.uploadProcessor;


import org.minecraftshire.auth.storages.upload.UploadFileInfo;
import org.minecraftshire.auth.storages.upload.UploadInfo;
import org.springframework.jdbc.core.JdbcTemplate;

public class UploadProcessorWorkerPayload {

    private JdbcTemplate jdbc;
    private UploadFileInfo fileInfo;
    private UploadInfo info;


    public UploadProcessorWorkerPayload(
            JdbcTemplate jdbc,
            UploadFileInfo fileInfo,
            UploadInfo info
    ) {
        this.jdbc = jdbc;
        this.fileInfo = fileInfo;
        this.info = info;
    }


    public JdbcTemplate getJdbc() {
        return jdbc;
    }

    public UploadFileInfo getFileInfo() {
        return fileInfo;
    }

    public UploadInfo getInfo() {
        return info;
    }
}
