package com.yuhao.deepoj.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhao.deepoj.common.ErrorCode;
import com.yuhao.deepoj.constant.CommonConstant;
import com.yuhao.deepoj.constant.MqConstant;
import com.yuhao.deepoj.exception.BusinessException;
import com.yuhao.deepoj.judge.JudgeService;
import com.yuhao.deepoj.judge.codesandbox.model.JudgeInfo;
import com.yuhao.deepoj.mapper.SubmissionMapper;
import com.yuhao.deepoj.model.dto.submission.SubmissionAddRequest;
import com.yuhao.deepoj.model.dto.submission.SubmissionQueryRequest;
import com.yuhao.deepoj.model.entity.Problem;
import com.yuhao.deepoj.model.entity.Submission;
import com.yuhao.deepoj.model.entity.User;
import com.yuhao.deepoj.model.enums.SubmissionLanguageEnum;
import com.yuhao.deepoj.model.enums.SubmissionStatusEnum;
import com.yuhao.deepoj.model.vo.SubmissionVO;
import com.yuhao.deepoj.mq.MessageProducer;
import com.yuhao.deepoj.service.ProblemService;
import com.yuhao.deepoj.service.SubmissionService;
import com.yuhao.deepoj.service.UserService;
import com.yuhao.deepoj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Yuhao
 * @description 针对表【submission(题目提交)】的数据库操作Service实现
 * @createDate 2024-01-17 19:04:30
 */
@Service
public class SubmissionServiceImpl extends ServiceImpl<SubmissionMapper, Submission>
        implements SubmissionService {
    @Resource
    private ProblemService problemService;

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private JudgeService judgeService;

    /**
     * 提交题目
     *
     * @param submissionAddRequest
     * @param loginUser
     * @return 提交记录的 ID
     */
    @Override
    public long doSubmit(SubmissionAddRequest submissionAddRequest, User loginUser) {
        String language = submissionAddRequest.getLanguage();
        if (SubmissionLanguageEnum.getEnumByValue(language) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言不存在");
        }
        long problemId = submissionAddRequest.getProblemId();
        // 判断实体是否存在，根据类别获取实体
        Problem problem = problemService.getById(problemId);
        if (problem == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 每个用户串行提交题目
        Submission submission = new Submission();
        submission.setUserId(loginUser.getId());
        submission.setProblemId(problemId);
        submission.setCode(submissionAddRequest.getCode());
        submission.setLanguage(submissionAddRequest.getLanguage());
        // 提交后的初始状态：处在判题队列
        submission.setStatus(SubmissionStatusEnum.PENDING.getValue());
        // todo 禁止连续提交
        boolean save = this.save(submission);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目提交插入数据库失败");
        }
        Long submissionId = submission.getId();
        // 执行判题服务
        CompletableFuture.runAsync(() -> judgeService.doJudge(submissionId));
        return submissionId;
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到mybatis框架支持的QueryWrapper类）
     *
     * @param submissionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Submission> getQueryWrapper(SubmissionQueryRequest submissionQueryRequest) {
        QueryWrapper<Submission> queryWrapper = new QueryWrapper<>();
        if (submissionQueryRequest == null) {
            return queryWrapper;
        }

        Long problemId = submissionQueryRequest.getProblemId();
        Long userId = submissionQueryRequest.getUserId();
        String language = submissionQueryRequest.getLanguage();
        String status = submissionQueryRequest.getStatus();
        String sortField = submissionQueryRequest.getSortField();
        String sortOrder = submissionQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.eq(ObjectUtils.isNotEmpty(problemId), "problemId", problemId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(SubmissionStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public SubmissionVO getSubmissionVO(Submission submission, User loginUser) {
        SubmissionVO submissionVO = SubmissionVO.objToVo(submission);
        // 脱敏：提交 userId 和登录 userId 不同，不可查看
        Long userId = loginUser.getId();
        if (!userId.equals(submission.getUserId()) && !userService.isAdmin(loginUser)) {
            submissionVO.setCode(null);
        }
        return submissionVO;
    }

    @Override
    public Page<SubmissionVO> getSubmissionVOPage(Page<Submission> submissionPage, User loginUser) {
        List<Submission> submissionList = submissionPage.getRecords();
        Page<SubmissionVO> submissionVOPage = new Page<>(submissionPage.getCurrent(), submissionPage.getSize(), submissionPage.getTotal());
        if (CollUtil.isEmpty(submissionList)) {
            return submissionVOPage;
        }
        List<SubmissionVO> submissionVOList = submissionList.stream()
                .map(submission -> getSubmissionVO(submission, loginUser)).collect(Collectors.toList());
        submissionVOPage.setRecords(submissionVOList);
        return submissionVOPage;
    }
}




