package com.cslb.aiagent.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data // 提供所有属性的getter和setter方法
@Builder // 提供构建器模式的支持
@NoArgsConstructor // 提供无参构造函数
@AllArgsConstructor // 提供全参构造函数
public class ChatInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id; // 对应数据库中的id字段

    private String conversationId; // 注意这里conversation_id在数据库中为varbinary类型，但在Java中我们通常用String处理

    private String messages; // 文本类型的messages字段

    // 如果需要从byte[]转换成String或者反过来，可以考虑在这里添加转换逻辑。
}