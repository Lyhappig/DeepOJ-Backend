package com.yuhao.deepoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.yuhao.deepoj.constant.CommonConstant;
import com.yuhao.deepoj.model.dto.problem.JudgeCase;
import com.yuhao.deepoj.model.dto.problem.JudgeConfig;
import com.yuhao.deepoj.judge.codesandbox.model.JudgeInfo;
import com.yuhao.deepoj.model.entity.Problem;
import com.yuhao.deepoj.judge.codesandbox.enums.JudgeResultEnum;

import java.util.List;

/**
 * Java 程序的判题策略
 */
public class JavaJudgeStrategy implements JudgeStrategy {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doCompare(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = judgeInfo.getRunMemory();
        Long time = judgeInfo.getRunTime();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Problem problem = judgeContext.getProblem();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        JudgeResultEnum judgeInfoMessageEnum = JudgeResultEnum.Accepted;
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setRunMemory(memory);
        judgeInfoResponse.setRunTime(time);
        // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
        // todo 从文件获取预期输出
        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = JudgeResultEnum.WRONG_ANSWER;
            judgeInfoResponse.setResult(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        // 依次判断每一项输出和预期输出是否相等
        // todo 从文件获取预期输出
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutputPath().equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeResultEnum.WRONG_ANSWER;
                judgeInfoResponse.setResult(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        // 判断题目是否超出时间、空间限制
        String judgeConfigStr = problem.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        // Java、Python等其他语言，时间、空间限制都是C/C++时限的两倍
        Long needMemoryLimit = 2L * CommonConstant.MB * judgeConfig.getMemoryLimit();
        Long needTimeLimit = 2L * judgeConfig.getTimeLimit();
        if (memory > needMemoryLimit) {
            judgeInfoMessageEnum = JudgeResultEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setResult(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        if (time > needTimeLimit) {
            judgeInfoMessageEnum = JudgeResultEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setResult(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setResult(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }
}

