package com.sustech.ooad.controller;

import com.sustech.ooad.entity.TestEntity;
import com.sustech.ooad.mapper.TestEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
