package com.yuhao.deepoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuhao.deepoj.model.dto.problem.ProblemQueryRequest;
import com.yuhao.deepoj.model.entity.Problem;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuhao.deepoj.model.entity.User;
import com.yuhao.deepoj.model.vo.ProblemVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Yuhao
 * @description 针对表【problem(题目)】的数据库操作Service
 * @createDate 2024-01-17 19:03:49
 */
public interface ProblemService extends IService<Problem> {
    /**
     * 校验题目
     *
     * @param problem
     * @param add
     */
    void validProblem(Problem problem, boolean add);

    /**
     * 获取查询条件
     *
     * @param problemQueryRequest
     * @return
     */
    QueryWrapper<Problem> getQueryWrapper(ProblemQueryRequest problemQueryRequest);

    /**
     * 获取题目封装
     *
     * @param problem
     * @param loginUser
     * @return
     */
    ProblemVO getProblemVO(Problem problem, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param problemPage
     * @param loginUser
     * @return
     */
    Page<ProblemVO> getProblemVOPage(Page<Problem> problemPage, User loginUser);
}
