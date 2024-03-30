package com.yuhao.deepoj.judge;

import com.yuhao.deepoj.judge.strategy.DefaultJudgeStrategy;
import com.yuhao.deepoj.judge.strategy.JavaJudgeStrategy;
import com.yuhao.deepoj.judge.strategy.JudgeContext;
import com.yuhao.deepoj.judge.strategy.JudgeStrategy;
import com.yuhao.deepoj.judge.codesandbox.model.JudgeInfo;
import com.yuhao.deepoj.model.entity.Submission;
import org.springframework.stereotype.Service;

/**
 * 判题策略管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 获取判题结果
     *
     * @param judgeContext
     * @return
     */
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        String language = judgeContext.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (language.equals("java")) {
            judgeStrategy = new JavaJudgeStrategy();
        }
        return judgeStrategy.doCompare(judgeContext);
    }
}

