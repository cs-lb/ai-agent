package com.cslb.aiagent.advisor;

import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BannedWordsAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

    // 定义违禁词列表
    private static final List<String> BANNED_WORDS = Arrays.asList(
            "暴力", "色情", "赌博", "毒品", "诈骗",
            "反动", "政治敏感词", "其他违禁词"
    );

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        String content = advisedRequest.userText();
        for(String word : BANNED_WORDS){
            if(content.contains(word)){
                //构造一个ChatResponse对象
               ChatResponse chatResponse = new ChatResponse(
                       Collections.singletonList(
                               new Generation(new AssistantMessage("您的问题包含违禁内容，请重新提问"))
                       )
               );

               return AdvisedResponse.builder()
                       .response(chatResponse)
                       .adviseContext(Collections.emptyMap())
                       .build();
            }
        }
        return chain.nextAroundCall(advisedRequest);
    }

    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        String content = advisedRequest.userText();
        for(String word : BANNED_WORDS){
            if(content.contains(word)){
                //构造一个ChatResponse对象
                ChatResponse chatResponse = new ChatResponse(
                        Collections.singletonList(
                                new Generation(new AssistantMessage("您的问题包含违禁内容，请重新提问"))
                        )
                );

                return Flux.just(
                        AdvisedResponse.builder()
                                .response(chatResponse)
                                .adviseContext(Collections.emptyMap())
                                .build());
            }
        }
        return chain.nextAroundStream(advisedRequest);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return -10000;
    }
}
