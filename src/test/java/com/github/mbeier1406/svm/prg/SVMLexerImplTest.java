package com.github.mbeier1406.svm.prg;

import static com.github.mbeier1406.svm.prg.SVMLexer.SYM_SPACE;
import static com.github.mbeier1406.svm.prg.SVMLexer.SYM_TAB;
import static com.github.mbeier1406.svm.prg.SVMLexer.SYM_TOKEN_DATA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMLexer.Symbol;

/**
 * Tests für die Klasse {@linkplain SVMLexerImpl}.
 */
public class SVMLexerImplTest {

	public static final Logger LOGGER = LogManager.getLogger(SVMLexerImplTest.class);

	/** Die zu scannende Testdatei ist {@value} */
	public static final String PRG = "src/test/resources/com/github/mbeier1406/svm/prg/example.prg";

	/** Das zu testende Objekt */
	public SVMLexer svmLexer = new SVMLexerImpl();

	@Test
	public void testeDateiScannen() throws SVMException {
		svmLexer.scan(PRG);
	}

	@ParameterizedTest
	@MethodSource("getTestdaten")
	public void testeLineScan(String line, final List<Symbol> symbols) throws SVMException {
		var scanedSymbols = ((SVMLexerImpl) svmLexer).scanLine(line);
		assertThat(scanedSymbols, equalTo(symbols));
	}

	@SuppressWarnings("serial")
	public static Stream<Arguments> getTestdaten() {
		return Stream.of(
				Arguments.of("", new ArrayList<Symbol>() {{}}),
				Arguments.of("	", new ArrayList<Symbol>() {{add(SYM_TAB);}}), // Ein TAB
				Arguments.of("  ", new ArrayList<Symbol>() {{}}),
				Arguments.of("	 	 ", new ArrayList<Symbol>() {{add(SYM_TAB);add(SYM_TAB);}}), // Tabs enthalten
				Arguments.of("# xx", new ArrayList<Symbol>() {{}}),
				Arguments.of("   # xx", new ArrayList<Symbol>() {{}}),
				Arguments.of("	&data", new ArrayList<Symbol>() {{add(SYM_TAB);add(SYM_TOKEN_DATA);}})
				);
	}

	@ParameterizedTest
	@MethodSource("getUngueltigeTestdaten")
	public void testeUnguetliteLineScan(String line, Class<?extends Exception> ex, String msg) {
		Exception e = assertThrows(ex, () -> ((SVMLexerImpl) svmLexer).scanLine(line));
		assertThat(e.getLocalizedMessage(), containsString(msg));
	}

	public static Stream<Arguments> getUngueltigeTestdaten() {
		return Stream.of(
				Arguments.of("	&abc", SVMException.class, "muss eine Sektion (data/code) folgen"),
				Arguments.of("	yy ? xx", SVMException.class, "Ungültige(s) Token: '?'"),
				Arguments.of(null, NullPointerException.class, "line"));
	}

	@Test
	public void testeScanner() {
		String patternString="(?<DOT>\\\\.)|(?<TAB>\\\\t)|(?<HASH>#)|(?<SPACE> )|(?<COMMA>,)|(?<DOLLAR>\\\\$)|(?<PERCENT>%)|(?<AMPERSAND>&)|(?<NUMBER>\\d+)|(?<STRING>[A-Za-z][A-Za-z0-9]*)";
		Pattern pattern = Pattern.compile(patternString);
		Scanner scanner = new Scanner("	yy ? xx");
		while ( scanner.hasNext() ) {
			String token = scanner.next();
			LOGGER.trace("token={}", token);
		}
		scanner.close();
	}
	
}
