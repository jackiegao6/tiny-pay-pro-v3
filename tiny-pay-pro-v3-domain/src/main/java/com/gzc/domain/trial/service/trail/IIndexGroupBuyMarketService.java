package com.gzc.domain.trial.service.trail;

import com.gzc.domain.trial.model.entity.req.TrailMarketProductEntity;
import com.gzc.domain.trial.model.entity.resp.TrailBalanceEntity;

public interface IIndexGroupBuyMarketService {

    TrailBalanceEntity indexMarketTrial(TrailMarketProductEntity trailMarketProductEntity) throws Exception;
}
