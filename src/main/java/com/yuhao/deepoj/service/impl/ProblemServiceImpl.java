package com.yuhao.deepoj.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuhao.deepoj.common.ErrorCode;
import com.yuhao.deepoj.constant.CommonConstant;
import com.yuhao.deepoj.constant.ProblemConstant;
import com.yuhao.deepoj.exception.BusinessException;
import com.yuhao.deepoj.exception.ThrowUtils;
import com.yuhao.deepoj.mapper.ProblemMapper;
import com.yuhao.deepoj.model.dto.problem.JudgeConfig;
import com.yuhao.deepoj.model.dto.problem.ProblemQueryRequest;
import com.yuhao.deepoj.model.entity.Problem;
import com.yuhao.deepoj.model.entity.User;
import com.yuhao.deepoj.model.vo.ProblemVO;
import com.yuhao.deepoj.model.vo.UserVO;
import com.yuhao.deepoj.service.ProblemService;
import com.yuhao.deepoj.service.UserService;
import com.yuhao.deepoj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Yuhao
 * @description 针对表【problem(题目)】的数据库操作Service实现
 * @createDate 2024-01-17 19:03:49
 */
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem>
        implements ProblemService {
    @Resource
    private UserService userService;

    /**
     * 校验题目是否合法
     *
     * @param problem
     * @param add
     */
    @Override
    public void validProblem(Problem problem, boolean add) {
        if (problem == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String title = problem.getTitle();
        String problemContent = problem.getProblemContent();
        String judgeConfigStr = problem.getJudgeConfig();
        String judgeCasesStr = problem.getJudgeCases();
        String answer = problem.getAnswer();

        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title, problemContent, answer, judgeConfigStr), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(problemContent) && problemContent.length() > CommonConstant.KB * 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "题目内容过长");
        }
        // todo 检验单个用例长度不超过某个阈值（50 MB）
        if (StringUtils.isNotBlank(judgeCasesStr) && judgeCasesStr.length() > CommonConstant.MB * 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "判题用例过长");
        }
        JudgeConfig judgeConfig = JSONUtil.toBean(problem.getJudgeConfig(), JudgeConfig.class);
        // todo 检测答案的长度不超过judgeConfig的提供值
        if (StringUtils.isNotBlank(answer) && answer.length() > judgeConfig.getCodeLengthLimit() * CommonConstant.KB) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "答案代码过长");
        }
    }

    /**
     * 获取查询包装类（用户根据哪些字段查询，根据前端传来的请求对象，得到mybatis框架支持的QueryWrapper类）
     *
     * @param problemQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Problem> getQueryWrapper(ProblemQueryRequest problemQueryRequest) {
        QueryWrapper<Problem> queryWrapper = new QueryWrapper<>();
        if (problemQueryRequest == null) {
            return queryWrapper;
        }

        Long id = problemQueryRequest.getId();
        String title = problemQueryRequest.getTitle();
        Integer minDifficulty = problemQueryRequest.getMinDifficulty();
        Integer maxDifficulty = problemQueryRequest.getMaxDifficulty();
        if (ObjectUtils.isEmpty(minDifficulty) || minDifficulty < ProblemConstant.minDifficulty) {
            minDifficulty = ProblemConstant.minDifficulty;
        }
        if (ObjectUtils.isEmpty(maxDifficulty) || maxDifficulty > ProblemConstant.maxDifficulty) {
            maxDifficulty = ProblemConstant.maxDifficulty;
        }
        Long contestId = problemQueryRequest.getContestId();
        Long userId = problemQueryRequest.getUserId();
        String sortField = problemQueryRequest.getSortField();
        String sortOrder = problemQueryRequest.getSortOrder();

        // 拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(title), "title", title);
        // todo 根据标签查询题目
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(contestId), "contestId", contestId);
        queryWrapper.between("difficulty", minDifficulty, maxDifficulty);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public ProblemVO getProblemVO(Problem problem, User loginUser) {
        ProblemVO problemVO = ProblemVO.objToVo(problem);
        Long userId = problem.getUserId();
        if (!userId.equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            problemVO.setAnswer(null);
            problemVO.setJudgeCases(null);
        }
        // 关联出题人信息
        UserVO userVO = userService.getUserVO(userService.getById(userId));
        problemVO.setUserVO(userVO);
        return problemVO;
    }

    @Override
    public Page<ProblemVO> getProblemVOPage(Page<Problem> problemPage, User loginUser) {
        List<Problem> problemList = problemPage.getRecords();
        Page<ProblemVO> problemVOPage = new Page<>(problemPage.getCurrent(), problemPage.getSize(), problemPage.getTotal());
        if (CollUtil.isEmpty(problemList)) {
            return problemVOPage;
        }
        // 提取题目页中包含的所有出题人用户，通过 map 根据 userId 映射到 User
        Set<Long> userIdSet = problemList.stream().map(Problem::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充对应出题人视图对象到题目页中的每个题目
        List<ProblemVO> problemVOList = problemList.stream().map(problem -> {
            ProblemVO problemVO = ProblemVO.objToVo(problem);
            Long userId = problem.getUserId();
            if (!userId.equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
                problemVO.setAnswer(null);
                problemVO.setJudgeCases(null);
            }
            // 关联出题人信息
            User user = userIdUserListMap.get(userId).get(0);
            problemVO.setUserVO(userService.getUserVO(user));
            return problemVO;
        }).collect(Collectors.toList());
        problemVOPage.setRecords(problemVOList);
        return problemVOPage;
    }
}




