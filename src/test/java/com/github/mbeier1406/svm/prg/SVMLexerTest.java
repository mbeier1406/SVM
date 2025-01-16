package com.github.mbeier1406.svm.prg;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.prg.SVMLexer.TokenType;

/**
 * Tests für die Klasse {@linkplain SVMLexer}.
 */
public class SVMLexerTest {

	public static final Logger LOGGER = LogManager.getLogger(SVMLexerTest.class);

	/** Einzelnes LF muss als STRING erkannt werden */
	@Test
	public void testeLineFeed() {
		assertThat(TokenType.getToken('\n'), equalTo(TokenType.STRING));
	}

	@Test
	public void testeTokenTypPattern() {
		String pattern = SVMLexer.getTokenTypePattern();
		LOGGER.trace("pattern={}", pattern);
	}
}
