package com.autilus.chatty.config;

import com.autilus.chatty.SessionEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {
    @Bean
    public SessionEventListener presenceEventListener() {
        SessionEventListener presence = new SessionEventListener();
        return presence;
    }
}
