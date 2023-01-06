package com.sustech.ooad.mapper.dataMappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.data.Room;

import java.util.List;

public interface RoomMapper extends BaseMapper<Room> {
    List<Room> getRoomByTowerAndFloor(Integer towerId, Integer floorNumber);
    Room getRoomById(Integer id);
}
