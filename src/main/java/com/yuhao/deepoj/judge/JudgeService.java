package com.yuhao.deepoj.judge;

import com.yuhao.deepoj.model.entity.Submission;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     * @param submissionId
     * @return
     */
    Submission doJudge(long submissionId);
}

