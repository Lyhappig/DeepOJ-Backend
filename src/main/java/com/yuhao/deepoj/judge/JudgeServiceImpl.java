package com.yuhao.deepoj.judge;

import cn.hutool.json.JSONUtil;
import com.yuhao.deepoj.common.ErrorCode;
import com.yuhao.deepoj.exception.BusinessException;
import com.yuhao.deepoj.judge.codesandbox.CodeSandbox;
import com.yuhao.deepoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.yuhao.deepoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yuhao.deepoj.judge.codesandbox.proxy.CodeSandboxDynamicProxy;
import com.yuhao.deepoj.judge.strategy.JudgeContext;
import com.yuhao.deepoj.model.dto.problem.JudgeCase;
import com.yuhao.deepoj.model.dto.submission.JudgeInfo;
import com.yuhao.deepoj.model.entity.Problem;
import com.yuhao.deepoj.model.entity.Submission;
import com.yuhao.deepoj.model.enums.SubmissionStatusEnum;
import com.yuhao.deepoj.service.ProblemService;
import com.yuhao.deepoj.service.SubmissionService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private ProblemService problemService;

    @Resource
    private SubmissionService submissionService;

    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String type;


    @Override
    public Submission doJudge(long submissionId) {
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
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "代码已提交判题机");
        }
        // 3）更改判题（题目提交）的状态为 “编译中”，防止重复执行
        Submission submissionUpdate = new Submission();
        submissionUpdate.setId(submissionId);
        submissionUpdate.setStatus(SubmissionStatusEnum.COMPILING.getValue());
        boolean update = submissionService.updateById(submissionUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交状态更新错误");
        }
        // 4）获取判题相关信息，创建并调用沙箱
        String language = submission.getLanguage();
        String code = submission.getCode();
        String judgeCaseStr = problem.getJudgeCases();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        // todo 将输入路径对应的文件读取到此处
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInputPath).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        CodeSandbox codeSandbox = CodeSandboxDynamicProxy.createCodeSandboxProxy(type);
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();
        // 5）根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = JudgeContext.builder()
                .judgeInfo(executeCodeResponse.getJudgeInfo())
                .inputList(inputList)
                .outputList(outputList)
                .judgeCaseList(judgeCaseList)
                .problem(problem)
                .submission(submission)
                .build();
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 6）修改数据库中的判题结果
        submissionUpdate = new Submission();
        submissionUpdate.setId(submissionId);
        submissionUpdate.setStatus(SubmissionStatusEnum.SUCCEED.getValue());
        submissionUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = submissionService.updateById(submissionUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        return submissionService.getById(problemId);
    }
}

