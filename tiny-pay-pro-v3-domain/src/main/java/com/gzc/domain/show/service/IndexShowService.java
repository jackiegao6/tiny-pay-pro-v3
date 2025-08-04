package com.gzc.domain.show.service;


import com.gzc.domain.show.adapter.repository.IIndexShowRepository;
import com.gzc.domain.show.model.valobj.TeamStatisticVO;
import com.gzc.domain.show.model.valobj.UserInTeamInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexShowService implements IIndexShowService{

    private final IIndexShowRepository indexShowRepository;

    @Override
    public List<UserInTeamInfoVO> queryInProgressUserGroupBuyOrderDetailList(Long activityId, String userId, int ownerCount, int randomCount) {
        List<UserInTeamInfoVO> res = new ArrayList<>();

        // 查询个人拼团数据
        if (0 != ownerCount) {
            List<UserInTeamInfoVO> ownerList = indexShowRepository.queryOwnerInProgressUserDetailList(activityId, userId, ownerCount);
            if (null != ownerList && !ownerList.isEmpty()){
                res.addAll(ownerList);
            }
        }

        // 查询其他非个人拼团
        if (0 != randomCount) {
            List<UserInTeamInfoVO> randomList = indexShowRepository.queryRandomInProgressUserDetailList(activityId, userId, randomCount);
            if (null != randomList && !randomList.isEmpty()){
                res.addAll(randomList);
            }
        }

        return res;
    }

    @Override
    public TeamStatisticVO queryTeamStatisticByActivityId(Long activityId) {
        return indexShowRepository.queryTeamStatisticByActivityId(activityId);
    }
}
