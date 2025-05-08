package com.cslb.aiagent.demo.invoke;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;

public class HttpAiInvoke {
    // 替换为你的 API Key
    private static final String API_KEY = "sk-26e572dff515469fa1ade664a7923a44";

    public static void main(String[] args) {
        // 构建 messages 数组
        JSONArray messages = new JSONArray();
        messages.add(
                JSONUtil.createObj()
                        .put("role", "system")
                        .put("content", "You are a helpful assistant.")
        );
        messages.add(
                JSONUtil.createObj()
                        .put("role", "user")
                        .put("content", "你是谁？")
        );
        // 构建请求体
        String requestBody = JSONUtil.createObj()
                .put("model", "qwen-plus")
                .put("input", JSONUtil.createObj()
                        .put("messages", messages)
                )
                .put("parameters", JSONUtil.createObj()
                        .put("result_format", "message")
                )
                .toString();

        // 发送 POST 请求
        HttpResponse response = HttpRequest.post("https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation")
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .execute();

        // 处理响应结果
        if (response.isOk()) {
            String result = response.body();
            System.out.println("响应结果: " + result);
        } else {
            System.err.println("请求失败，状态码: " + response.getStatus());
        }
    }
}