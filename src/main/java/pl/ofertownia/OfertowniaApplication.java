package pl.ofertownia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.ofertownia.mail.MailProperties;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.TimeZone;

@SpringBootApplication
@EnableConfigurationProperties({MailProperties.class})
public class OfertowniaApplication {

	public static void main(String[] args) {

		SpringApplication.run(OfertowniaApplication.class, args);
	}

}
