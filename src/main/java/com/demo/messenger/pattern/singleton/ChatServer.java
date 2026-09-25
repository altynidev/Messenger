package com.demo.messenger.pattern.singleton;

import com.demo.messenger.model.Message;
import com.demo.messenger.pattern.observer.ChatObserver;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatServer {
    private final Map<String, Set<ChatObserver>> channelSubscribers = new ConcurrentHashMap<>();

    public void subscribe(String channelId, ChatObserver observer) {
        channelSubscribers.computeIfAbsent(channelId, k -> ConcurrentHashMap.newKeySet()).add(observer);
        System.out.println("User joined channel: " + channelId);
    }

    public void unsubscribe(String channelId, ChatObserver observer) {
        if (channelSubscribers.containsKey(channelId)) {
            channelSubscribers.get(channelId).remove(observer);
        }
    }

    public void broadcast(String channelId, Message message) {
        Set<ChatObserver> observers = channelSubscribers.get(channelId);

        if (observers != null) {
            System.out.println("Broadcasting to " + observers.size() + " users in channel " + channelId);
            for (ChatObserver observer : observers) {
                observer.update(message);
            }
        } else {
            System.out.println("No one is in channel " + channelId + " to receive message.");
        }
    }
}