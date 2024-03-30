package com.yuhao.deepoj.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {
    /**
     * 提交 ID
     */
    private Long submissionId;

    /**
     * 用户输出
     */
    private List<String> outputList;

    /**
     * 运行信息（错误/正常）
     */
    private List<String> messageList;

    /**
     * 运行时间
     */
    private List<Long> runTimeList;

    /**
     * 运行内存
     */
    private List<Long> runMemoryList;

    /**
     * 执行状态
     */
    private String status;
}

