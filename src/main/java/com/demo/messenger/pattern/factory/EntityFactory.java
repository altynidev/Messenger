package com.demo.messenger.pattern.factory;

import com.demo.messenger.model.Channel;
import com.demo.messenger.model.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class EntityFactory {

    public Channel createChannel(String name, String ownerId) {
        Channel channel = new Channel();
        channel.setName(name);
        channel.setOwnerId(ownerId);
        channel.setInviteCode(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        return channel;
    }

    public Message createMessage(String sender, String content) {
        Message message = new Message();
        message.setId(UUID.randomUUID().toString());
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now().toString());
        return message;
    }
}