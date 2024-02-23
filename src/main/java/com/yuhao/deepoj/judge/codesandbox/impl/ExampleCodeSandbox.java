package com.yuhao.deepoj.judge.codesandbox.impl;

import com.yuhao.deepoj.judge.codesandbox.CodeSandbox;
import com.yuhao.deepoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yuhao.deepoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yuhao.deepoj.model.dto.submission.JudgeInfo;
import com.yuhao.deepoj.model.enums.JudgeResultEnum;
import com.yuhao.deepoj.model.enums.SubmissionStatusEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 示例代码沙箱（仅为了跑通业务流程）
 */
@Slf4j
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(SubmissionStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setResult(JudgeResultEnum.Accepted.getValue());
        judgeInfo.setScore(100);
        judgeInfo.setRunMemory(100L);
        judgeInfo.setRunTime(1000L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}

