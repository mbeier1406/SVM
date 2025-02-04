package com.github.mbeier1406.svm.prg.lexer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain SVMLexer}.
 */
public class SVMLexerTest {

	public static final Logger LOGGER = LogManager.getLogger(SVMLexerTest.class);

	/** Stellt sicher, dass ein fehlender {@linkplain SVMLexer.Token} in {@linkplain SVMLexer.Symbol} einen definierten Fehler liefert */
	@Test
	public void testeSymbolTokenNullValue() {
		var ex = assertThrows(NullPointerException.class, () -> new SVMLexer.Symbol(null, "text"));
		assertThat(ex.getLocalizedMessage(), containsString("token"));
	}

	/** Stellt sicher, dass ein fehlender Wert in {@linkplain SVMLexer.Symbol} einen leeren {@linkplain Optional} als String liefert */
	@Test
	public void testeSymbolValueStringNullValue() {
		assertThat(new SVMLexer.Symbol(SVMLexer.Token.COMMA, null).getStringValue(), equalTo(Optional.empty()));
	}

	/** Stellt sicher, dass ein fehlender Wert in {@linkplain SVMLexer.Symbol} einen leeren {@linkplain Optional} als Integer liefert */
	@Test
	public void testeSymbolValueIntNullValue() {
		assertThat(new SVMLexer.Symbol(SVMLexer.Token.COMMA, null).getIntValue(), equalTo(Optional.empty()));
	}

	/** Stellt sicher, dass ein Text-{@linkplain SVMLexer.Symbol} den korrekten Wert liefert */
	@Test
	public void testeSymbolStringValue() {
		assertThat(new SVMLexer.Symbol(SVMLexer.Token.CONSTANT, "Test").getStringValue(), equalTo(Optional.of("Test")));
	}

	/** Stellt sicher, dass ein fehlender {@linkplain SVMLexer.Token} in {@linkplain SVMLexer.Symbol} einen definierten Fehler liefert */
	@Test
	public void testeSymbolStringValueGetInt() {
		var ex = assertThrows(NumberFormatException.class, () -> new SVMLexer.Symbol(SVMLexer.Token.CONSTANT, "text").getIntValue());
		assertThat(ex.getLocalizedMessage(), containsString("text"));
	}

	@Test
	public void testeSymbolLineNumberNull() {
		
	}

	@Test
	public void testeSymbolLineNullValue() {
		
	}

	@Test
	public void testeSymbolLineLineNumberNull() {
		
	}

}
