package com.sustech.ooad.mapper.dataMappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.data.Hotel;

import java.util.List;

public interface HotelMapper extends BaseMapper<Hotel> {
    List<Hotel> getHotelByCoordinate(Double longitude, Double latitude, Integer RETURN_CNT);
}
