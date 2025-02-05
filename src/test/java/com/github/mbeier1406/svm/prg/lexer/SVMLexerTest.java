package com.github.mbeier1406.svm.prg.lexer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
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

	/** Stellt sicher, dass ein fehlender <i>Value</i> in {@linkplain SVMLexer.Symbol} die korrekten Werte liefert liefert */
	@Test
	public void testeSymbolNullValue() {
		var symbol = new SVMLexer.Symbol(SVMLexer.Token.COMMA, null);
		assertAll("Symbol COMA",
				() -> assertEquals(symbol.getIntValue(), Optional.empty()),
				() -> assertEquals(symbol.getStringValue(), Optional.empty()),
				() -> assertEquals(symbol.value(), null),
				() -> assertEquals(symbol.token(), SVMLexer.Token.COMMA));
	}

	/** Stellt sicher, dass ein <i>Text-Value</i> in {@linkplain SVMLexer.Symbol} die korrekten Werte liefert liefert */
	@Test
	public void testeSymbolTextValue() {
		var symbol = new SVMLexer.Symbol(SVMLexer.Token.CONSTANT, "wert");
		assertAll("Symbol CONSTANT",
				() -> assertEquals(symbol.getStringValue(), Optional.of("wert")),
				() -> assertEquals(symbol.value(), "wert"),
				() -> assertEquals(symbol.token(), SVMLexer.Token.CONSTANT));
	}

	/** Stellt sicher, dass ein <i>Text-Value</i> in {@linkplain SVMLexer.Symbol} als Zahl abgerufen einen definierten Fehler liefert */
	@Test
	public void testeSymbolStringValueGetInt() {
		var ex = assertThrows(NumberFormatException.class, () -> new SVMLexer.Symbol(SVMLexer.Token.CONSTANT, "text").getIntValue());
		assertThat(ex.getLocalizedMessage(), containsString("text"));
	}

	/** Stellt sicher, dass ein <i>Number-Value</i> in {@linkplain SVMLexer.Symbol} die korrekten Werte liefert liefert */
	@Test
	public void testeSymbolNumberValue() {
		var symbol = new SVMLexer.Symbol(SVMLexer.Token.CONSTANT, "1234");
		assertAll("Symbol CONSTANT",
				() -> assertEquals(symbol.getIntValue(), Optional.of(1234)),
				() -> assertEquals(symbol.getStringValue(), Optional.of("1234")),
				() -> assertEquals(symbol.value(), "1234"),
				() -> assertEquals(symbol.token(), SVMLexer.Token.CONSTANT));
	}

	/** Stellt sicher, dass eine fehlende Zeile in {@linkplain SVMLexer.LineInfo} einen definierten Fehler liefert */
	@Test
	public void testeLineInfoStringNull() {
		var ex = assertThrows(NullPointerException.class, () -> new SVMLexer.LineInfo(1, null, new ArrayList<>()));
		assertThat(ex.getLocalizedMessage(), containsString("line"));
	}

	/** Stellt sicher, dass fehlende Symbole in {@linkplain SVMLexer.LineInfo} einen definierten Fehler liefert */
	@Test
	public void testeLineInfoSymbolsNull() {
		var ex = assertThrows(NullPointerException.class, () -> new SVMLexer.LineInfo(1, "line", null));
		assertThat(ex.getLocalizedMessage(), containsString("symbols"));
	}

	/** Stellt sicher, dass eine ungültige Zeilennummer in {@linkplain SVMLexer.LineInfo} einen definierten Fehler liefert */
	@Test
	public void testeLineInfoLineNumber() {
		var ex = assertThrows(IllegalArgumentException.class, () -> new SVMLexer.LineInfo(-1, "line", new ArrayList<>()));
		assertThat(ex.getLocalizedMessage(), containsString("-1"));
	}

	/** Stellt sicher, dass gültige {@linkplain SVMLexer.LineInfo} korrekte Werte liefert */
	@Test
	@SuppressWarnings("serial")
	public void testeLineInfo() {
		var symbols = new ArrayList<SVMLexer.Symbol>() {{
			add(new SVMLexer.Symbol(SVMLexer.Token.CONSTANT, "wert"));
			add(new SVMLexer.Symbol(SVMLexer.Token.COMMA, null));
		}};
		var lineInfo = new SVMLexer.LineInfo(5, "line 5", symbols);
		assertAll("LineInfo",
				() -> assertEquals(lineInfo.symbols(), symbols),
				() -> assertEquals(lineInfo.line(), "line 5"),
				() -> assertEquals(lineInfo.lineNumber(), 5));
	}

}
