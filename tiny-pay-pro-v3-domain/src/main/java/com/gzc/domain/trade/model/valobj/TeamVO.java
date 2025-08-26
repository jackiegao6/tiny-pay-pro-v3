package com.gzc.domain.trade.model.valobj;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamVO {

    private Map<String, Map<String, String>> user2orderMap;

}
