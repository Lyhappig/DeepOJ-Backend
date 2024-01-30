package com.yuhao.deepoj.model.dto.problem;

import lombok.Data;

import java.util.List;

/**
 * 题目内容对象（不会被查询）
 */
@Data
public class ProblemContent {
    /**
     * 题面
     */
    private String content;

    /**
     * 输入描述
     */
    private String inputDescription;

    /**
     * 输出描述
     */
    private String outputDescription;

    /**
     * 测试样例
     */
    List<SampleCase> samples;

    /**
     * 说明/提示
     */
    private String note;
}
