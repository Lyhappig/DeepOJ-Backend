package com.yuhao.deepoj.model.dto.problem;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/Lyhappig">YuhaoLian</a>
 * @from <a href="https://lyhappig.github.io/">Blog</a>
 */
@Data
public class ProblemAddRequest implements Serializable {
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
    private ProblemContent content;

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
     * todo 上传过来的判题用例应该是文件压缩包
     */
    private List<JudgeCase> judgeCase;

    private static final long serialVersionUID = 1L;
}