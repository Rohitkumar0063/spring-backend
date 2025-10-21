package com.cybernauts.backend;


import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		String mongoUri = System.getenv("MONGO_URI");
		System.out.println("MONGO_URI: " + (mongoUri != null ? "SET" : "NULL"));
		if (mongoUri != null) {
			System.out.println("URI starts with: " + mongoUri.substring(0, Math.min(20, mongoUri.length())));

		}
		SpringApplication.run(BackendApplication.class, args);
	}


	@Bean
	public WebMvcConfigurer corsConfigurer() {
		System.out.println("✅ Global CORS config initialized");

		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@NotNull CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins(
								"http://localhost:3000",
								"https://playful-arithmetic-5db94a.netlify.app",
								"https://*.netlify.app" // wildcard subdomains for Netlify
						)
						.allowedOriginPatterns("*")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("*")
						.allowCredentials(true);
			}
		};
	}

}
