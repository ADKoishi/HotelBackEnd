package com.sustech.ooad;

import com.sustech.ooad.Utils.SVGUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootTest
@Slf4j
class HotelApplicationTests {

	@Test
	void contextLoads() {

		SVGUtils.parseSvgFile("C:\\Users\\25749\\Desktop\\OOAD\\final_project\\hotel\\testResources\\pnr.svg");
	}

}
