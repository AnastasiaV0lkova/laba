package mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.TimeZone;


@SpringBootApplication
public class SpringBootRunner {

	@PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
    }

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRunner.class, args);
	}
}
