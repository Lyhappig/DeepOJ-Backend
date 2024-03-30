package com.yuhao.deepoj.judge;

import cn.hutool.json.JSONUtil;
import com.yuhao.deepoj.common.ErrorCode;
import com.yuhao.deepoj.constant.MqConstant;
import com.yuhao.deepoj.exception.BusinessException;
import com.yuhao.deepoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yuhao.deepoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yuhao.deepoj.judge.codesandbox.model.JudgeInfo;
import com.yuhao.deepoj.judge.codesandbox.service.CodeSandbox;
import com.yuhao.deepoj.judge.codesandbox.service.proxy.CodeSandboxDynamicProxy;
import com.yuhao.deepoj.judge.strategy.JudgeContext;
import com.yuhao.deepoj.model.dto.problem.JudgeCase;
import com.yuhao.deepoj.model.dto.problem.JudgeConfig;
import com.yuhao.deepoj.model.entity.Problem;
import com.yuhao.deepoj.model.entity.Submission;
import com.yuhao.deepoj.model.enums.SubmissionStatusEnum;
import com.yuhao.deepoj.mq.MessageProducer;
import com.yuhao.deepoj.service.ProblemService;
import com.yuhao.deepoj.service.SubmissionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private ProblemService problemService;

    @Resource
    private SubmissionService submissionService;

    @Resource
    private MessageProducer producer;

    @Value("${codesandbox.type:example}")
    private String type;

    @Override
    public void doJudge(long submissionId) {
        // 1）传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        Submission submission = submissionService.getById(submissionId);
        if (submission == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long problemId = submission.getProblemId();
        Problem problem = problemService.getById(problemId);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 2）如果题目提交状态不为等待中，就不用重复执行了
        if (!submission.getStatus().equals(SubmissionStatusEnum.PENDING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "提交已处在待判题队列");
        }
        // 3）将题目提交到消息队列
        String language = submission.getLanguage();
        String code = submission.getCode();
        JudgeConfig judgeConfig = JSONUtil.toBean(problem.getJudgeConfig(), JudgeConfig.class);

        String judgeCaseStr = problem.getJudgeCases();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInputPath).collect(Collectors.toList());
        // todo 将输入路径对应的文件读取到此处
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .submissionId(submissionId)
                .code(code)
                .language(language)
                .inputList(inputList)
                .timeLimit(Long.valueOf(judgeConfig.getTimeLimit()))
                .memoryLimit(Long.valueOf(judgeConfig.getMemoryLimit()))
                .stackLimit(Long.valueOf(judgeConfig.getStackLimit()))
                .build();
        producer.sendMessage(MqConstant.EXCHANGE_NAME, MqConstant.PENDING_KEY, JSONUtil.toJsonStr(executeCodeRequest));
    }
}

