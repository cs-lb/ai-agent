package com.cslb.aiagent.app;

import com.cslb.aiagent.advisor.BannedWordsAdvisor;
import com.cslb.aiagent.advisor.MyLoggerAdvisor;
import com.cslb.aiagent.chatMemory.MySQLChatMemory;
import com.cslb.aiagent.model.dto.ChatRequest;
import com.cslb.aiagent.model.vo.HistoryChatVO;
import com.cslb.aiagent.service.ChatMemoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

//@Component
@RestController
@Slf4j
@RequestMapping("/chat")
public class LoveApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    @Resource
    private ChatMemoryService chatMemoryService;

    //构造器注入方式来注入dashscopeChatModel，并创建一个ChatClient对象
    public LoveApp(ChatModel dashscopeChatModel,MySQLChatMemory mysqlChatMemory){
        //初始化基于MySQL的对话记忆

        chatClient = ChatClient.builder(dashscopeChatModel) //模型
                .defaultSystem(SYSTEM_PROMPT) //设置系统的promot
                .defaultAdvisors(
                        new BannedWordsAdvisor(),
                        new MessageChatMemoryAdvisor(mysqlChatMemory), //添加一个消息记忆的顾问（拦截器）
                        new MyLoggerAdvisor() //添加自定义的日志拦截器
                )
                .build();
    }

    /**
     * 传入用户输入消息和对话id，即为一轮对话（流式）
     * @param chatRequest
     * @return
     */
    @PostMapping("")
    public Flux<String> doChat(@RequestBody ChatRequest chatRequest){
        String message = chatRequest.getMessage();
        String chatId = chatRequest.getChatId();

        Flux<String> streamResponse = chatClient
                    .prompt()
                    .user(message)
                    .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId) //设置对话的id
                            .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10) //设置对话记忆的大小
                    )
                    .stream()
                    .content();
        return streamResponse;
    }


    @Resource
    private VectorStore loveAppVectorStore; //基于本地知识库

    @Resource
    private Advisor loveAppRagCloudAdvisor; //基于云知识库

    @Resource
    private VectorStore pgVectorVectorStore; //基于PgVector 向量存储


    /**
     * 传入用户输入消息和对话id，即为一轮对话（基于RAG）
     * @param chatRequest
     * @return
     */
    @PostMapping("/rag")
    public Flux<String> doChatWithRag(@RequestBody ChatRequest chatRequest){
        String message = chatRequest.getMessage();
        String chatId = chatRequest.getChatId();

        Flux<String> streamResponse = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId) //设置对话的id
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10) //设置对话记忆的大小
                )
                // 应用 RAG 知识库问答
//                .advisors(new QuestionAnswerAdvisor(loveAppVectorStore))
                // 应用 RAG 检索增强服务（基于云知识库 和 RetrievalAugmentationAdvisor）
//                .advisors(loveAppRagCloudAdvisor)
                // 应用 RAG 检索增强服务（基于PgVector 向量存储）
                .advisors(new QuestionAnswerAdvisor(pgVectorVectorStore))
                .stream()
                .content();
        return streamResponse;
    }

    /**
     * 返回所有历史会话的 chatId 和摘要（首条消息）。
     * @return
     */
    @GetMapping("/history/list")
    public List<HistoryChatVO> getHistoryChatList(){
        return chatMemoryService.getHistoryChatList();
    }

    /**
     * 返回指定 chatId 的历史对话记录。
     * @param chatId
     * @return
     */
    @GetMapping("/history/{chatId}")
    public List<Message> getChatHistory(@PathVariable String chatId){
        List<Message> messages = chatMemoryService.selectMessageById(chatId);
        return messages;
    }






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

    record Report(String title, List<String> suggestions){} //定义一个Report类，用于存储报告（java新特性）


    //输出指定格式的回复
    public Report doChatWithReport(String message, String chatId){
        Report report = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId) //设置对话的id
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10) //设置对话记忆的大小
                )
                .call()
                .entity(Report.class);

        log.info("report: {}",report);
        return report;
    }
}


