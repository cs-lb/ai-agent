package com.cslb.aiagent.app;

import cn.hutool.core.lang.UUID;
import com.cslb.aiagent.tools.WeatherTools;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
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

    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
//        testMessage("周末想带女朋友去上海约会，推荐几个适合情侣的小众打卡地？");
//
//        // 测试网页抓取：恋爱案例分析
//        testMessage("最近和对象吵架了，看看编程导航网站（codefather.cn）的其他情侣是怎么解决矛盾的？");
//
//        // 测试资源下载：图片下载
//        testMessage("直接下载一张适合做手机壁纸的星空情侣图片为文件");
//
//        // 测试终端操作：执行代码
//        testMessage("执行 Python3 脚本来生成数据分析报告");

        // 测试文件操作：保存用户档案
//        testMessage("保存我的恋爱档案为文件");

        // 测试 PDF 生成
//        testMessage("生成一份‘七夕约会计划’PDF，包含餐厅预订、活动流程和礼物清单");
    }

    @Test
    public void testMessage() {
        String chatId = UUID.randomUUID().toString();
        String answer = loveApp.doChatWithTools("最近和对象吵架了，看看编程导航网站（codefather.cn）的其他情侣是怎么解决矛盾的？", chatId);
        Assertions.assertNotNull(answer);
    }

}


