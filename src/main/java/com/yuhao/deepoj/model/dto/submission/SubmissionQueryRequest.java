package com.yuhao.deepoj.model.dto.submission;

import com.yuhao.deepoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询提交
 *
 * @author <a href="https://github.com/Lyhappig">YuhaoLian</a>
 * @from <a href="https://lyhappig.github.io/">Blog</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SubmissionQueryRequest extends PageRequest implements Serializable {
    /**
     * 题目 id
     */
    private Long problemId;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 评测语言
     */
    private String language;

    /**
     * 判题状态（枚举值）
     */
    private String status;

    /**
     * 提交时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}