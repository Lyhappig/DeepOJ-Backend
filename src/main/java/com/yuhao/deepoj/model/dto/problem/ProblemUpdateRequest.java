package com.yuhao.deepoj.model.dto.problem;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求（管理员更新，具备最大权限）
 *
 * @author <a href="https://github.com/Lyhappig">YuhaoLian</a>
 * @from <a href="https://lyhappig.github.io/">Blog</a>
 */
@Data
public class ProblemUpdateRequest implements Serializable {

    /**
     * id 不可修改，用于追踪被编辑的题目
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 难度
     */
    private Integer difficulty;

    /**
     * 标签列表
     * todo List<Tag> tags
     */
//    private List<Integer> tags;

    /**
     * 题目内容
     */
    private ProblemContent problemContent;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCases;

    private static final long serialVersionUID = 1L;
}