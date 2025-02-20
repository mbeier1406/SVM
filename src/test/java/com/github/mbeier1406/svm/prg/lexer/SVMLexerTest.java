package com.github.mbeier1406.svm.prg.lexer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;

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

	/** Wenn der {@linkplain Token} keinen Parameter erfordert: kein Fehler bei <b>null</b>-Value */
	@Test
	public void testeSymbolParameterValueNullOk() {
		Arrays.stream(SVMLexer.Token.values())
			.filter(t -> !t.needsParameter())
			.map(t -> new SVMLexer.Symbol(t, null))
			.peek(LOGGER::info)
			.count(); // Damit der Stream ausgeführt wird
	}

	/** Wenn der {@linkplain Token} einen Parameter erfordert: kein Fehler bei Value */
	@Test
	public void testeSymbolParameterValueNotNullOk() {
		Arrays.stream(SVMLexer.Token.values())
			.filter(t -> t.needsParameter())
			.map(t -> new SVMLexer.Symbol(t, "123"))
			.peek(LOGGER::info)
			.count(); // Damit der Stream ausgeführt wird
	}

	/** Wenn der {@linkplain Token} einen Parameter erfordert: Fehler bei <b>null</b>-Value */
	@Test
	public void testeSymbolParameterValueNullNotOk() {
		Arrays.stream(SVMLexer.Token.values())
			.filter(t -> t.needsParameter())
			.forEach(t -> {
				var ex = assertThrows(NullPointerException.class, () -> new SVMLexer.Symbol(t, null));
				assertThat(ex.getLocalizedMessage(), containsString("value"));
			});
	}

	/** Wenn der {@linkplain Token} keinen Parameter erfordert: Fehler bei Value */
	@Test
	public void testeSymbolParameterValueNotNullNotOk() {
		Arrays.stream(SVMLexer.Token.values())
			.filter(t -> !t.needsParameter())
			.forEach(t -> {
				var ex = assertThrows(IllegalArgumentException.class, () -> new SVMLexer.Symbol(t, "123"));
				assertThat(ex.getLocalizedMessage(), containsString("erwartet keinen Parameter"));
			});
	}

	/** Stellt sicher, dass eine fehlende Zeile in {@linkplain SVMLexer.LineInfo} einen definierten Fehler liefert */
	@Test
	public void testeLineInfoStringNull() {
		@SuppressWarnings("serial")
		var ex = assertThrows(NullPointerException.class, () -> new SVMLexer.LineInfo(1, null, new ArrayList<>() {{
			add(new SVMLexer.Symbol(SVMLexer.Token.COMMA, null));
		}}));
		assertThat(ex.getLocalizedMessage(), containsString("line"));
	}

	/** Stellt sicher, dass fehlende Symbole in {@linkplain SVMLexer.LineInfo} einen definierten Fehler liefert */
	@Test
	public void testeLineInfoSymbolsNull() {
		var ex = assertThrows(NullPointerException.class, () -> new SVMLexer.LineInfo(1, "line", null));
		assertThat(ex.getLocalizedMessage(), containsString("symbols"));
	}

	/** Stellt sicher, dass die Symbolliste in {@linkplain SVMLexer.LineInfo} mindestens einen Wert hat */
	@Test
	public void testeLineInfoSymbolsEmpty() {
		var ex = assertThrows(IllegalArgumentException.class, () -> new SVMLexer.LineInfo(1, "line", new ArrayList<SVMLexer.Symbol>()));
		assertThat(ex.getLocalizedMessage(), containsString("Leere Symbolliste"));
	}

	/** Stellt sicher, dass eine ungültige Zeilennummer in {@linkplain SVMLexer.LineInfo} einen definierten Fehler liefert */
	@Test
	public void testeLineInfoLineNumber() {
		@SuppressWarnings("serial")
		var ex = assertThrows(IllegalArgumentException.class, () -> new SVMLexer.LineInfo(-1, "line", new ArrayList<>() {{
			add(new SVMLexer.Symbol(SVMLexer.Token.COMMA, null));
		}}));
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
