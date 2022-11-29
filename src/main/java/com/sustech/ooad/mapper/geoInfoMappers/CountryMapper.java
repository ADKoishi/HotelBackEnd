package com.sustech.ooad.mapper.geoInfoMappers;

import com.sustech.ooad.entity.geoInfo.Country;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface CountryMapper {
    List<Country> getAllCountries();
}
