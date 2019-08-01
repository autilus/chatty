package com.autilus.chatty.model.event;

import lombok.Data;

import java.time.Instant;

@Data
public class LogoutEvent {

    private String username;
    private Instant logoutDate;

    public LogoutEvent(String username) {
        this.username = username;
        this.logoutDate = Instant.now();
    }
}
