package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootTest
@Slf4j
class HotelApplicationTests {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	DataSource dataSource;

	@Test
	void contextLoads() {
		Long data = jdbcTemplate.queryForObject("select count(*) from clip_path", Long.class);
		log.info("记录{}", data);
		log.info("数据源类型{}", dataSource.getClass());
	}

}
