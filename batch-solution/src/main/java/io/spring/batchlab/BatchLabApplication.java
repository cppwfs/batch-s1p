package io.spring.batchlab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class BatchLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchLabApplication.class, args);

	}

	@Bean
	public CommandLineRunner commandLineRunner(ConfigurableApplicationContext context) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				checkResults(context.getBean(JdbcTemplate.class));
			}
		};
	}
	/**
	 * Checks the People table to see if it contains Person records.
	 *
	 * @param jdbcTemplate
	 *            A JdbcTemplate for checking the PEOPLE table contents.
	 */
	public static void checkResults(JdbcTemplate jdbcTemplate) {
		final Logger logger = LoggerFactory.getLogger(BatchLabApplication.class);

		// Check the application worked
		Long creationCount = jdbcTemplate.queryForObject("SELECT count(*) FROM People", Long.class);

		logger.info("Finished. Application loaded " + creationCount + " people");

		if (creationCount == 0)
			throw new IllegalStateException("No People records were created");
		else
			logger.info("Congratulations, application SUCCEEDED");

	}
}

