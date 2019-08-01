package com.autilus.chatty.controller;

import com.autilus.chatty.model.ChatMessage;
import com.autilus.chatty.model.event.LoginEvent;
import com.autilus.chatty.repo.ChatRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Collection;

@Controller("/chat")
public class ChatController {
    Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @SubscribeMapping("/chat.participants")
    public Collection<LoginEvent> retrieveParticipants() {
        logger.info("participants" + chatRepo.getAllSessions().values().toString());
        return chatRepo.getAllSessions().values();
    }

    @MessageMapping("/chat.public")
    public ChatMessage publicChat(@Payload ChatMessage chatMessage, Principal principal) {
        chatMessage.setTo("Public");
        chatMessage.setFrom(principal.getName());
        logger.info("public chat"+chatMessage.toString());
        return chatMessage;
    }

    @MessageMapping("/chat.private.{username}")
    public void privateChat(@Payload ChatMessage chatMessage, @DestinationVariable("username") String username, Principal principal) {
        chatMessage.setTo(username);
        chatMessage.setFrom(principal.getName());
        chatMessage.setMessage("[private]" + chatMessage.getMessage());
        simpMessagingTemplate.convertAndSend("/user/" + username + "/exchange/amq.direct/chat.private", chatMessage);
        logger.info("private message SENDED");
    }
}
