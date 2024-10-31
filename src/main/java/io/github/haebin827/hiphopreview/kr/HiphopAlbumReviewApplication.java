package io.github.haebin827.hiphopreview.kr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HiphopAlbumReviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiphopAlbumReviewApplication.class, args);
	}

}
