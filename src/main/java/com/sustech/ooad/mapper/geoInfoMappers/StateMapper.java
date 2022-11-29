package com.sustech.ooad.mapper.geoInfoMappers;

import com.sustech.ooad.entity.geoInfo.State;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface StateMapper {
    List<State> getStatesByCountryCode(String countryCode);
}
