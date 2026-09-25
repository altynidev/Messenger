package com.demo.messenger.pattern.observer;

import com.demo.messenger.model.Message;

public interface ChatObserver {
    void update(Message message);
}