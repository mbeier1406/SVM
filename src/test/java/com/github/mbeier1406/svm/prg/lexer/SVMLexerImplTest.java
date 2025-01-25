package com.github.mbeier1406.svm.prg.lexer;

import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TOKEN_DATA;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token.DATA;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token.LABEL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

/**
 * Tests für die Klasse {@linkplain SVMLexerImpl}.
 */
public class SVMLexerImplTest {

	public static final Logger LOGGER = LogManager.getLogger(SVMLexerImplTest.class);

	/** Die zu scannende Testdatei ist {@value} */
	public static final String PRG = "src/test/resources/com/github/mbeier1406/svm/prg/example.svm";

	/** Das zu testende Objekt */
	public SVMLexer svmLexer = new SVMLexerImpl();

	@ParameterizedTest
	@MethodSource("getTestdaten")
	public void testeTextScannen(String prg, final List<List<SVMLexer.Symbol>> symbols) throws SVMException {
		LOGGER.trace("prg={}", prg);
		var scanedSymbols = svmLexer.scan(prg);
		LOGGER.trace("scanedSymbols={}", scanedSymbols);
		assertThat(scanedSymbols, equalTo(symbols));
	}

	/** Liefert die Testdaten für {@link #testeTextScannen()} */
	@SuppressWarnings("serial")
	public static List<Arguments> getTestdaten() {
		return List.of(
				Arguments.of(
						"""
	&data
.label1
	xyz
						""",
						List.of(
								new ArrayList<Symbol>() {{add(SYM_TOKEN_DATA);}},
								new ArrayList<Symbol>() {{add(new Symbol(LABEL, "label1"));}},
								new ArrayList<Symbol>() {{add(new Symbol(DATA, "xyz"));}}
							)
						),
				Arguments.of(
						"""
\t&data
						""",
						List.of(new ArrayList<Symbol>() {{add(SYM_TOKEN_DATA);}})
						),
				Arguments.of(
						"""

# Leeres Programm

						""",
						new ArrayList<List<Symbol>>()
						),
				Arguments.of(
						"""
						""",
						new ArrayList<List<Symbol>>()
						)
				);
	}


	@Test
	public void testeDateiScannen() throws SVMException {
//		svmLexer.scan(PRG);
	}
	
}
