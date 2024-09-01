package pl.ofertownia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.ofertownia.mail.MailProperties;

@SpringBootApplication
@EnableConfigurationProperties({MailProperties.class})
public class OfertowniaApplication {

	public static void main(String[] args) {
		SpringApplication.run(OfertowniaApplication.class, args);
	}

}
