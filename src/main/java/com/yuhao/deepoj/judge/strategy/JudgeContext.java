package com.yuhao.deepoj.judge.strategy;

import com.yuhao.deepoj.model.dto.problem.JudgeCase;
import com.yuhao.deepoj.judge.codesandbox.model.JudgeInfo;
import com.yuhao.deepoj.model.entity.Problem;
import com.yuhao.deepoj.model.entity.Submission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Problem problem;

    private Submission submission;
}

