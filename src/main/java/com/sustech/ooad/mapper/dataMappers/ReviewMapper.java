package com.sustech.ooad.mapper.dataMappers;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.data.Review;

public interface ReviewMapper extends BaseMapper<Review> {
    Review getReviewByOrderId(Integer orderId);
}
