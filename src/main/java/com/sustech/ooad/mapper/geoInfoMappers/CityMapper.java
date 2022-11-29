package com.sustech.ooad.mapper.geoInfoMappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.geoInfo.City;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface CityMapper extends BaseMapper<City> {
    List<City> getCitiesByStateCode(String countryCode, String stateCode);
}
