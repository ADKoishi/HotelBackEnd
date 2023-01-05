package com.sustech.ooad.mapper.dataMappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sustech.ooad.entity.data.Category;

import java.util.List;

public interface CategoryMapper extends BaseMapper<Category> {
    List<Category> getCategoriesByHotelId(Integer hotelId);
    Category getCategoryById(Integer categoryId);
}
