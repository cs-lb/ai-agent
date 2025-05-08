package com.cslb.aiagent.app;

import com.cslb.aiagent.advisor.MyLoggerAdvisor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class LoveApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    //构造器注入方式来注入dashscopeChatModel，并创建一个ChatClient对象
    public LoveApp(ChatModel dashscopeChatModel){
        //初始化基于内存的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();

        chatClient = ChatClient.builder(dashscopeChatModel) //模型
                .defaultSystem(SYSTEM_PROMPT) //设置系统的promot
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory), //添加一个消息记忆的顾问（拦截器）
                        new MyLoggerAdvisor() //添加自定义的日志拦截器
                )
                .build();
    }

    //传入用户输入消息和对话id，即为一轮对话（同步、非流式）
    public String doChat(String message, String chatId){
        ChatResponse chatResponse = chatClient
                    .prompt()
                    .user(message)
                    .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId) //设置对话的id
                            .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10) //设置对话记忆的大小
                    )
                    .call()
                    .chatResponse();
        String responseText = chatResponse.getResult().getOutput().getText();
        log.info("responseText: {}",responseText);
        return responseText;
    }

}


