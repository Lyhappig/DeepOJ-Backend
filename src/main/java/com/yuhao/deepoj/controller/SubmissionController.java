package com.yuhao.deepoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuhao.deepoj.common.BaseResponse;
import com.yuhao.deepoj.common.ErrorCode;
import com.yuhao.deepoj.common.ResultUtils;
import com.yuhao.deepoj.exception.BusinessException;
import com.yuhao.deepoj.model.dto.submission.SubmissionAddRequest;
import com.yuhao.deepoj.model.dto.submission.SubmissionQueryRequest;
import com.yuhao.deepoj.model.entity.Submission;
import com.yuhao.deepoj.model.entity.User;
import com.yuhao.deepoj.model.vo.SubmissionVO;
import com.yuhao.deepoj.service.SubmissionService;
import com.yuhao.deepoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 提交接口
 *
 * @author <a href="https://github.com/Lyhappig">YuhaoLian</a>
 * @from <a href="https://lyhappig.github.io/">Blog</a>
 */
@RestController
@RequestMapping("/submit")
@Slf4j
public class SubmissionController {

    @Resource
    private SubmissionService submissionService;

    @Resource
    private UserService userService;

    /**
     * 题目提交
     *
     * @param submissionAddRequest
     * @param request
     * @return resultNum 提交记录的 ID
     */
    @PostMapping("/")
    public BaseResponse<Long> doSubmit(@RequestBody SubmissionAddRequest submissionAddRequest,
                                       HttpServletRequest request) {
        if (submissionAddRequest == null || submissionAddRequest.getProblemId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交
        final User loginUser = userService.getLoginUser(request);
        long submissionId = submissionService.doSubmit(submissionAddRequest, loginUser);
        return ResultUtils.success(submissionId);
    }

    /**
     * 分页获取提交列表（封装类）
     * 根据用户权限的不同，对提交代码进行脱敏
     *
     * @param submissionQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<SubmissionVO>> listSubmissionVOByPage(@RequestBody SubmissionQueryRequest submissionQueryRequest,
                                                                   HttpServletRequest request) {
        long current = submissionQueryRequest.getCurrent();
        long size = submissionQueryRequest.getPageSize();
        Page<Submission> submissionPage = submissionService.page(new Page<>(current, size),
                submissionService.getQueryWrapper(submissionQueryRequest));
        final User loginUser = userService.getLoginUser(request);
        // 返回脱敏信息
        return ResultUtils.success(submissionService.getSubmissionVOPage(submissionPage, loginUser));
    }
}
