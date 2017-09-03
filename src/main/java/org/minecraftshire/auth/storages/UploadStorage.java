package org.minecraftshire.auth.storages;


import org.minecraftshire.auth.data.session.SessionData;
import org.minecraftshire.auth.storages.upload.UploadFileInfo;
import org.minecraftshire.auth.storages.upload.UploadInfo;
import org.minecraftshire.auth.workers.WorkerDoneCallback;
import org.minecraftshire.auth.workers.uploadProcessor.UploadProcessorWorker;
import org.minecraftshire.auth.workers.uploadProcessor.UploadProcessorWorkerPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class UploadStorage {

    private ConcurrentHashMap<Long, UploadInfo> uploads = new ConcurrentHashMap<>();
    private SecureRandom random = new SecureRandom();
    private JdbcTemplate jdbc;
    private UploadProcessorWorker uploadProcessor;


    @Autowired
    public UploadStorage(JdbcTemplate jdbc, UploadProcessorWorker uploadProcessor) {
        this.jdbc = jdbc;
        this.uploadProcessor = uploadProcessor;
    }


    // Generate unique token
    public long requestToken(WorkerDoneCallback<UploadProcessorWorkerPayload> onDone, SessionData sessionData) {
        long token;

        do {
            // Генерируем новый токен, пока м не можем сложить его в словарь (putIfAbsent возвращает не null)
            token = random.nextLong();
        } while (uploads.putIfAbsent(token, new UploadInfo(onDone, token, sessionData)) != null);

        return token;
    }


    public int getUploadStatus(long token) {
        UploadInfo info = uploads.get(token);

        // Если токен не найден.
        if (info == null) {
            return UploadInfo.STATUS_NOT_FOUND;
        }

        int status = info.getStatus();

        // Если файл по токену уже загрузили и обработали, то удалим токен.
        if (status == UploadInfo.STATUS_FINISHED) {
            uploads.remove(token);
        }

        return status;
    }


    public void onUploaded(long token, MultipartFile file) {
        UploadInfo info = uploads.get(token);
        info.setStatus(UploadInfo.STATUS_PROCESSING);

        UploadFileInfo fileInfo = new UploadFileInfo(file);
        UploadProcessorWorkerPayload payload = new UploadProcessorWorkerPayload(jdbc, fileInfo, info);

        uploadProcessor.schedule(payload, (WorkerDoneCallback<UploadProcessorWorkerPayload>) (worker, payload1) -> {
            info.setStatus(UploadInfo.STATUS_FINISHED);
        });
    }


    public static String getUrl(String hash, String contentType) {
        String url = hash;

        // Определяем расширение "файла" по его contentType.
        if ("image/jpeg".equalsIgnoreCase(contentType)) {
            url += ".jpg";
        } else if ("image/png".equalsIgnoreCase(contentType)) {
            url += ".png";
        } else {
            // У файла выставлен некорректный contentType -- не дадим пользователю его скачать!
            url = null;
        }

        return url;
    }

}
