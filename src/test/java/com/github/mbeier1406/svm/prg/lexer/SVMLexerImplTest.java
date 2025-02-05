package com.github.mbeier1406.svm.prg.lexer;

import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_COMMA;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_LEFTPAR;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_RIGHTPAR;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TOKEN_DATA;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token.DATA;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token.LABEL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;

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
	public void testeTextScannen(String prg, final List<LineInfo> lineInfos) throws SVMException {
		LOGGER.trace("prg={}", prg);
		var scanedLineInfos = svmLexer.scan(prg);
		LOGGER.trace("scanedLineInfos ={}", scanedLineInfos );
		assertThat(scanedLineInfos , equalTo(lineInfos));
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
								new LineInfo(1, "	&data", new ArrayList<Symbol>() {{add(SYM_TOKEN_DATA);}}),
								new LineInfo(2, ".label1", new ArrayList<Symbol>() {{add(new Symbol(LABEL, "label1"));}}),
								new LineInfo(3, "	xyz", new ArrayList<Symbol>() {{add(new Symbol(DATA, "xyz"));}})
							)
						),
				Arguments.of(
						"""
# Test
	&data
						""",
						List.of(new LineInfo(2, "	&data", new ArrayList<Symbol>() {{add(SYM_TOKEN_DATA);}}))
						),
				Arguments.of(
						"""

# Leeres Programm

						""",
						new ArrayList<LineInfo>()
						),
				Arguments.of(
						"""
						""",
						new ArrayList<LineInfo>()
						)
				);
	}

	/** Lexer auf {@value #PRG}: prüfen, ob des Programm korrekt eingelesen wurde */
	@Test
	@SuppressWarnings("serial")
	public void testeDateiScannen() throws SVMException {
		List<LineInfo> lineInfo = svmLexer.scan(new File(PRG));
		LOGGER.trace("lineInfo={}", lineInfo);
		assertThat(lineInfo.size(), equalTo(16));
		assertThat(lineInfo.get(0), equalTo(new LineInfo(4, "	&data", new ArrayList<Symbol>() {{add(SYM_TOKEN_DATA);}})));
		assertThat(lineInfo.get(1), equalTo(new LineInfo(5, ".text1", new ArrayList<Symbol>() {{add(new Symbol(Token.LABEL, "text1"));}})));
//		assertThat(lineInfo.get(2), equalTo(new ArrayList<Symbol>() {{add(new Symbol(Token.DATA, "abc\\n"));}}));
//		assertThat(lineInfo.get(6), equalTo(new ArrayList<Symbol>() {{add(new Symbol(Token.CODE, "nop"));}}));
//		assertThat(lineInfo.get(7), equalTo(new ArrayList<Symbol>() {{
//			add(new Symbol(Token.CODE, "mov"));add(new Symbol(Token.CONSTANT, "2"));add(SYM_COMMA);add(new Symbol(Token.REGISTER, "0"));
//		}}));
//		assertThat(lineInfo.get(9), equalTo(new ArrayList<Symbol>() {{
//			add(new Symbol(Token.CODE, "mov"));add(SYM_LEFTPAR);add(new Symbol(Token.LABEL_REF, "text2"));add(SYM_RIGHTPAR);
//			add(SYM_COMMA);add(new Symbol(Token.REGISTER, "2"));
//		}}));
//		assertThat(lineInfo.get(10), equalTo(new ArrayList<Symbol>() {{
//			add(new Symbol(Token.CODE, "mov"));add(new Symbol(Token.FUNCTION, "len"));add(SYM_LEFTPAR);add(new Symbol(Token.LABEL_REF, "text2"));
//			add(SYM_RIGHTPAR);add(SYM_COMMA);add(new Symbol(Token.REGISTER, "3"));
//		}}));
//		assertThat(lineInfo.get(15), equalTo(new ArrayList<Symbol>() {{add(new Symbol(Token.CODE, "int"));add(new Symbol(Token.CONSTANT, "1"));}}));
	}
	
}
