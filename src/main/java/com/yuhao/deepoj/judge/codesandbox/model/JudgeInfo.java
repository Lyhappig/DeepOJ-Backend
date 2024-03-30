package com.yuhao.deepoj.judge.codesandbox.model;

import lombok.Data;

/**
 * 评测信息（结果）
 */
@Data
public class JudgeInfo {
    /**
     * 评测结果枚举值
     */
    private String result;

    /**
     * 评测得分
     */
    private Integer score;

    /**
     * 运行时间
     */
    private Long runTime;

    /**
     * 运行内存
     */
    private Long runMemory;

    public JudgeInfo() {}

    public JudgeInfo(String result, int score, long time, long memory) {
        this.result = result;
        this.score = score;
        this.runTime = time;
        this.runMemory = memory;
    }
}
