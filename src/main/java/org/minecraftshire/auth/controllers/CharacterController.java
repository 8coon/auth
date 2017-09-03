package org.minecraftshire.auth.controllers;

import org.minecraftshire.auth.aspects.AuthRequired;
import org.minecraftshire.auth.aspects.UserAgent;
import org.minecraftshire.auth.data.character.CharacterCreationData;
import org.minecraftshire.auth.data.character.CharacterSetData;
import org.minecraftshire.auth.data.session.SessionData;
import org.minecraftshire.auth.exceptions.ExceptionWithCause;
import org.minecraftshire.auth.exceptions.WrongCredentialsException;
import org.minecraftshire.auth.repositories.CharacterRepository;
import org.minecraftshire.auth.responses.ErrorWithCauseResponse;
import org.minecraftshire.auth.storages.UploadStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

}
