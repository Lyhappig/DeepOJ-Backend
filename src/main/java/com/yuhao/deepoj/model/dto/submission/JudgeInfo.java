package com.yuhao.deepoj.model.dto.submission;

import lombok.Data;

@Data
public class JudgeInfo {
    /**
     * 评测结果枚举值
     */
    private Integer result;

    /**
     * 评测得分
     */
    private Integer score;

    /**
     * 运行内存
     */
    private Long runMemory;

    /**
     * 运行时间
     */
    private Integer runTime;
}
