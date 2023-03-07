package team.waitingcatch.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WaitingCatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaitingCatchApplication.class, args);
	}
	
}