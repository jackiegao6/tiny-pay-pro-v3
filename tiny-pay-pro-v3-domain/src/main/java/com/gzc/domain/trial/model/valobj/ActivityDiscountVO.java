package com.gzc.domain.trial.model.valobj;

import com.gzc.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDiscountVO {
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 活动名称
     */
    private String activityName;
    /**
     * 商品ID
     */
    private String goodsId;
    /**
     * 折扣配置
     */
    private DiscountVO discountVO;
    /**
     * 拼团方式（0自动成团、1达成目标拼团）
     */
    private Integer groupType;
    /**
     * 拼团次数限制
     */
    private Integer takeLimitCount;
    /**
     * 拼团目标
     */
    private Integer target;
    /**
     * 拼团时长（分钟）
     */
    private Integer validTime;
    /**
     * 活动状态（0创建、1生效、2过期、3废弃）
     */
    private Integer status;
    /**
     * 活动开始时间
     */
    private Date startTime;
    /**
     * 活动结束时间
     */
    private Date endTime;
    /**
     * 人群标签规则标识
     */
    private String tagId;
    /**
     * 人群标签规则范围
     */
    private String tagScope;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DiscountVO {
        /**
         * 折扣标题
         */
        private String discountName;

        /**
         * 折扣描述
         */
        private String discountDesc;

        /**
         * 折扣类型（0:base、1:tag）
         */
        private Byte discountType;

        /**
         * 营销优惠计划（ZJ:直减、MJ:满减、N元购）
         */
        private String marketPlan;

        /**
         * 营销优惠表达式
         */
        private String marketExpr;
    }

    /**
     * 可见限制 & 参与限制
     * 只要存在这样一个值，那么首次获得的默认值就是 false
     */
    public boolean[] getTagRestrictFlag() {
        String[] split = this.tagScope.split(Constants.SPLIT);
        boolean[] res = new boolean[2];
        for (String tagRestrict : split) {
            if (Objects.equals(tagRestrict, "1")){
                res[0] = true;
                continue;
            }
            if (Objects.equals(tagRestrict, "2")){
                res[1] = true;
            }
        }
        return res;
    }


}
