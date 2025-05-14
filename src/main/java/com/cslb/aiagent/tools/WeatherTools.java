package com.cslb.aiagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class WeatherTools {
    @Tool(description = "获取指定城市的当前天气情况")
    String getWeather(@ToolParam(description = "城市名称") String city) {
        // 获取天气的实现逻辑
        System.out.println("正在获取天气信息...");
        System.out.println(city);
        return "北京今天晴朗，气温25°C";
    }
}
