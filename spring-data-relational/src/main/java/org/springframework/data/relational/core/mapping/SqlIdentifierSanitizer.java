package org.springframework.data.relational.core.mapping;

import org.springframework.data.relational.core.sql.SqlIdentifiers;
import org.springframework.util.Assert;

/**
 * Functional interface to sanitize SQL identifiers for SQL usage. Useful to guard SpEL expression results.
 *
 * @author Kurt Niemi
 * @author Mark Paluch
 * @since 3.2
 * @see RelationalMappingContext#setSqlIdentifierSanitizer(SqlIdentifierSanitizer)
 */
@FunctionalInterface
public interface SqlIdentifierSanitizer {

	/**
	 * A sanitizer to allow letters, combining marks, decimal digits and {@code _} only. All other characters are removed
	 * silently. Rejects names that don't contain a single legal character, since those would render as empty
	 * identifiers.
	 *
	 * @return
	 */
	static SqlIdentifierSanitizer words() {

		return name -> {

			Assert.notNull(name, "Input to sanitize must not be null");

			String sanitized = SqlIdentifiers.strip(name);

			Assert.hasText(sanitized, () -> "Sanitizing %s resulted in an empty identifier".formatted(name));

			return sanitized;
		};
	}

	/**
	 * Sanitize a SQL identifier to either remove unwanted character sequences or to throw an exception.
	 *
	 * @param sqlIdentifier the identifier name.
	 * @return sanitized SQL identifier.
	 */
	String sanitize(String sqlIdentifier);
}
