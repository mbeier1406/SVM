package com.github.mbeier1406.svm.prg.lexer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain TokenGroupLexer}.
 */
public class TokenGroupLexerTest {

	public static final Logger LOGGER = LogManager.getLogger(TokenGroupLexerTest.class);

	/** Stellt sicher, dass eine korrekter RegEx zur Erkennung der {@linkplain SVMLexer.TokenPart} geliefert wird */
	@Test
	public void testeTokenTypPattern() {
		String pattern = TokenGroupLexer.getTokenTypePattern();
		LOGGER.trace("pattern={}", pattern);
		assertThat(pattern, not(equalTo(null)));
		Pattern.compile(pattern); // soll keine Exception werfen
	}

}
