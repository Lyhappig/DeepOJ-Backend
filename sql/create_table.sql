# 数据库初始化

-- 创建库
create database if not exists deepoj;

-- 切换库
use deepoj;

-- 用户表
create table if not exists user
(
    id              bigint auto_increment comment 'id'      primary key,
    userAccount     varchar(256)                            not null comment '账号',
    userPassword    varchar(512)                            not null comment '密码',
    unionId         varchar(256)                            null comment '微信开放平台id',
    mpOpenId        varchar(256)                            null comment '公众号openId',
    userName        varchar(256)                            null comment '用户昵称',
    userAvatar      varchar(1024)                           null comment '用户头像',
    userProfile     varchar(512)                            null comment '用户简介',
    userRole        varchar(256) default 'user'             not null comment '用户角色：user/admin/ban',
    createTime      datetime     default CURRENT_TIMESTAMP  not null comment '创建时间',
    updateTime      datetime     default CURRENT_TIMESTAMP  not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint      default 0                  not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 题目表
create table if not exists problem
(
    id                  bigint auto_increment comment 'id'      primary key,
    title               varchar(512)                            null comment '标题',
--     tags                varchar(1024)                           null comment '标签列表（整型数组）',
    content             text                                    null comment '题目内容（json对象）',
    answer              text                                    null comment '题目答案',
    judgeConfig         text                                    null comment '判题配置（json对象）',
    judgeCase           text                                    null comment '判题用例（json数组）',
    difficulty          int      default 0                      not null comment '难度',
    submitNum           int      default 0                      not null comment '题目提交数',
    acceptedNum         int      default 0                      not null comment '题目通过数',
    thumbNum            int      default 0                      not null comment '点赞数',
    favourNum           int      default 0                      not null comment '收藏数',
    userId              bigint                                  not null comment '出题人 id',
    contestId           bigint                                  null comment '比赛 id',
    createTime          datetime default CURRENT_TIMESTAMP      not null comment '创建时间',
    updateTime          datetime default CURRENT_TIMESTAMP      not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete            tinyint  default 0                      not null comment '是否删除',
    index               idx_userId (userId)
) comment '题目' collate = utf8mb4_unicode_ci;

-- 题目提交表
create table if not exists submission
(
    id              bigint auto_increment comment 'id'      primary key,
    problemId       bigint                                  not null comment '题目 id',
    userId          bigint                                  not null comment '用户 id',
    language        varchar(256)                            not null comment '评测语言',
    code            text                                    not null comment '代码',
    status          varchar(256)   default 'Pending'        not null comment '判题状态（枚举值）',
    judgeInfo       text                                    null comment '判题信息（json对象）',
    runTime         bigint         default 0                not null comment '运行时间',
    runMemory       bigint         default 0                not null comment '运行内存',
    createTime datetime default CURRENT_TIMESTAMP           not null comment '提交时间',
    updateTime datetime default CURRENT_TIMESTAMP           not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_problemId (problemId),
    index idx_userId (userId)
) comment '题目提交';
