package com.sustech.ooad.mapper.dataMappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.data.Hotel;
import com.sustech.ooad.entity.data.Room;

import java.util.List;

public interface HotelMapper extends BaseMapper<Hotel> {
    List<Hotel> getHotelsByCoordinate(Double longitude, Double latitude, Integer RETURN_CNT);

    Integer getAccessibleRoomCount(Integer hotelId);

    Integer getHotelCountryIdByHotelId(Integer hotelId);

    List<Hotel> getHotelsByCityIdS0(Integer cityId);

}
