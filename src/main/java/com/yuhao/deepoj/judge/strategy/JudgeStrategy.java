package com.yuhao.deepoj.judge.strategy;

import com.yuhao.deepoj.model.dto.submission.JudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doCompare(JudgeContext judgeContext);
}

