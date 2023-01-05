package com.sustech.ooad;

import com.sustech.ooad.Utils.SVGUtils;
import com.sustech.ooad.property.StaticProp;
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
	StaticProp staticProp;
	@Test
	void contextLoads() {

		System.out.println(staticProp.getStaticUrl());
		System.out.println(staticProp.getStaticDirectory());
	}

}
