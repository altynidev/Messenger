package com.demo.messenger.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Message {
    private String id;
    private String sender;
    private String content;
    private String timestamp;
}