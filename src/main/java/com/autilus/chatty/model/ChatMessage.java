package com.autilus.chatty.model;

import lombok.Data;

import java.time.Instant;

@Data
public class ChatMessage {

    private String from;
    private String to;
    private String message;
    private Instant date;

    public ChatMessage() {
        this.date = Instant.now();
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}