/*
 * Copyright 2026-present the original author or authors.
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
package org.springframework.data.relational.core.sql;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Defines the set of characters Spring Data Relational considers legal in an unquoted SQL identifier: letters,
 * combining marks, decimal digits and {@code _}. Note that {@code \p{L}} and {@code \p{M}} cover non ASCII scripts,
 * while {@code \p{Nd}} deliberately excludes numeric characters that are not decimal digits, like {@code ½} or
 * {@code Ⅳ}.
 * <p>
 * Callers either {@link #strip(String) strip} the illegal characters from a name they derive themselves, or
 * {@link #isValidPath(String) check} a name that was handed to them by the user.
 *
 * @author Jens Schauder
 * @since 4.2
 */
public class SqlIdentifiers {

	private static final String IDENTIFIER_CHARACTERS = "\\p{L}\\p{M}\\p{Nd}_";

	private static final Pattern ILLEGAL_IDENTIFIER_CHARACTER = Pattern.compile("[^" + IDENTIFIER_CHARACTERS + "]");

	private static final Predicate<String> CONTAINS_ILLEGAL_PATH_CHARACTER = Pattern
			.compile("[^" + IDENTIFIER_CHARACTERS + ".]").asPredicate();

	/**
	 * Removes all characters that are illegal in an identifier from {@code name}. The result may be empty, it is up to
	 * the caller to decide if that is acceptable.
	 *
	 * @param name the name to strip. Must not be {@literal null}.
	 * @return {@code name} without its illegal characters. Guaranteed to be not {@literal null}.
	 */
	public static String strip(String name) {

		// avoid the regex for the overwhelmingly common case of a plain ASCII name
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && (c < '0' || c > '9') && c != '_') {
				return ILLEGAL_IDENTIFIER_CHARACTER.matcher(name).replaceAll("");
			}
		}

		return name;
	}

	/**
	 * Checks whether {@code path} consists exclusively of identifier characters and the {@code .} used to separate the
	 * segments of a path. Such a path can be included in a SQL statement without risking a SQL injection.
	 *
	 * @param path the path to check. Must not be {@literal null}.
	 * @return {@literal true} if {@code path} contains no illegal character.
	 */
	public static boolean isValidPath(String path) {
		return !CONTAINS_ILLEGAL_PATH_CHARACTER.test(path);
	}
}
