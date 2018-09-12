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
import org.springframework.batch.item.ItemProcessor;

/**
 * Custom Item Processor converting all names to upper case.
 */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

	@Override
	public Person process(final Person person) throws Exception {
		// Force name to upper-case
		final Person transformedPerson = new Person( //
				person.getFirstName().toUpperCase(), //
				person.getLastName().toUpperCase());

		log.info("Converting (" + person + ") into (" + transformedPerson + ")");
		return transformedPerson;
	}

}
