package org.minecraftshire.auth.controllers;

import org.minecraftshire.auth.Server;
import org.minecraftshire.auth.aspects.AuthRequired;
import org.minecraftshire.auth.aspects.UserAgent;
import org.minecraftshire.auth.data.character.*;
import org.minecraftshire.auth.data.session.SessionData;
import org.minecraftshire.auth.exceptions.ExceptionWithCause;
import org.minecraftshire.auth.exceptions.WrongCredentialsException;
import org.minecraftshire.auth.repositories.CharacterRepository;
import org.minecraftshire.auth.responses.ErrorWithCauseResponse;
import org.minecraftshire.auth.responses.TokenResponse;
import org.minecraftshire.auth.storages.UploadStorage;
import org.minecraftshire.auth.utils.logging.Logger;
import org.minecraftshire.auth.workers.WorkerDoneCallback;
import org.minecraftshire.auth.workers.uploadProcessor.UploadProcessorWorkerPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@CrossOrigin(origins = {"http://localhost:3000", "*"})
@RequestMapping("/character")
public class CharacterController {

    private CharacterRepository characters;
    private UploadStorage uploadStorage;

    @Autowired
    public CharacterController(
            CharacterRepository characters,
            UploadStorage uploadStorage
    ) {
        this.characters = characters;
        this.uploadStorage = uploadStorage;
    }


    @AuthRequired
    @PostMapping("/create")
    public ResponseEntity create(
            @RequestBody CharacterCreationData data,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        data.setOwner(sessionData.getUsername());

        try {
            characters.create(data);

            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (ExceptionWithCause e) {
            return new ResponseEntity<>(
                    new ErrorWithCauseResponse("fail", e.getCausedBy()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    @AuthRequired
    @PostMapping("/set")
    public ResponseEntity set(
            @RequestBody CharacterSetData data,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        try {
            if (data.isFavorite() != null) {
                characters.setFavorite(sessionData.getUsername(), data.getId(), data.isFavorite());
            }

            if (data.isOnline() != null) {
                characters.setOnline(sessionData.getUsername(), data.getId(), data.isOnline());
            }
        } catch (WrongCredentialsException e) {
            return new ResponseEntity<>(
                    new ErrorWithCauseResponse("fail", e.getCausedBy()),
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @AuthRequired
    @PostMapping("/upload_skin")
    public ResponseEntity uploadAvatar(
            @RequestBody UploadSkinData data,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        CharacterData character;

        try {
            character = characters.get(data.getId(), sessionData.getUsername());
        } catch (ExceptionWithCause exceptionWithCause) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        if (!character.getOwner().equals(sessionData.getUsername())) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        String token = String.valueOf(uploadStorage.requestToken(
                CharacterController.onSkinUpload,
                sessionData,
                data.getId()
        ));

        return new ResponseEntity<>(
                new TokenResponse(token),
                HttpStatus.OK
        );
    }


    @AuthRequired
    @PostMapping("/get")
    public ResponseEntity get(
            @RequestBody CharacterCreationData data,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        try {
            return new ResponseEntity<>(
                    characters.get(data.getFirstName(), data.getLastName(), sessionData.getUsername()),
                    HttpStatus.OK
            );
        } catch (ExceptionWithCause e) {
            return new ResponseEntity<>(
                    new ErrorWithCauseResponse("fail", e.getCausedBy()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    @AuthRequired
    @PostMapping("/delete")
    public ResponseEntity delete(
            @RequestBody UploadSkinData data,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        try {
            characters.setDeleted(data.getId(), sessionData.getUsername(), true);

            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (WrongCredentialsException e) {
            return new ResponseEntity<>(
                    new ErrorWithCauseResponse("fail", e.getCausedBy()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    @AuthRequired
    @PostMapping("/restore")
    public ResponseEntity restore(
            @RequestBody UploadSkinData data,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        try {
            characters.setDeleted(data.getId(), sessionData.getUsername(), false);

            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (WrongCredentialsException e) {
            return new ResponseEntity<>(
                    new ErrorWithCauseResponse("fail", e.getCausedBy()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }


    @GetMapping("/{id}/{filename}")
    public ResponseEntity getSkin(
            @PathVariable("id") int id
    ) {
        SkinData skin = characters.getSkin(id);

        if (skin == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(skin.getContentType()));

        return new ResponseEntity<>(
                skin.getData(),
                headers,
                HttpStatus.OK
        );
    }


    private static WorkerDoneCallback<UploadProcessorWorkerPayload> onSkinUpload = (worker, payload) -> {
        try {
            Server.getContext().getBean(CharacterRepository.class).setSkin(
                    (Integer) payload.getInfo().getArg(),
                    payload.getFileInfo().getFile().getBytes(),
                    payload.getFileInfo().getFile().getContentType()
            );
        } catch (IOException e) {
            Logger.getLogger().severe(e);
        }
    };

}
