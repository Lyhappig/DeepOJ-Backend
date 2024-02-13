package com.yuhao.deepoj.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 题目
 *
 * @TableName problem
 */
@TableName(value = "problem")
@Data
public class Problem implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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
     * 比赛 id
     */
    private Long contestId;

    /**
     * 题目内容（json对象）
     */
    private String problemContent;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题配置（json对象）
     */
    private String judgeConfig;

    /**
     * 判题用例（json数组）
     */
    private String judgeCases;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 出题人 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}