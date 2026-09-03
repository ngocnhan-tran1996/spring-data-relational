/*
 * Copyright 2023-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.data.jdbc.core.convert;

import org.springframework.data.relational.core.sql.SqlIdentifiers;
import org.springframework.util.Assert;

/**
 * Sanitizes the name of bind parameters, so they don't contain any illegal characters.
 *
 * @author Jens Schauder
 * @author Christoph Strobl
 * @since 3.0.2
 */
abstract class BindParameterNameSanitizer {

	static String sanitize(String rawName) {

		String sanitized = SqlIdentifiers.strip(rawName);

		Assert.hasText(sanitized,
				() -> "Bind parameter for column %s resulted in an empty identifier".formatted(rawName));

		return sanitized;
	}
}
