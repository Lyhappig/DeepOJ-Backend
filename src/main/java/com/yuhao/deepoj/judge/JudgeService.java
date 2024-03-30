package com.yuhao.deepoj.judge;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     *
     * @param submissionId
     * @return
     */
    void doJudge(long submissionId);
}

