package com.sustech.ooad.mapper.dataMappers;

import com.sustech.ooad.entity.data.TestEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestEntityMapper {

    TestEntity getTestEntityByID(Integer id);
}
