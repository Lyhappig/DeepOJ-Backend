package com.yuhao.deepoj.model.dto.problem;

import com.yuhao.deepoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/Lyhappig">YuhaoLian</a>
 * @from <a href="https://lyhappig.github.io/">Blog</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProblemQueryRequest extends PageRequest implements Serializable {

    /**
     * id
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
     * todo 有了 Problem-Tag 对应表，不需要在此处查询标签
     */

    /**
     * 比赛 id
     */
    private Long contestId;

    /**
     * 出题人 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}