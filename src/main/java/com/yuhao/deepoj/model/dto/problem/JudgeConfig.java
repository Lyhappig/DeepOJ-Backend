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
    private Integer timeLimit;

    /**
     * 内存限制(MB)
     */
    private Integer memoryLimit;

    /**
     * 堆栈限制(MB)
     */
    private Integer stackLimit;

    /**
     * 代码长度限制(B)
     */
    private Integer codeLengthLimit;
}
