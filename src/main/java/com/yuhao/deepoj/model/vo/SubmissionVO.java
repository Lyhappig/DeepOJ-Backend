package com.yuhao.deepoj.model.vo;

import cn.hutool.json.JSONUtil;
import com.yuhao.deepoj.judge.codesandbox.model.JudgeInfo;
import com.yuhao.deepoj.model.entity.Submission;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 提交视图
 *
 * @author <a href="https://github.com/Lyhappig">YuhaoLian</a>
 * @from <a href="https://lyhappig.github.io/">Blog</a>
 */
@Data
public class SubmissionVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题目 id
     */
    private Long submissionId;

    /**
     * 对应题目信息
     */
    private ProblemVO problemVO;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 对应用户信息
     */
    private UserVO userVO;

    /**
     * 评测语言（枚举值）
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
     * 判题信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 运行内存
     */
    private Long runMemory;

    /**
     * 运行时间
     */
    private Long runTime;

    /**
     * 提交时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 包装类转对象
     *
     * @param submissionVO
     * @return
     */
    public static Submission voToObj(SubmissionVO submissionVO) {
        if (submissionVO == null) {
            return null;
        }
        Submission submission = new Submission();
        BeanUtils.copyProperties(submissionVO, submission);

        // 转换判题信息对象为字符串
        JudgeInfo judgeInfoVO = submissionVO.getJudgeInfo();
        if (judgeInfoVO != null) {
            submission.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoVO));
        }
        return submission;
    }

    /**
     * 对象转包装类
     *
     * @param submission
     * @return
     */
    public static SubmissionVO objToVo(Submission submission) {
        if (submission == null) {
            return null;
        }
        SubmissionVO submissionVO = new SubmissionVO();
        BeanUtils.copyProperties(submission, submissionVO);

        // 判题信息字符串转化为包装类的成员变量
        String judgeInfoStr = submission.getJudgeInfo();
        submissionVO.setJudgeInfo(JSONUtil.toBean(judgeInfoStr, JudgeInfo.class));
        return submissionVO;
    }

    private static final long serialVersionUID = 1L;
}
