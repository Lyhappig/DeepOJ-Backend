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
public class ExecuteCodeRequest {
    /**
     * 提交 ID
     */
    private Long submissionId;

    /**
     * 题目输入
     */
    private List<String> inputList;

    /**
     * 判题代码
     */
    private String code;

    /**
     * 判题语言
     */
    private String language;

    /**
     * 时间限制(ms)
     */
    private Long timeLimit;

    /**
     * 内存限制(MB)
     */
    private Long memoryLimit;

    /**
     * 堆栈限制(MB)
     */
    private Long stackLimit;
}

