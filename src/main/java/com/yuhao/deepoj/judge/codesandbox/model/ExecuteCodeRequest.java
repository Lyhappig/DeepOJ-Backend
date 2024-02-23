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
}

