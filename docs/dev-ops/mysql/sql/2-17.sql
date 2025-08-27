# 转储表 group_buy_order
# ------------------------------------------------------------
use `group_buy_market`;
DROP TABLE IF EXISTS `group_buy_order`;

CREATE TABLE `group_buy_order`
(
    `id`              int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `team_id`         varchar(8)       NOT NULL COMMENT '拼单组队ID',
    `activity_id`     bigint(8)        NOT NULL COMMENT '活动ID',
    `original_price`  decimal(8, 2)    NOT NULL COMMENT '原始价格',
    `deduction_price` decimal(8, 2)    NOT NULL COMMENT '折扣金额',
    `current_price`   decimal(8, 2)    NOT NULL COMMENT '支付价格',
    `target_count`    int(5)           NOT NULL COMMENT '目标数量',
    `complete_count`  int(5)           NOT NULL COMMENT '完成数量',
    `lock_count`      int(5)           NOT NULL COMMENT '锁单数量',
    `status`          tinyint(1)       NOT NULL DEFAULT '0' COMMENT '状态（0-拼单中、1-完成、2-失败）默认：0',
    `valid_start_time`datetime         NOT NULL COMMENT '拼团开始时间',
    `valid_end_time`  datetime         NOT NULL COMMENT '拼团结束时间',
    `notify_type` varchar(8)           NOT NULL DEFAULT 'HTTP' COMMENT '回调类型（HTTP、MQ）',
    `notify_url` varchar(512)          NULL COMMENT '回调地址（HTTP 回调不可为空）',
    `create_time`     datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_team_id` (`team_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

LOCK TABLES `group_buy_order` WRITE;
/*!40000 ALTER TABLE `group_buy_order` DISABLE KEYS */;

INSERT INTO `group_buy_order` (`id`, `team_id`, `activity_id`, `original_price`, `deduction_price`, `current_price`, `target_count`, `complete_count`, `lock_count`, `status`, `valid_start_time`, `valid_end_time`, `notify_type`, `notify_url`, `create_time`, `update_time`)
VALUES
    (1,'58693013',100123,100.00,20.00,80.00,1,1,1,1,'2025-03-16 17:43:44','2025-05-16 17:58:44','MQ',NULL,'2025-03-16 17:43:43','2025-03-16 18:23:05'),
    (2,'16341565',100123,100.00,20.00,80.00,1,1,1,1,'2025-03-16 18:27:52','2025-03-16 18:42:52','HTTP','http://127.0.0.1:8091/api/v1/test/group_buy_notify','2025-03-16 18:27:51','2025-03-16 18:28:58'),
    (3,'63403622',100123,100.00,20.00,80.00,1,1,1,1,'2025-03-17 22:11:26','2025-03-17 22:26:26','MQ',NULL,'2025-03-17 22:11:26','2025-03-17 22:12:04');


/*!40000 ALTER TABLE `group_buy_order` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 notify_task
# ------------------------------------------------------------

DROP TABLE IF EXISTS `notify_task`;

CREATE TABLE `notify_task` (
                               `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                               `activity_id` bigint(8) NOT NULL COMMENT '活动ID',
                               `team_id` varchar(8) NOT NULL COMMENT '拼单组队ID',
                               `notify_type` varchar(8) NOT NULL DEFAULT 'HTTP' COMMENT '回调类型（HTTP、MQ）',
                               `notify_mq` varchar(32) DEFAULT NULL COMMENT '回调主题',
                               `notify_url` varchar(128) DEFAULT NULL COMMENT '回调接口',
                               `notify_count` int(8) NOT NULL COMMENT '回调次数',
                               `notify_status` tinyint(1) NOT NULL COMMENT '回调状态【0初始、1完成、2重试、3失败】',
                               `parameter_json` varchar(256) NOT NULL COMMENT '参数对象',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `uq_team_id` (`team_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


LOCK TABLES `notify_task` WRITE;
/*!40000 ALTER TABLE `notify_task` DISABLE KEYS */;

INSERT INTO `notify_task` (`id`, `activity_id`, `team_id`, `notify_type`, `notify_mq`, `notify_url`, `notify_count`, `notify_status`, `parameter_json`, `create_time`, `update_time`)
VALUES
    (7,100123,'58693013','MQ','topic.team_success',NULL,1,1,'{\"teamId\":\"58693013\",\"outTradeNoList\":[\"214969043474\"]}','2025-03-16 18:23:05','2025-03-16 18:23:05'),
    (8,100123,'16341565','HTTP','topic.team_success','http://127.0.0.1:8091/api/v1/test/group_buy_notify',1,1,'{\"teamId\":\"16341565\",\"outTradeNoList\":[\"539291175688\"]}','2025-03-16 18:28:59','2025-03-16 18:29:12'),
    (9,100123,'63403622','MQ','topic.team_success',NULL,1,1,'{\"teamId\":\"63403622\",\"outTradeNoList\":[\"904941690333\"]}','2025-03-17 22:12:04','2025-03-17 22:12:04');


/*!40000 ALTER TABLE `notify_task` ENABLE KEYS */;
UNLOCK TABLES;
