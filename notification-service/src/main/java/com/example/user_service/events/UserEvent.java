package com.example.user_service.events;

public record UserEvent(UserOperation operation, String email) {}