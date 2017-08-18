package org.minecraftshire.auth.controllers;


import org.minecraftshire.auth.aspects.AuthRequired;
import org.minecraftshire.auth.aspects.UserAgent;
import org.minecraftshire.auth.data.SessionData;
import org.minecraftshire.auth.data.UploadTokenData;
import org.minecraftshire.auth.responses.StatusResponse;
import org.minecraftshire.auth.storages.UploadStorage;
import org.minecraftshire.auth.storages.upload.UploadInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin(origins = {"http://localhost:3000", "*"})
@RequestMapping("/upload")
public class UploadController {

    private UploadStorage uploadStorage;

    private static final int RETRY_AFTER_DEFAULT = 1000;


    @Autowired
    public UploadController(UploadStorage uploadStorage) {
        this.uploadStorage = uploadStorage;
    }


    @AuthRequired
    @PostMapping("/status")
    public ResponseEntity status(
            @RequestBody UploadTokenData tokenData,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        int status = uploadStorage.getUploadStatus(Long.valueOf(tokenData.getUploadToken()));
        String text = "none";

        switch (status) {
            case UploadInfo.STATUS_AWAITING_CLIENT: text = "awaiting_client"; break;
            case UploadInfo.STATUS_PROCESSING: text = "processing"; break;
            case UploadInfo.STATUS_FINISHED: text = "finished"; break;
        }

        return new ResponseEntity<>(
                new StatusResponse(text, UploadController.RETRY_AFTER_DEFAULT),
                HttpStatus.OK
        );
    }


    @PostMapping("/upload/{token}")
    public ResponseEntity upload(
            @PathVariable("token") String strToken,
            @RequestParam("file") MultipartFile file
    ) {
        long token = Long.valueOf(strToken);

        if (uploadStorage.getUploadStatus(token) != UploadInfo.STATUS_AWAITING_CLIENT) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        uploadStorage.onUploaded(token, file);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
