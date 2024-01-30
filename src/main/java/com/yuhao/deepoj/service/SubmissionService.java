package com.yuhao.deepoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuhao.deepoj.model.dto.submission.SubmissionQueryRequest;
import com.yuhao.deepoj.model.dto.submission.SubmissionAddRequest;
import com.yuhao.deepoj.model.entity.Submission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuhao.deepoj.model.entity.User;
import com.yuhao.deepoj.model.vo.SubmissionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Yuhao
 * @description 针对表【submission(题目提交)】的数据库操作Service
 * @createDate 2024-01-17 19:04:30
 */
public interface SubmissionService extends IService<Submission> {
    /**
     * 题目提交
     *
     * @param submissionAddRequest 提交信息
     * @param loginUser
     * @return
     */
    long doSubmit(SubmissionAddRequest submissionAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param submissionQueryRequest
     * @return
     */
    QueryWrapper<Submission> getQueryWrapper(SubmissionQueryRequest submissionQueryRequest);

    /**
     * 获取提交记录封装
     *
     * @param problem
     * @param loginUser
     * @return
     */
    SubmissionVO getSubmissionVO(Submission problem, User loginUser);

    /**
     * 分页获取提交记录封装
     *
     * @param problemPage
     * @param loginUser
     * @return
     */
    Page<SubmissionVO> getSubmissionVOPage(Page<Submission> problemPage, User loginUser);
}
