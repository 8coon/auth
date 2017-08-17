package org.minecraftshire.auth.controllers;


import org.minecraftshire.auth.aspects.AuthRequired;
import org.minecraftshire.auth.aspects.UserAgent;
import org.minecraftshire.auth.data.AuthTokenData;
import org.minecraftshire.auth.data.IntArrayAuth;
import org.minecraftshire.auth.data.NotificationData;
import org.minecraftshire.auth.data.SessionData;
import org.minecraftshire.auth.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = {"http://localhost:3000", "*"})
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationRepository notifications;


    @AuthRequired
    @PostMapping("/add_test")
    public ResponseEntity addTest(
            @RequestBody NotificationData notification,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        notification.setUsername(sessionData.getUsername());
        notification.setUnread(false);
        notifications.add(notification);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @AuthRequired
    @PostMapping("/mark_read")
    public ResponseEntity markRead(
            @RequestBody IntArrayAuth ids,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        notifications.markRead(sessionData.getUsername(), ids.getValues());
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @AuthRequired
    @PostMapping("/list")
    public ResponseEntity list(
            @RequestBody AuthTokenData authTokenData,
            UserAgent userAgent,
            SessionData sessionData
    ) {
        return new ResponseEntity<List>(
                notifications.get(sessionData.getUsername()),
                HttpStatus.OK
        );
    }

}
