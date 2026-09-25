package com.demo.messenger.controller;

import com.demo.messenger.model.Channel;
import com.demo.messenger.pattern.factory.EntityFactory;
import com.demo.messenger.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/channels")
@CrossOrigin
public class ChannelController {

    @Autowired private ChannelRepository channelRepository;
    @Autowired private EntityFactory entityFactory;

    @GetMapping
    public List<Channel> getChannels(@RequestParam String user) {
        return channelRepository.findByMembersContaining(user);
    }

    @GetMapping("/{id}")
    public Channel getChannel(@PathVariable String id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Channel not found"));
    }

    @PostMapping("/create")
    public Channel create(@RequestBody Map<String, String> payload) {
        String owner = payload.get("ownerId");
        Channel ch = entityFactory.createChannel(payload.get("name"), owner);

        ch.getMembers().add(owner);

        return channelRepository.save(ch);
    }

    @PostMapping("/join")
    public Channel join(@RequestBody Map<String, String> payload) {
        String code = payload.get("inviteCode");
        String username = payload.get("username");

        Channel ch = channelRepository.findByInviteCode(code);
        if (ch != null) {
            if (!ch.getMembers().contains(username)) {
                ch.getMembers().add(username);
                return channelRepository.save(ch);
            }
        }
        return ch;
    }

    @DeleteMapping("/{id}")
    public void deleteChannel(@PathVariable String id) {
        channelRepository.deleteById(id);
    }
}