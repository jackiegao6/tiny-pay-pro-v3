package com.gzc.domain.show.adapter.repository;

import com.gzc.domain.show.model.valobj.TeamStatisticVO;
import com.gzc.domain.show.model.valobj.UserInTeamInfoVO;

import java.util.List;

public interface IIndexShowRepository {


    List<UserInTeamInfoVO> queryOwnerInProgressUserDetailList(Long activityId, String userId, int ownerCount);

    List<UserInTeamInfoVO> queryRandomInProgressUserDetailList(Long activityId, String userId, int randomCount);

    TeamStatisticVO queryTeamStatisticByActivityId(Long activityId);

}
