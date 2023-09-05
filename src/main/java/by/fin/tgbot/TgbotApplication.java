package by.fin.tgbot;

// link to GitHub source code with sample project
// https://github.com/SkillfactoryCoding/JAVA-Spring-skillfactory_bot

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TgbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TgbotApplication.class, args);
	}

}
