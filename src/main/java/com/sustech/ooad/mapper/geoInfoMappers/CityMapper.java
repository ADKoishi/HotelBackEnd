package com.sustech.ooad.mapper.geoInfoMappers;

import com.sustech.ooad.entity.geoInfo.City;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CityMapper {
    List<City> getCitiesByStateCode(String countryCode, String stateCode);
}
