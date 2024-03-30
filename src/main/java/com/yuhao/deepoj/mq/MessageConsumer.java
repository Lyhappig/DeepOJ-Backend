package com.yuhao.deepoj.mq;

import cn.hutool.json.JSONUtil;
import com.rabbitmq.client.Channel;
import com.yuhao.deepoj.common.ErrorCode;
import com.yuhao.deepoj.constant.MqConstant;
import com.yuhao.deepoj.exception.BusinessException;
import com.yuhao.deepoj.judge.JudgeManager;
import com.yuhao.deepoj.judge.JudgeService;
import com.yuhao.deepoj.judge.codesandbox.enums.JudgeResultEnum;
import com.yuhao.deepoj.judge.codesandbox.model.ExecuteCodeResponse;
import com.yuhao.deepoj.judge.codesandbox.model.JudgeInfo;
import com.yuhao.deepoj.judge.strategy.JudgeContext;
import com.yuhao.deepoj.model.dto.problem.JudgeCase;
import com.yuhao.deepoj.model.dto.problem.JudgeConfig;
import com.yuhao.deepoj.model.entity.Problem;
import com.yuhao.deepoj.model.entity.Submission;
import com.yuhao.deepoj.model.enums.SubmissionStatusEnum;
import com.yuhao.deepoj.service.ProblemService;
import com.yuhao.deepoj.service.SubmissionService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class MessageConsumer {
    @Resource
    private ProblemService problemService;

    @Resource
    private SubmissionService submissionService;

    @Resource
    private JudgeManager judgeManager;

    /**
     * 接收消息
     *
     * @param message
     * @param channel
     * @param deliveryTag
     */
    @SneakyThrows
    @RabbitListener(queues = MqConstant.SUCCEED_QUEUE, ackMode = "MANUAL")
    public void receiveSucceedQueueMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        log.info("receive message: {}", message);
        try {
            ExecuteCodeResponse executeCodeResponse = JSONUtil.toBean(message, ExecuteCodeResponse.class);
            Submission submission = submissionService.getById(executeCodeResponse.getSubmissionId());
            Problem problem = problemService.getById(submission.getProblemId());
            String judgeCaseStr = problem.getJudgeCases();
            List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
            JudgeConfig judgeConfig = JSONUtil.toBean(problem.getJudgeConfig(), JudgeConfig.class);

            Submission submissionUpdate = new Submission();
            submissionUpdate.setId(submission.getId());
            submissionUpdate.setStatus(SubmissionStatusEnum.JUDGING.getValue());
            boolean update = submissionService.updateById(submissionUpdate);
            if (!update) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交状态更新错误");
            }
            JudgeContext judgeContext = JudgeContext.builder()
                    .executeCodeResponse(executeCodeResponse)
                    .judgeCaseList(judgeCaseList)
                    .judgeConfig(judgeConfig)
                    .language(submission.getLanguage())
                    .build();
            JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
            // 修改数据库中的判题结果
            CompletableFuture.runAsync(() -> {
                submissionUpdate.setStatus(SubmissionStatusEnum.SUCCEED.getValue());
                submissionUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
                boolean succeed = submissionService.updateById(submissionUpdate);
                if (!succeed) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
                }
            });
            channel.basicAck(deliveryTag, false);
            log.info("Queue[{}] ack message", MqConstant.SUCCEED_QUEUE);
        } catch (Exception e) {
//            if (message.getMessageProperties().getRedelivered()) {
//                log.error("消息已重复处理失败,拒绝再次接收...");
//                channel.basicReject(deliveryTag, false); // 拒绝消息
//            } else {
//                log.info("消息即将再次返回队列处理...");
//                channel.basicNack(deliveryTag, false, true);
//            }
            // b1: true: 消息未处理成功则重新入队
            channel.basicNack(deliveryTag, false, true);
            log.info("Queue[{}] non-ack message", MqConstant.SUCCEED_QUEUE);
        }
    }
}
