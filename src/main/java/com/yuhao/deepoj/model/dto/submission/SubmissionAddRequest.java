package com.yuhao.deepoj.model.dto.submission;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建提交
 *
 * @author <a href="https://github.com/Lyhappig">YuhaoLian</a>
 * @from <a href="https://lyhappig.github.io/">Blog</a>
 */
@Data
public class SubmissionAddRequest implements Serializable {
    /**
     * 题目 id
     */
    private Long problemId;

//    /**
//     * 用户 id
//     */
//    private Long userId;

    /**
     * 评测语言
     */
    private String language;

    /**
     * 代码
     */
    private String code;

    private static final long serialVersionUID = 1L;
}