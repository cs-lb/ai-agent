package com.cslb.aiagent.mapper;

import com.cslb.aiagent.model.entity.ChatInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ChatMemoryMapper {

    @Select("select messages from chat_memory where conversation_id = #{conversationId} ")
    String selectMessageById(String conversationId);

    @Delete("delete from chat_memory where conversation_id = #{conversationId}")
    void clearMemoryById(String conversationId);

    @Update("update chat_memory set messages = #{messages} where conversation_id =#{conversationId}")
    void updateMessageById(String conversationId, String messages);

    @Insert("insert into chat_memory (conversation_id, messages) values (#{conversationId},#{messages})")
    void insertMessageById(String conversationId, String messages);

    /**
     * 获取所有对话数据（包括信息和id）
     */
    @Select("select * from chat_memory")
    List<ChatInfo> getAll();
}
