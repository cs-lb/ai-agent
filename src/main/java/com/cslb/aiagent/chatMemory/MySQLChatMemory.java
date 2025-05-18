package com.cslb.aiagent.chatMemory;

import com.cslb.aiagent.service.ChatMemoryService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class MySQLChatMemory implements ChatMemory{
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChatMemoryService chatMemoryService;

    public void add(String conversationId, List<Message> messages) {
        log.info("Adding messages to MySQL chat memory：{}",messages);
        try {
            List<Message> all = chatMemoryService.selectMessageById(conversationId);
            all.addAll(messages);
            chatMemoryService.updateMessageById(conversationId, objectMapper.writeValueAsString(all));
        } catch (Exception var4) {
            log.error("Error adding messages to MySQL chat memory", var4);
            throw new RuntimeException(var4);
        }
    }

    public List<Message> get(String conversationId, int lastN) {
        List all;
        try {
            all = chatMemoryService.selectMessageById(conversationId);
        } catch (Exception var5) {
            log.error("Error getting messages from MySQL chat memory", var5);
            throw new RuntimeException(var5);
        }

        return all != null ? all.stream().skip((long) Math.max(0, all.size() - lastN)).toList() : List.of();
    }

    public void clear(String conversationId) {
        chatMemoryService.clearMemoryById(conversationId);
    }

}