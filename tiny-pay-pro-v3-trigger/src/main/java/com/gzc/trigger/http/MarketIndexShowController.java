package com.gzc.trigger.http;

import com.gzc.api.IMarketIndexShowService;
import com.gzc.api.dto.req.MarketIndexShowRequestDTO;
import com.gzc.api.dto.resp.MarketIndexShowResponseDTO;
import com.gzc.api.response.Response;
import com.gzc.domain.show.model.valobj.TeamStatisticVO;
import com.gzc.domain.show.model.valobj.UserInTeamInfoVO;
import com.gzc.domain.show.service.IIndexShowService;
import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;
import com.gzc.domain.trial.service.trail.ITrailService;
import com.gzc.types.enums.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/gbm/index")
public class MarketIndexShowController implements IMarketIndexShowService {

    private final ITrailService trailService;
    private final IIndexShowService iIndexShowService;

    @RequestMapping(value = "/config", method = RequestMethod.POST)
    @Override
    public Response<MarketIndexShowResponseDTO> queryGroupBuyMarketConfig(@RequestBody MarketIndexShowRequestDTO marketIndexShowRequestDTO) {
        try {
            String userId = marketIndexShowRequestDTO.getUserId();

            // 1. 营销优惠试算
            TrailBalanceEntity trialBalanceEntity = trailService.indexMarketTrial(TrailMarketProductEntity.builder()
                    .userId(userId)
                    .goodsId(marketIndexShowRequestDTO.getGoodsId())
                    .build());
            MarketIndexShowResponseDTO.GoodsInfo goodsInfo = MarketIndexShowResponseDTO.GoodsInfo.builder()
                    .goodsId(trialBalanceEntity.getGoodsId())
                    .originalPrice(trialBalanceEntity.getOriginalPrice())
                    .deductionPrice(trialBalanceEntity.getDeductionPrice())
                    .currentPrice(trialBalanceEntity.getCurrentPrice())
                    .build();


            // 2. 查询拼团组队 先查自己的 再随机查两条别人的拼团数据
            Long activityId = trialBalanceEntity.getActivityId();
            List<UserInTeamInfoVO> UserInTeamInfoVOs = iIndexShowService.queryInProgressUserGroupBuyOrderDetailList(activityId, userId, 1, 2);
            List<MarketIndexShowResponseDTO.TeamInfo> teamInfos = new ArrayList<>();
            if (null != UserInTeamInfoVOs && !UserInTeamInfoVOs.isEmpty()) {
                for (UserInTeamInfoVO UserInTeamInfoVO : UserInTeamInfoVOs) {
                    MarketIndexShowResponseDTO.TeamInfo teamInfo = MarketIndexShowResponseDTO.TeamInfo.builder()
                            .userId(UserInTeamInfoVO.getUserId())
                            .teamId(UserInTeamInfoVO.getTeamId())
                            .activityId(UserInTeamInfoVO.getActivityId())
                            .targetCount(UserInTeamInfoVO.getTargetCount())
                            .completeCount(UserInTeamInfoVO.getCompleteCount())
                            .lockCount(UserInTeamInfoVO.getLockCount())
                            .validStartTime(UserInTeamInfoVO.getValidStartTime())
                            .validEndTime(UserInTeamInfoVO.getValidEndTime())
                            .validTimeCountdown(MarketIndexShowResponseDTO.TeamInfo.differenceDateTime2Str(new Date(), UserInTeamInfoVO.getValidEndTime()))
                            .outTradeNo(UserInTeamInfoVO.getOutTradeNo())
                            .build();
                    teamInfos.add(teamInfo);
                }
            }

            // 3. 统计拼团数据
            TeamStatisticVO teamStatisticVO = iIndexShowService.queryTeamStatisticByActivityId(activityId);
            MarketIndexShowResponseDTO.TeamStatisticInfo teamStatisticInfo = MarketIndexShowResponseDTO.TeamStatisticInfo.builder()
                    .allTeamCount(teamStatisticVO.getAllTeamCount())
                    .allTeamCompleteCount(teamStatisticVO.getAllTeamCompleteCount())
                    .allTeamUserCount(teamStatisticVO.getAllTeamUserCount())
                    .build();

            Response<MarketIndexShowResponseDTO> response = Response.<MarketIndexShowResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(MarketIndexShowResponseDTO.builder()
                            .goodsInfo(goodsInfo)
                            .teamInfos(teamInfos)
                            .teamStatisticInfo(teamStatisticInfo)
                            .build())
                    .build();
            log.info("拼团首页数据加载 完成");

            return response;
        } catch (Exception e) {
            log.error("查询拼团营销配置失败", e);
            return Response.<MarketIndexShowResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
