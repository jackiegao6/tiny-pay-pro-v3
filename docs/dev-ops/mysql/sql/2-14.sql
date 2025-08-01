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
    `notify_url` varchar(512) NOT NULL COMMENT '回调地址',
    `create_time`     datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uq_team_id` (`team_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

LOCK TABLES `group_buy_order` WRITE;
/*!40000 ALTER TABLE `group_buy_order` DISABLE KEYS */;

INSERT INTO `group_buy_order` (`id`, `team_id`, `activity_id`, `original_price`, `deduction_price`, `current_price`, `target_count`, `complete_count`, `lock_count`, `status`, `valid_start_time`, `valid_end_time`, `notify_url`, `create_time`, `update_time`)
VALUES
    (3,'80759049',100123,100.00,10.00,90.00,3,3,3,1,'2025-01-31 17:28:19','2025-01-31 18:28:19','http://127.0.0.1:8091/api/v1/test/group_buy_notify','2025-01-31 17:28:19','2025-01-31 17:51:38');

/*!40000 ALTER TABLE `group_buy_order` ENABLE KEYS */;
UNLOCK TABLES;

