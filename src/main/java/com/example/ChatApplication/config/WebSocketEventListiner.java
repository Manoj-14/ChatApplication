package com.example.ChatApplication.config;

import com.example.ChatApplication.model.ChatMessage;
import com.example.ChatApplication.model.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListiner {

    public final SimpMessageSendingOperations messageTemplate;
    @EventListener
    public void handleWebSocketDisconnectListiner(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String user = (String)headerAccessor.getSessionAttributes().get("user");
        if(user != null){
            log.info("User Disconnected {}",user);
            var chatMessage = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(user)
                    .build();

            messageTemplate.convertAndSend("/topic/public",chatMessage);
        }
    }
}
