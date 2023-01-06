package com.sustech.ooad.mapper.dataMappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.data.Order;

import java.sql.Date;
import java.util.List;

public interface OrderMapper extends BaseMapper<Order> {
    Integer getOrderConflictedCntByDateAndRoomId(Date startDate, Date endDate, Integer roomId);

    void placeOrder(Order order);

    Double getOrderPriceSum();

    List<Order> getOrderByUserId(Integer userId);

    Order getOrderById(Integer id);

    void moveOrderToDeletedById(Integer id);

    void deleteAvailOrder(Integer id);
}
