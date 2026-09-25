package com.demo.messenger.repository;

import com.demo.messenger.model.Channel;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ChannelRepository extends MongoRepository<Channel, String> {
    Channel findByInviteCode(String inviteCode);

    // NEW: Find chats where the members list contains the specific user
    List<Channel> findByMembersContaining(String username);
}