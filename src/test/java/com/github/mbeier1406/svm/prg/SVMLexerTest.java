package com.github.mbeier1406.svm.prg;

import static com.github.mbeier1406.svm.prg.SVMLexer.TokenType;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain SVMLexer}.
 */
public class SVMLexerTest {

	public static final Logger LOGGER = LogManager.getLogger(SVMLexerTest.class);

	/** Stellt sicher, dass für das erste Zeichen eines jeden Tokens der korrekte Typ geliefert wird */
	@Test
	public void testeTokenTypess() {
		Arrays.stream(TokenType.values()).peek(LOGGER::trace).forEach(t -> {
			var token = TokenType.getToken(t.getText().charAt(0));
			assertThat(token, equalTo(t));
		});
	}

	/** Einzelnes LF muss als STRING erkannt werden */
	@Test
	public void testeLineFeed() {
		assertThat(TokenType.getToken('\n'), equalTo(TokenType.STRING));
	}

}
