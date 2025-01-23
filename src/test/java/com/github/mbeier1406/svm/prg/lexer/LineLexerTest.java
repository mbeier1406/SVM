package com.github.mbeier1406.svm.prg.lexer;

import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TAB;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TOKEN_DATA;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TOKEN_CODE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

/**
 * Tests für die Klasse {@linkplain LineLexer}.
 */
public class LineLexerTest {

	public static final Logger LOGGER = LogManager.getLogger(LineLexerTest.class);

	/** Testet die positiven Fälle aus {@linkplain #getTestdaten()} */
	@ParameterizedTest
	@MethodSource("getTestdaten")
	public void testeLineScan(String line, final List<Symbol> symbols) throws SVMException {
		List<Symbol> scanedSymbols = new ArrayList<>();
		LineLexer.LINE_SCANNER.scanLine(scanedSymbols, line);
		assertThat(scanedSymbols, equalTo(symbols));
	}

	/** Liefert die gültigen Testdaten */
	@SuppressWarnings("serial")
	public static Stream<Arguments> getTestdaten() {
		return Stream.of(
				Arguments.of("", new ArrayList<Symbol>() {{}}),
				Arguments.of("	", new ArrayList<Symbol>() {{add(SYM_TAB);}}), // Ein TAB
				Arguments.of("  ", new ArrayList<Symbol>() {{}}),
				Arguments.of("	 	 ", new ArrayList<Symbol>() {{add(SYM_TAB);add(SYM_TAB);}}), // Tabs enthalten
				Arguments.of("# xx", new ArrayList<Symbol>() {{}}),
				Arguments.of("   # xx", new ArrayList<Symbol>() {{}}),
				Arguments.of("	&data", new ArrayList<Symbol>() {{add(SYM_TAB);add(SYM_TOKEN_DATA);}}),
				Arguments.of("	&code", new ArrayList<Symbol>() {{add(SYM_TAB);add(SYM_TOKEN_CODE);}})
				);
	}

	/** Testet die negativen Fälle aus {@linkplain #getUngueltigeTestdaten()} */
	@ParameterizedTest
	@MethodSource("getUngueltigeTestdaten")
	public void testeUnguetliteLineScan(String line, Class<?extends Exception> ex, String msg) {
		Exception e = assertThrows(ex, () -> LineLexer.LINE_SCANNER.scanLine(new ArrayList<>(), line));
		assertThat(e.getLocalizedMessage(), containsString(msg));
	}

	/** Liefert die ungültigen Testdaten */
	public static Stream<Arguments> getUngueltigeTestdaten() {
		return Stream.of(
				Arguments.of("	&abc", SVMException.class, "muss eine Sektion (data/code) folgen"),
				Arguments.of("	&code ? xx", SVMException.class, "Ungültige(s) Token: '?'"),
				Arguments.of(null, SVMException.class, "line"));
	}

}
