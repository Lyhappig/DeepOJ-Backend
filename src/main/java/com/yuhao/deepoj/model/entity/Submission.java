package com.yuhao.deepoj.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 题目提交
 *
 * @TableName submission
 */
@TableName(value = "submission")
@Data
public class Submission implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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
     * 代码
     */
    private String code;

    /**
     * 判题状态（枚举值）
     */
    private String status;

    /**
     * 判题信息（json对象）
     */
    private String judgeInfo;

    /**
     * 提交时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 运行时间
     */
    private Long runTime;

    /**
     * 运行内存
     */
    private Long runMemory;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}