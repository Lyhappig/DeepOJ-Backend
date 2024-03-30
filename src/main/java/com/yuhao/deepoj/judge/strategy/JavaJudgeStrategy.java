package com.yuhao.deepoj.judge.strategy;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yuhao.deepoj.common.ErrorCode;
import com.yuhao.deepoj.constant.CommonConstant;
import com.yuhao.deepoj.exception.BusinessException;
import com.yuhao.deepoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yuhao.deepoj.model.dto.problem.JudgeCase;
import com.yuhao.deepoj.model.dto.problem.JudgeConfig;
import com.yuhao.deepoj.judge.codesandbox.model.JudgeInfo;
import com.yuhao.deepoj.model.entity.Problem;
import com.yuhao.deepoj.judge.codesandbox.enums.JudgeResultEnum;
import com.yuhao.deepoj.utils.ComputeUtils;

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
        ExecuteCodeResponse executeCodeResponse = judgeContext.getExecuteCodeResponse();
        String status = executeCodeResponse.getStatus();
        if (checkNoOutput(status)) {
            return new JudgeInfo(status, 0, 0, 0);
        }

        List<String> outputList = executeCodeResponse.getOutputList();
        List<String> messageList = executeCodeResponse.getMessageList();
        List<Long> runTimeList = executeCodeResponse.getRunTimeList();
        List<Long> runMemoryList = executeCodeResponse.getRunMemoryList();
        // todo 从文件获取预期输出
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        int n = judgeCaseList.size();
        if (isThrowError(n, executeCodeResponse)) {
            throw new BusinessException(ErrorCode.EXTERNAL_ERROR);
        }

        JudgeConfig judgeConfig = judgeContext.getJudgeConfig();
        JudgeInfo judgeInfo = new JudgeInfo();
        String result = null;
        long timeLimit = judgeConfig.getTimeLimit() * 2;
        long memoryLimit = CommonConstant.MB * judgeConfig.getMemoryLimit() * 2;
        long stackLimit = CommonConstant.MB * judgeConfig.getStackLimit();
        int rightCount = 0;
        long time = 0;
        long memory = 0;
        boolean hasError = false;

        for (int i = 0; i < n; i++) {
            if (StrUtil.isNotEmpty(messageList.get(i))) {
                hasError = true;
                if (runTimeList.get(i) >= timeLimit) {
                    result = JudgeResultEnum.TIME_LIMIT_EXCEEDED.getValue();
                } else if (runMemoryList.get(i) >= memoryLimit) {
                    result = JudgeResultEnum.MEMORY_LIMIT_EXCEEDED.getValue();
                } else {
                    result = JudgeResultEnum.RUNTIME_ERROR.getValue();
                }
            } else if (outputList.get(i).equals(judgeCaseList.get(i).getOutputPath())) {
                rightCount++;
            }
            time = Math.max(time, runTimeList.get(i));
            memory = Math.max(memory, runMemoryList.get(i));
        }

        judgeInfo.setScore(ComputeUtils.getRateScore(rightCount, n));
        judgeInfo.setRunTime(time);
        judgeInfo.setRunMemory(memory);
        if (hasError) {
            judgeInfo.setResult(result);
        } else {
            judgeInfo.setResult(rightCount == n ? JudgeResultEnum.Accepted.getValue() : JudgeResultEnum.WRONG_ANSWER.getValue());
        }
        return judgeInfo;
    }

    public boolean checkNoOutput(String status) {
        if (StrUtil.isBlank(status)) return false;
        return status.equals(JudgeResultEnum.COMPILE_ERROR.getValue()) ||
                status.equals(JudgeResultEnum.CANCELLED.getValue()) ||
                status.equals(JudgeResultEnum.SYSTEM_ERROR.getValue());
    }

    public boolean isThrowError(int n, ExecuteCodeResponse response) {
        List<String> outputList = response.getOutputList();
        List<String> messageList = response.getMessageList();
        List<Long> runTimeList = response.getRunTimeList();
        List<Long> runMemoryList = response.getRunMemoryList();
        if (outputList.size() != n || messageList.size() != n ||
                runTimeList.size() != n || runMemoryList.size() != n) {
            return true;
        }
        return false;
    }
}

