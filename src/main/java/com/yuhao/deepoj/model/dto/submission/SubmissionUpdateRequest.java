package com.yuhao.deepoj.model.dto.submission;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新提交（仅管理员可用，Rejudge）
 * todo
 *
 * @author <a href="https://github.com/Lyhappig">YuhaoLian</a>
 * @from <a href="https://lyhappig.github.io/">Blog</a>
 */
@Data
public class SubmissionUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}