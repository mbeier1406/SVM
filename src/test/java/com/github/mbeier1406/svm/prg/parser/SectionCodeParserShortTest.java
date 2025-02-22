package com.github.mbeier1406.svm.prg.parser;

import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_COMMA;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_LEFTPAR;
import static com.github.mbeier1406.svm.prg.parser.SectionDataParserShortTest.STD_LINE_INFO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgramShort;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

/**
 * Tests für die Klasse {@linkplain SectionCodeParserShort}.
 */
public class SectionCodeParserShortTest {

	public static final Logger LOGGER = LogManager.getLogger(SectionCodeParserShortTest.class);

	/** Das zu testende Objekt */
	public final SectionCodeParser<Short> sectionCodeParser = new SectionCodeParserShort();

	/** Das zu erstellende Programm */
	public final SVMProgram<Short> svmProgramm = new SVMProgramShort();


	/** Stellt sicher, dass der Startindex >= 0 ist */
	@Test
	public void testeStartIndexKleinerNull() {
		var ex = assertThrows(SVMException.class, () -> sectionCodeParser.parse(svmProgramm, STD_LINE_INFO, -1));
		assertThat(ex.getLocalizedMessage(), containsString("Der Startindex muss zwischen 0 und"));
	}

	/** Stellt sicher, dass der Startindex < Länge InfoList ist */
	@Test
	public void testeStartIndexKleinerGroesserGleichInfoList() {
		var ex = assertThrows(SVMException.class, () -> sectionCodeParser.parse(svmProgramm, STD_LINE_INFO, 1));
		assertThat(ex.getLocalizedMessage(), containsString("Der Startindex muss zwischen 0 und 0 liegen"));
	}


	/** Stellt sicher, dass ein definierter Fehler erzeugt wird, wenn eine Code-zeile nicht mit einer Instruktion beginnt */
	@Test
	public void testeCodezeileBeginntNichtMitInstruktion() {
		@SuppressWarnings("serial")
		final ArrayList<LineInfo> lineInfoList = new ArrayList<>() {{
			add(new LineInfo(1, "	, (", new ArrayList<Symbol>() {{ add(SYM_COMMA); add(SYM_LEFTPAR); }}));
		}};
		var ex = assertThrows(SVMException.class, () -> sectionCodeParser.parse(svmProgramm, lineInfoList, 0));
		assertThat(ex.getLocalizedMessage(), containsString("Es wird die Datensektion erwartet"));
	}

}
