package com.demo.messenger.socket;

import com.demo.messenger.model.Channel;
import com.demo.messenger.model.Message;
import com.demo.messenger.pattern.factory.EntityFactory;
import com.demo.messenger.pattern.observer.UserSession;
import com.demo.messenger.pattern.singleton.ChatServer;
import com.demo.messenger.repository.ChannelRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Optional;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired private ChatServer chatServer;
    @Autowired private ChannelRepository channelRepository;
    @Autowired private EntityFactory entityFactory;

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("User Connected: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("User Disconnected: " + session.getId());
        // In a real app, we would remove the user from ChatServer here
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            JsonNode node = mapper.readTree(payload);

            String type = node.get("type").asText();
            String channelId = node.get("channelId").asText();

            if ("JOIN".equals(type)) {
                // Connect user to the channel
                chatServer.subscribe(channelId, new UserSession(session));
            }
            else if ("SEND".equals(type)) {
                String sender = node.get("sender").asText();
                String content = node.get("content").asText();

                // 1. Create Message
                Message msg = entityFactory.createMessage(sender, content);

                // 2. Save to DB
                Optional<Channel> channelOpt = channelRepository.findById(channelId);
                if (channelOpt.isPresent()) {
                    Channel channel = channelOpt.get();
                    channel.getMessages().add(msg);
                    channelRepository.save(channel); // SAVE UPDATE

                    // 3. Send to everyone
                    chatServer.broadcast(channelId, msg);
                } else {
                    System.err.println("Channel not found: " + channelId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}