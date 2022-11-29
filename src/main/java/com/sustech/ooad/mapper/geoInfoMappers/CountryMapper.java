package com.sustech.ooad.mapper.geoInfoMappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.geoInfo.Country;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface CountryMapper extends BaseMapper<Country> {
    List<Country> getAllCountries();
}
