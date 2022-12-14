package com.sustech.ooad.mapper.geoInfoMappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.geoInfo.State;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface StateMapper extends BaseMapper<State> {
    List<State> getStatesByCountryCode(String countryCode);
}
