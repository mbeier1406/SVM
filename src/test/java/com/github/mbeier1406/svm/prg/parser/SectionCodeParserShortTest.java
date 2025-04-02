package com.github.mbeier1406.svm.prg.parser;

import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_COMMA;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_INT;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_LEFTPAR;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TOKEN_CODE;
import static com.github.mbeier1406.svm.prg.parser.SectionDataParserShortTest.STD_LINE_INFO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionFactory;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.prg.SVMProgramShort;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;

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

	/** Erzeugt und überprüft den generierten Code für ein einzeiliges Programm {@code int $1} */
	@Test
	public void testeProgramm() throws SVMException {
		@SuppressWarnings("serial")
		final ArrayList<LineInfo> lineInfoList = new ArrayList<>() {{
			add(new LineInfo(1, "	&code", new ArrayList<Symbol>() {{ add(SYM_TOKEN_CODE); }}));
			add(new LineInfo(2, "	int $1", new ArrayList<Symbol>() {{ add(SYM_INT); add(new Symbol(Token.CONSTANT, "1")); }}));
		}};
		sectionCodeParser.parse(svmProgramm, lineInfoList, 0);
		assertThat(svmProgramm.getInstructionList().size(), equalTo(1));
		var virtualInstruction = svmProgramm.getInstructionList().get(0);
		assertThat(virtualInstruction.label(), equalTo(null));
		assertThat(virtualInstruction.labelList(), equalTo(new Label[] {null}));
		var instruction = virtualInstruction.instruction();
		assertThat(instruction.lenInWords(), equalTo(null));
		assertThat(instruction.instruction(), equalTo(InstructionFactory.INT));
		assertThat(instruction.params(), equalTo(new byte[] { 0x1 }));
	}

	/** Stellt sicher, dass die {@linkplain VirtualInstruction}-Items die Debugging-Informationen enthalten */
	@Test
	public void testeProgrammMitDebugging() throws SVMException {
		this.sectionCodeParser.setDebugging(true);
		testeProgramm();
		assertThat(this.svmProgramm.getInstructionList().stream().map(VirtualInstruction::lineInfo).peek(LOGGER::info).filter(Objects::nonNull).count(), equalTo(1L));
	}

}
