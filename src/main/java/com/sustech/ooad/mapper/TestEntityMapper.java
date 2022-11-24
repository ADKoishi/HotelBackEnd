package com.sustech.ooad.mapper;

import com.sustech.ooad.entity.TestEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestEntityMapper {

    TestEntity getTestEntityByID(Integer id);
}
