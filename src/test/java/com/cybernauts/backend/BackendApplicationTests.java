package com.cybernauts.backend;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

	@TestConfiguration
	static class EmbeddedMongoConfig {

		@Bean
		public MongoClient mongoClient() {
			// Embedded Mongo will start automatically in tests
			return MongoClients.create("mongodb://localhost:27017/testdb");
		}

		@Bean
		public MongoTemplate mongoTemplate(MongoClient mongoClient) {
			return new MongoTemplate(mongoClient, "testdb");
		}
	}

}
