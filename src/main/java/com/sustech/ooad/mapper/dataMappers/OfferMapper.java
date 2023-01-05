package com.sustech.ooad.mapper.dataMappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.data.Offer;
import com.sustech.ooad.entity.data.Tower;

import java.util.List;

public interface OfferMapper extends BaseMapper<Offer> {
    Offer getOfferByCode(String offerCode);
}
