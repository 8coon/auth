package org.minecraftshire.auth.storages.upload;


import org.springframework.web.multipart.MultipartFile;


public class UploadFileInfo {

    private MultipartFile file;


    public UploadFileInfo(
            MultipartFile file
    ) {
        this.file = file;
    }


    public MultipartFile getFile() {
        return this.file;
    }

}
