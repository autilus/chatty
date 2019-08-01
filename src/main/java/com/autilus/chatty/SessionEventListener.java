package com.autilus.chatty;

import com.autilus.chatty.model.event.LoginEvent;
import com.autilus.chatty.model.event.LogoutEvent;
import com.autilus.chatty.repo.ChatRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public class SessionEventListener {
    Logger logger = LoggerFactory.getLogger(SessionEventListener.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatRepo chatRepo;

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();
        logger.info(username + " just connected");
        String sessionId = headers.getSessionId();
        LoginEvent loginEvent = new LoginEvent(headers.getUser().getName());
        chatRepo.addSession(sessionId, loginEvent);
        messagingTemplate.convertAndSend("/topic/chat.login", loginEvent);
    }

    @EventListener
    private void handleSessionConnected(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String username = headers.getUser().getName();
        logger.info(username + " just DISconnected");
        String sessionId = headers.getSessionId();
        chatRepo.removeParticipant(sessionId);
        LogoutEvent logoutEvent = new LogoutEvent(username);
        messagingTemplate.convertAndSend("/topic/chat.logout", logoutEvent);
    }


    @EventListener
    private void brokerAvailabilityEvent(BrokerAvailabilityEvent event) {
        if(event.isBrokerAvailable()) {
            logger.info("Broker is available");
        } else {
            logger.error("Broker is NOT available");
        }
    }

}
