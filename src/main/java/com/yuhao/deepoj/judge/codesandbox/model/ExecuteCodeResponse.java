package com.yuhao.deepoj.judge.codesandbox.model;

import com.yuhao.deepoj.model.dto.submission.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {
    /**
     * 用户输出
     */
    private List<String> outputList;

    /**
     * 接口信息
     */
    private String message;

    /**
     * 执行状态
     */
    private String status;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;
}

