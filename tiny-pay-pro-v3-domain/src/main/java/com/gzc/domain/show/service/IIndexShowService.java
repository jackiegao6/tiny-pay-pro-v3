package com.gzc.domain.show.service;

import com.gzc.domain.show.model.valobj.TeamStatisticVO;
import com.gzc.domain.show.model.valobj.UserInTeamInfoVO;

import java.util.List;

public interface IIndexShowService {

    List<UserInTeamInfoVO> queryInProgressUserGroupBuyOrderDetailList(Long activityId, String userId, int ownerCount, int randomCount);

    TeamStatisticVO queryTeamStatisticByActivityId(Long activityId);

}
