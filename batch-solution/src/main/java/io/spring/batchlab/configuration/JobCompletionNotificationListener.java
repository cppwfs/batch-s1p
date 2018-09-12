/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.spring.batchlab.configuration;

import io.spring.batchlab.domain.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


/**
 * A listener that will run when the job completes and validate the names of all
 * the people processed and added to the database.
 */
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	public static final String UPPER_CASE_REGEX = "[A-Z]*";

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	// Spring Boot creates this automatically
	private final JdbcTemplate jdbcTemplate;

	/**
	 * Get Spring to pass in the {@link JdbcTemplate} instance created by Spring
	 * Boot.
	 *
	 * @param jdbcTemplate
	 *            Auto-generated JDBC Template instance.
	 */
	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Iterate over the PEOPLE table, listing the records found.
	 */
	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");

			jdbcTemplate.query("SELECT first_name, last_name FROM people",
					(rs, row) -> new Person(rs.getString(1), rs.getString(2))).forEach(person -> {
				validate(person);
			});
		}
	}

	/**
	 * Throws an exception if the Person has names that are not completely upper
	 * case. Otherwise it logs them.
	 *
	 * @param person
	 *            A person object from the database.
	 */
	protected void validate(Person person) {

		if (person.getFirstName().matches(UPPER_CASE_REGEX) && person.getLastName().matches(UPPER_CASE_REGEX))
			log.info("Found <" + person + "> in the database.");
		else
			throw new IllegalStateException("Person [" + person + "] not converted to upper-case");
	}
}