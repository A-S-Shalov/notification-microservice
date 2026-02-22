package com.example.notification_service.web;

import com.example.notification_service.mail.EmailService;
import com.example.user_service.events.UserOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<Void> send(
            @RequestParam String email,
            @RequestParam UserOperation operation
    ) {

        emailService.send(operation, email);

        return ResponseEntity.ok().build();
    }
}