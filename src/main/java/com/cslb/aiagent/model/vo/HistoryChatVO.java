package com.cslb.aiagent.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryChatVO {
    //对话Id
    private String chatId;

    //摘要信息
    private String summaryMsg;

}
