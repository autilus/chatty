package com.autilus.chatty.model.event;

import lombok.Data;

import java.time.Instant;

@Data
public class LoginEvent {

    private String username;
    private Instant loginDate;

    public LoginEvent(String username) {
        this.username = username;
        this.loginDate = Instant.now();
    }
}
