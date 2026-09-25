package com.demo.messenger.pattern.observer;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.messenger.model.Message;
import java.io.IOException;

public class UserSession implements ChatObserver {
    private WebSocketSession session;
    private ObjectMapper mapper = new ObjectMapper();

    public UserSession(WebSocketSession session) {
        this.session = session;
    }

    @Override
    public void update(Message message) {
        if (session.isOpen()) {
            try {
                String json = mapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(json));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

//it converts the message to JSON and sends it to the user through WebSocket.