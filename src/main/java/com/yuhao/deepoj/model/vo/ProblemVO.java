package com.yuhao.deepoj.model.vo;

import cn.hutool.json.JSONUtil;
import com.yuhao.deepoj.model.dto.problem.JudgeConfig;
import com.yuhao.deepoj.model.dto.problem.ProblemContent;
import com.yuhao.deepoj.model.entity.Problem;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
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
    private ProblemContent content;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;

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
        ProblemContent problemContentVO = problemVO.getContent();
        if (problemContentVO != null) {
            problem.setContent(JSONUtil.toJsonStr(problemContentVO));
        }
        // 转换判题配置对象为字符串
        JudgeConfig judgeConfigVO = problemVO.getJudgeConfig();
        if (judgeConfigVO != null) {
            problem.setJudgeConfig(JSONUtil.toJsonStr(judgeConfigVO));
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
        String problemContentStr = problem.getContent();
        problemVO.setContent(JSONUtil.toBean(problemContentStr, ProblemContent.class));
        // 判题配置字符串转化为包装类的成员变量
        String judgeConfigStr = problem.getJudgeConfig();
        problemVO.setJudgeConfig(JSONUtil.toBean(judgeConfigStr, JudgeConfig.class));
        return problemVO;
    }

    private static final long serialVersionUID = 1L;
}
