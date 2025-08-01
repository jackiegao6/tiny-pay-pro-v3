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
    `valid_start_time` datetime NOT NULL COMMENT '拼团开始时间',
    `valid_end_time` datetime NOT NULL COMMENT '拼团结束时间',
    `create_time`     datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_team_id` (`team_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

LOCK TABLES `group_buy_order` WRITE;
/*!40000 ALTER TABLE `group_buy_order` DISABLE KEYS */;

INSERT INTO `group_buy_order` (`id`, `team_id`, `activity_id`, `original_price`, `deduction_price`, `current_price`, `target_count`, `complete_count`, `lock_count`, `status`, `valid_start_time`, `valid_end_time`, `create_time`, `update_time`)
VALUES
    (1,'93125665',100123,100.00,10.00,90.00,3,2,2,0,'2025-01-29 16:50:34','2025-01-30 17:05:34','2025-01-29 16:50:34','2025-01-29 17:13:02');

/*!40000 ALTER TABLE `group_buy_order` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 group_buy_order_list
# ------------------------------------------------------------

DROP TABLE IF EXISTS `group_buy_order_list`;

CREATE TABLE `group_buy_order_list`
(
    `id`             int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `user_id`        varchar(64)      NOT NULL COMMENT '用户ID',
    `team_id`        varchar(8)       NOT NULL COMMENT '拼单组队ID',
    `order_id`       varchar(12)      NOT NULL COMMENT '订单ID',
    `activity_id`    bigint(8)        NOT NULL COMMENT '活动ID',
    `start_time`     datetime         NOT NULL COMMENT '活动开始时间',
    `end_time`       datetime         NOT NULL COMMENT '活动结束时间',
    `goods_id`       varchar(16)      NOT NULL COMMENT '商品ID',
    `original_price` decimal(8, 2)    NOT NULL COMMENT '原始价格',
    `current_price`  decimal(8, 2)    NOT NULL COMMENT '折扣后金额',
    `status`         tinyint(1)       NOT NULL DEFAULT '0' COMMENT '状态；0初始锁定、1消费完成',
    `out_trade_no`   varchar(12)      NOT NULL COMMENT '外部交易单号-确保外部调用唯一幂等',
    `out_trade_time` datetime DEFAULT NULL COMMENT '外部交易时间',
    `biz_id` varchar(64) NOT NULL COMMENT '业务唯一ID',
    `create_time`    datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_order_id` (`order_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


LOCK TABLES `group_buy_order_list` WRITE;
/*!40000 ALTER TABLE `group_buy_order_list` DISABLE KEYS */;

INSERT INTO `group_buy_order_list` (`id`, `user_id`, `team_id`, `order_id`, `activity_id`, `start_time`, `end_time`, `goods_id`,  `original_price`, `current_price`, `status`, `out_trade_no`, `out_trade_time`, `biz_id`, `create_time`, `update_time`)
VALUES
    (37,'xfg01','93125665','142771127603',100123,'2024-12-07 10:19:40','2025-12-07 10:19:40','9890001',100.00,10.00,1,'514260871457',now(),'100123_xfg01_1','2025-01-29 16:50:34','2025-01-29 16:54:10'),
    (38,'xfg04','93125665','357868243232',100123,'2024-12-07 10:19:40','2025-12-07 10:19:40','9890001',100.00,10.00,1,'075605651839','2025-01-29 17:13:02','100123_xfg04_1','2025-01-29 17:10:12','2025-01-29 17:13:02');

/*!40000 ALTER TABLE `group_buy_order_list` ENABLE KEYS */;
UNLOCK TABLES;


# 转储表 notify_task
# ------------------------------------------------------------

DROP TABLE IF EXISTS `notify_task`;

CREATE TABLE `notify_task` (
                               `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
                               `activity_id` bigint(8) NOT NULL COMMENT '活动ID',
                               `team_id` varchar(8) NOT NULL COMMENT '拼单组队ID',
                               `notify_url` varchar(128) NOT NULL COMMENT '回调接口',
                               `notify_count` int(8) NOT NULL COMMENT '回调次数',
                               `notify_status` tinyint(1) NOT NULL COMMENT '回调状态【0初始、1完成、2重试、3失败】',
                               `parameter_json` varchar(256) NOT NULL COMMENT '参数对象',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

LOCK TABLES `notify_task` WRITE;
/*!40000 ALTER TABLE `notify_task` DISABLE KEYS */;

INSERT INTO `notify_task` (`id`, `activity_id`, `team_id`, `notify_url`, `notify_count`, `notify_status`, `parameter_json`, `create_time`, `update_time`)
VALUES
    (1,100123,'46832479','暂无',0,0,'{\"teamId\":\"46832479\",\"outTradeNoList\":[\"581909866926\",\"155123092895\",\"451517755304\"]}','2025-01-26 19:11:46','2025-01-26 19:11:46'),
    (2,100123,'38795123','暂无',0,0,'{\"teamId\":\"38795123\",\"outTradeNoList\":[\"134597814295\",\"154310924273\",\"228984300880\"]}','2025-01-28 08:27:26','2025-01-28 08:27:26'),
    (3,100123,'57199993','暂无',0,0,'{\"teamId\":\"57199993\",\"outTradeNoList\":[\"038426231487\",\"652896391719\",\"619401409195\"]}','2025-01-28 09:13:00','2025-01-28 09:13:00');

/*!40000 ALTER TABLE `notify_task` ENABLE KEYS */;
UNLOCK TABLES;
