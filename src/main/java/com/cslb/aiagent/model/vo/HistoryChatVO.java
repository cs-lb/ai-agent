package com.cslb.aiagent.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryChatVO implements Serializable {
    private static final long serialVersionUID = 1L;

    //对话Id
    private String chatId;

    //摘要信息
    private String summaryMsg;

}
