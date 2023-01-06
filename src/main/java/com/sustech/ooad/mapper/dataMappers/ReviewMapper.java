package com.sustech.ooad.mapper.dataMappers;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.data.Review;

import java.util.List;

public interface ReviewMapper extends BaseMapper<Review> {
    Review getReviewByOrderId(Integer orderId);

    void insertReview(Review review);

    List<Review> getAllRevies();
}
