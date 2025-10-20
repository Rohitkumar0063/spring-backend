package com.cybernauts.backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		// Load .env BEFORE Spring starts
		Dotenv dotenv = Dotenv.configure()
				.directory(".")
				.ignoreIfMissing()
				.load();

// Map env to Spring property
		System.setProperty("spring.data.mongodb.uri", dotenv.get("SPRING_DATA_MONGODB_URI"));
		System.setProperty("server.port", dotenv.get("PORT"));

		System.out.println("✅ Mongo URI loaded: " + System.getProperty("spring.data.mongodb.uri"));
		System.out.println("✅ Port: " + System.getProperty("server.port"));


		// Start Spring Boot
		SpringApplication.run(BackendApplication.class, args);
	}

	// Optional: CORS configuration
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@NotNull CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:3000");
			}
		};
	}
}
