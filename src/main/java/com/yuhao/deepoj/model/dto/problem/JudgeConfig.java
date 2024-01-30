package com.yuhao.deepoj.model.dto.problem;

import lombok.Data;

/**
 * 评测配置
 */
@Data
public class JudgeConfig {

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

    /**
     * 代码长度限制(B)
     */
    private Long codeLengthLimit;
}
