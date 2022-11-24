package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestHandler{
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/query")
    public String queryHandler(){
        return jdbcTemplate.queryForObject("select count(*) from clip_path", String.class);
    }
}
