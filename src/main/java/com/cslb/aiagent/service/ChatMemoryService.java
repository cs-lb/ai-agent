package com.cslb.aiagent.service;


import com.cslb.aiagent.model.vo.HistoryChatVO;
import org.springframework.ai.chat.messages.Message;

import java.util.List;

public interface ChatMemoryService {
    List<Message> selectMessageById(String conversationId);

    void clearMemoryById(String conversationId);

    void updateMessageById(String conversationId, String messages);

    List<HistoryChatVO> getHistoryChatList();
}
