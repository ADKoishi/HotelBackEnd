package com.sustech.ooad.mapper.dataMappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.data.Order;

import java.sql.Date;

public interface OrderMapper extends BaseMapper<Order> {
    Integer getOrderConflictedCntByDateAndRoomId(Date startDate, Date endDate, Integer roomId);

    void placeOrder(Order order);
}
