package com.github.mbeier1406.svm.prg.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TOKEN_DATA;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TOKEN_CODE;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;
import com.github.mbeier1406.svm.prg.SVMProgramShort;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;

/**
 * Tests für die Klasse {@linkplain SectionDataParserImpl}.
 */
public class SectionDataParserImplTest {

	public static final Logger LOGGER = LogManager.getLogger(SectionDataParserImplTest.class);

	/** Das zu testende Objekt */
	public final SectionDataParser sectionDataParser = new SectionDataParserImpl();

	/** Das zu erstellende Programm */
	public final SVMProgram<Short> svmProgramm = new SVMProgramShort();


	/** Stellt sicher, dass ein fehlende {@linkplain SVMProgram} einen definierten Fehler liefert */
	@Test
	public void testeSVMProgramNullValue() {
		var ex = assertThrows(NullPointerException.class, () -> sectionDataParser.parse(null, new ArrayList<>()));
		assertThat(ex.getLocalizedMessage(), containsString("svmProgram"));
	}

	/** Stellt sicher, dass eine fehlende Symbolliste einen definierten Fehler liefert */
	@Test
	public void testeLineInfoListNullValue() {
		var ex = assertThrows(NullPointerException.class, () -> sectionDataParser.parse(svmProgramm, null));
		assertThat(ex.getLocalizedMessage(), containsString("lineInfoList"));
	}

	/** Stellt sicher, dass ein definierten Fehler erzeugt wird, wenn das Programm bereits Daten enthält */
	@Test
	public void testeSVMProgramDataSectionNotEmpty() {
		svmProgramm.addData(new SVMProgram.Data<Short>(new SVMProgram.Label(LabelType.DATA, "xyz"), new Short[] {1}));
		var ex = assertThrows(SVMException.class, () -> sectionDataParser.parse(svmProgramm, new ArrayList<>()));
		assertThat(ex.getLocalizedMessage(), containsString("enthält bereits Daten"));
	}

	/** Stellt sicher, dass das SVM-Programm mit der Datensektion beginnt */
	@Test
	public void testeSVMProgramStartsWithDataSection() {
		@SuppressWarnings("serial")
		var ex = assertThrows(SVMException.class, () -> sectionDataParser.parse(svmProgramm, new ArrayList<LineInfo>() {{
			add(new LineInfo(1, "xyz", new ArrayList<Symbol>() {{ add(SYM_TOKEN_CODE); }}));
		}}));
		assertThat(ex.getLocalizedMessage(), containsString("Es wird die Datensektion erwartet"));
	}

	/** Stellt sicher, dass das SVM-Programm mit <u>ausschließlich</u> der Datensektion beginnt */
	@Test
	public void testeStartsWithOnlyDataSection() {
		@SuppressWarnings("serial")
		var ex = assertThrows(SVMException.class, () -> sectionDataParser.parse(svmProgramm, new ArrayList<LineInfo>() {{
			add(new LineInfo(1, "xyz", new ArrayList<Symbol>() {{ add(SYM_TOKEN_DATA); add(SYM_TOKEN_CODE); }}));
		}}));
		assertThat(ex.getLocalizedMessage(), containsString("Es wird die Datensektion erwartet"));
	}

	/** Stellt sicher, dass das SVM-Programm mit leerer Datensektion korrekt ist */
	@Test
	public void testeEmptyDataSection() throws SVMException {
		@SuppressWarnings("serial")
		int index = sectionDataParser.parse(svmProgramm, new ArrayList<LineInfo>() {{
			add(new LineInfo(1, "	&data", new ArrayList<Symbol>() {{ add(SYM_TOKEN_DATA); }}));
			add(new LineInfo(2, "	&code", new ArrayList<Symbol>() {{ add(SYM_TOKEN_CODE); }}));
		}});
		assertThat(index, equalTo(1)); // Ab Index eins weiterparsen...
	}

	/** Stellt sicher, dass nach einem Label eine Konstante angegeben wird (Programm darf hier nicht enden!) */
	@Test
	public void testeConstantAfterLabel1() {
		@SuppressWarnings("serial")
		var ex = assertThrows(SVMException.class, () -> sectionDataParser.parse(svmProgramm, new ArrayList<LineInfo>() {{
			add(new LineInfo(1, "	&data", new ArrayList<Symbol>() {{ add(SYM_TOKEN_DATA); }}));
			add(new LineInfo(2, ".xyz", new ArrayList<Symbol>() {{ add(new Symbol(Token.LABEL, "xyz")); }}));
		}}));
		assertThat(ex.getLocalizedMessage(), containsString("Nach einer Labeldefinition darf das Programm nicht enden"));
	}

	/** Stellt sicher, dass nach einem Label eine Konstante angegeben wird */
	@Test
	public void testeConstantAfterLabel2() {
		@SuppressWarnings("serial")
		var ex = assertThrows(SVMException.class, () -> sectionDataParser.parse(svmProgramm, new ArrayList<LineInfo>() {{
			add(new LineInfo(1, "	&data", new ArrayList<Symbol>() {{ add(SYM_TOKEN_DATA); }}));
			add(new LineInfo(2, ".xyz", new ArrayList<Symbol>() {{ add(new Symbol(Token.LABEL, "xyz")); }}));
			add(new LineInfo(3, "	&code", new ArrayList<Symbol>() {{ add(SYM_TOKEN_CODE); }}));
		}}));
		assertThat(ex.getLocalizedMessage(), containsString("Nach einer Labeldefinition wird eine Konstante erwartet"));
	}

}
