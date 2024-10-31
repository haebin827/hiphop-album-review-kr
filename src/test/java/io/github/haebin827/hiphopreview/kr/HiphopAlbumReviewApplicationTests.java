package io.github.haebin827.hiphopreview.kr;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootTest
@Log4j2
class HiphopAlbumReviewApplicationTests {

	@Autowired
	private DataSource ds;

	@Test
	void testConnection() throws Exception {
		@Cleanup
		Connection connection = ds.getConnection();
		log.info("Con: " + connection);
		Assertions.assertNotNull(connection);
	}

}
