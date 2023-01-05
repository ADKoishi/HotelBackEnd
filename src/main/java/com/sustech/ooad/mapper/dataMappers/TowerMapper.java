package com.sustech.ooad.mapper.dataMappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.data.Tower;

import java.util.List;

public interface TowerMapper extends BaseMapper<Tower> {
    List<Tower> getTowersByHotelId(Integer hotelId);
}
