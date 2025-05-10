package com.cslb.aiagent.service.impl;

import com.cslb.aiagent.mapper.ChatMemoryMapper;
import com.cslb.aiagent.model.entity.ChatInfo;
import com.cslb.aiagent.model.vo.HistoryChatVO;
import com.cslb.aiagent.service.ChatMemoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ChatMemoryServiceImpl implements ChatMemoryService {
    @Autowired
    private ChatMemoryMapper chatMemoryMapper;

    @Autowired
    private ObjectMapper objectMapper;
    public List<Message> selectMessageById(String conversationId) {
        try {
            String jsonMessages = chatMemoryMapper.selectMessageById(conversationId);
            List<Message> totalMessage = new ArrayList<>();
            if (jsonMessages != null && !jsonMessages.isEmpty()) {
                List<Message> messages = objectMapper.readValue(jsonMessages, new TypeReference<>() {});
                totalMessage.addAll(messages);
            }
            return totalMessage;
        } catch (Exception e) {
            log.error("Error adding messages to MySQL chat memory", e);
            throw new RuntimeException(e);
        }
    }

    public void clearMemoryById(String conversationId) {
        chatMemoryMapper.clearMemoryById(conversationId);
    }

    public void updateMessageById(String conversationId, String messages) {
        // Remove newlines and escape single quotes
        messages = messages.replaceAll("[\\r\\n]", "").replace("'", "''");
        String result = chatMemoryMapper.selectMessageById(conversationId);
        if (result == null ||result.isEmpty()) {
            log.info("insert message by mysql");
            chatMemoryMapper.insertMessageById(conversationId, messages);
        } else {
            log.info("update message by mysql");
            chatMemoryMapper.updateMessageById(conversationId, messages);
        }
    }

    /**
     * 返回对话id和对话摘要（首个问题）
     * @return
     */
    @Override
    public List<HistoryChatVO> getHistoryChatList() {
        List<ChatInfo> chatInfoList = chatMemoryMapper.getAll();
        List<HistoryChatVO> historyChatVOList = new ArrayList<>();

        try{
            for(ChatInfo chatInfo : chatInfoList){
                String jsonStrMsg = chatInfo.getMessages();
                List<Message> messages = new ArrayList<>();
                if (jsonStrMsg != null && !jsonStrMsg.isEmpty()) {
                    messages = objectMapper.readValue(jsonStrMsg, new TypeReference<>() {});
                }
                HistoryChatVO historyChatVO = HistoryChatVO.builder()
                        .chatId(chatInfo.getConversationId())
                        .summaryMsg(messages.getFirst().getText())
                        .build();
                historyChatVOList.add(historyChatVO);
            }
        }catch (Exception e){
            log.error("失败得到历史对话消息", e);
            throw new RuntimeException(e);
        }
        return historyChatVOList;
    }

}
