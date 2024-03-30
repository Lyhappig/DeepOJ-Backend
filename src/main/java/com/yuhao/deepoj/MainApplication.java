package com.yuhao.deepoj;

import com.yuhao.deepoj.mq.InitRabbitMq;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主类（项目启动入口）
 *
 * @author <a href="https://github.com/Lyhappig">YuhaoLian</a>
 * @from <a href="https://lyhappig.github.io/">Blog</a>
 */
// todo 如需开启 Redis，须移除 exclude 中的内容
//@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@SpringBootApplication
@MapperScan("com.yuhao.deepoj.mapper")
@EnableScheduling
@EnableRabbit
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MainApplication {
    public static void main(String[] args) {
        InitRabbitMq.doInit();
        SpringApplication.run(MainApplication.class, args);
    }
}
