package com.yuhao.deepoj.model.vo;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.yuhao.deepoj.model.dto.problem.JudgeCase;
import com.yuhao.deepoj.model.dto.problem.JudgeConfig;
import com.yuhao.deepoj.model.dto.problem.ProblemContent;
import com.yuhao.deepoj.model.entity.Problem;
import com.yuhao.deepoj.utils.StringCheckJSONUtils;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目视图（用户视角）
 *
 * @author <a href="https://github.com/Lyhappig">YuhaoLian</a>
 * @from <a href="https://lyhappig.github.io/">Blog</a>
 */
@Data
public class ProblemVO implements Serializable {

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
     * 标签
     * todo 转换为 List<Tag>
     */
//    private List<String> tags;

    /**
     * 比赛 id
     */
    private Long contestId;

    /**
     * 题目内容
     */
    private ProblemContent problemContent;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCases;

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
     * 出题人信息
     */
    UserVO userVO;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 包装类转对象
     *
     * @param problemVO
     * @return
     */
    public static Problem voToObj(ProblemVO problemVO) {
        if (problemVO == null) {
            return null;
        }
        Problem problem = new Problem();
        BeanUtils.copyProperties(problemVO, problem);
        // todo 标签更新到标签对象中

        // 转换题目内容对象为字符串
        ProblemContent problemContentVO = problemVO.getProblemContent();
        if (problemContentVO != null) {
            problem.setProblemContent(JSONUtil.toJsonStr(problemContentVO));
        }
        // 转换判题配置对象为字符串
        JudgeConfig judgeConfigVO = problemVO.getJudgeConfig();
        if (judgeConfigVO != null) {
            problem.setJudgeConfig(JSONUtil.toJsonStr(judgeConfigVO));
        }
        // 转换判题用例对象为字符串
        // todo 获取测试用例的文件对象
        List<JudgeCase> judgeCasesVO = problemVO.getJudgeCases();
        if (judgeCasesVO != null) {
            problem.setJudgeCases(JSONUtil.toJsonStr(judgeCasesVO));
        }
        return problem;
    }

    /**
     * 对象转包装类
     *
     * @param problem
     * @return
     */
    public static ProblemVO objToVo(Problem problem) {
        if (problem == null) {
            return null;
        }
        ProblemVO problemVO = new ProblemVO();
        BeanUtils.copyProperties(problem, problemVO);
        // todo 从题目对应的标签表中，取出标签并转换到“题目视图对象”
        // 题目内容字符串转化为包装类的成员变量
        String problemContentStr = problem.getProblemContent();
        if (StringCheckJSONUtils.isJSONString(problemContentStr)) {
            problemVO.setProblemContent(JSONUtil.toBean(problemContentStr, ProblemContent.class));
        }
        // 判题配置字符串转化为包装类的成员变量
        String judgeConfigStr = problem.getJudgeConfig();
        if (StringCheckJSONUtils.isJSONString(judgeConfigStr)) {
            problemVO.setJudgeConfig(JSONUtil.toBean(judgeConfigStr, JudgeConfig.class));
        }
        // 判题用例字符串转化为包装类的成员变量
        String judgeCasesStr = problem.getJudgeCases();
        if (StringCheckJSONUtils.isJsonArrayString(judgeCasesStr)) {
            problemVO.setJudgeCases(JSONObject.parseArray(judgeCasesStr, JudgeCase.class));
        }
        return problemVO;
    }

    private static final long serialVersionUID = 1L;
}
