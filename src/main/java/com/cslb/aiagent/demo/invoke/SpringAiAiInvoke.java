package com.cslb.aiagent.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * SpringAI alibaba 框架调用 AI 大模型
 */
//@Component
public class SpringAiAiInvoke implements CommandLineRunner { //实现CommandLineRunner接口，并且类加了@Component，项目启动时会执行自动run方法

    @Resource
    private ChatModel dashscopeChatModel;

    @Override
    public void run(String... args) throws Exception {
        AssistantMessage assistantMessage = dashscopeChatModel.call(new Prompt("你好👋"))
                .getResult()
                .getOutput();
        System.out.println(assistantMessage.getText());
    }
}
