package com.demo.messenger.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "channels")
public class Channel {
    @Id
    private String id;
    private String name;
    private String inviteCode;
    private String ownerId;
    private List<String> members = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();
}
//So one “channel” document contains both members and the chat history — this is very simple and convenient for a demo.