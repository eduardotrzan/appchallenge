package ca.appdirect.appchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication
public class AppChallengeApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(AppChallengeApplication.class);
	}

	public static void main(final String[] args) {
		SpringApplication.run(AppChallengeApplication.class, args);
	}
}
