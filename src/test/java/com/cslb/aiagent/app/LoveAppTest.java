package com.cslb.aiagent.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoveAppTest {
    @Resource
    private LoveApp loveApp;

    @Test
    void testChat(){
        String chatId = UUID.randomUUID().toString();

        //第一轮对话
//        String answer = loveApp.doChat("我是暴力狂",chatId);
//        Assertions.assertNotNull(answer); //断言NotNull

//        //第二轮对话
//        answer = loveApp.doChat("我如何能让我的另一半更爱我",chatId);
//        Assertions.assertNotNull(answer);
//
//        //第三轮对话
//        answer = loveApp.doChat("你还记得我是谁嘛",chatId);
//        Assertions.assertNotNull(answer);

    }



    @Test
    void testChatWithReport(){
        String chatId = UUID.randomUUID().toString();
        LoveApp.Report report = loveApp.doChatWithReport("你好，我是cslb，我想让另一半更爱我，但我不知道该怎么做", chatId);
        Assertions.assertNotNull(report);
    }
}


