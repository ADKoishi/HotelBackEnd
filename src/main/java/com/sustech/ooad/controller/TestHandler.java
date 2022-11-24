package com.sustech.ooad.controller;

import com.sustech.ooad.entity.data.TestEntity;
import com.sustech.ooad.mapper.TestEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestHandler{

    @Autowired
    TestEntityMapper testEntityMapper;

    @GetMapping("/query")
    public TestEntity queryHandler(){
        return testEntityMapper.getTestEntityByID(1);
    }
}
