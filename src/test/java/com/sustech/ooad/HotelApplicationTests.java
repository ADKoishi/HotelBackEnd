package com.sustech.ooad;

import com.sustech.ooad.Utils.SVGUtils;
import com.sustech.ooad.property.StaticProp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.xml.sax.SAXException;

import javax.sql.DataSource;
import java.io.IOException;

@SpringBootTest
@Slf4j
class HotelApplicationTests {
	@Autowired
	StaticProp staticProp;
	@Test
	void contextLoads() {
		try {
			System.out.println(SVGUtils.parseSvgFile("C:\\Users\\25749\\Desktop\\OOAD\\final_project\\statics\\tower\\1\\floor\\2.svg"));
		} catch (IOException | SAXException e) {
			throw new RuntimeException(e);
		}
	}

}
