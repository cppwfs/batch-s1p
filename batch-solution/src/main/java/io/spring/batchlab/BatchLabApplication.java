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

	/**
	 * The number of lines in <tt>src/main/resources/sample-data.csv</tt> - one per
	 * person.
	 */
	public static final int RECORDS_EXPECTED = 5;


	public static void main(String[] args) {
		SpringApplication.run(BatchLabApplication.class, args);

	}

	@Bean
	public CommandLineRunner commandLineRunner(ConfigurableApplicationContext context) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				//ApplicationContext context = SpringApplication.run(BatchdemoApplication.class, args);
				checkResults(context.getBean(JdbcTemplate.class));
			}
		};
	}
	/**
	 * Checks the in-memory test database (H2) contains the expected number of
	 * Person records.
	 *
	 * @param jdbcTemplate
	 *            A JdbcTemplate for checking the PEOPLE table contents.
	 */
	public static void checkResults(JdbcTemplate jdbcTemplate) {
		final Logger logger = LoggerFactory.getLogger(BatchLabApplication.class);

		// Check the application worked
		Long creationCount = jdbcTemplate.queryForObject("SELECT count(*) FROM People", Long.class);

		logger.info("Finished. Application loaded " + creationCount + " people");

		if (creationCount != RECORDS_EXPECTED)
			throw new IllegalStateException("FAIL: Five Person records should have been created");
		else
			logger.info("Congratulations, application SUCCEEDED");

	}
}
