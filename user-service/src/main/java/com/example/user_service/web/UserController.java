package com.example.user_service.web;

import com.example.user_service.events.UserEvent;
import com.example.user_service.events.UserEventProducer;
import com.example.user_service.events.UserOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserEventProducer producer;

    public UserController(UserEventProducer producer) {
        this.producer = producer;
    }

    // Создать пользователя (принимаем email в query параметре, чтобы было максимально просто)
    // пример: POST http://localhost:8081/users?email=test@mail.com
    @PostMapping
    public ResponseEntity<Void> create(@RequestParam String email) {
        producer.send(new UserEvent(UserOperation.CREATE, email));
        return ResponseEntity.accepted().build();
    }

    // Удалить пользователя по email
    // пример: DELETE http://localhost:8081/users/test@mail.com
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> delete(@PathVariable String email) {
        producer.send(new UserEvent(UserOperation.DELETE, email));
        return ResponseEntity.accepted().build();
    }
}