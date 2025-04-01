package com.github.mbeier1406.svm.prg.parser;

import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TOKEN_CODE;
import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TOKEN_DATA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;
import com.github.mbeier1406.svm.prg.SVMProgramShort;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;

/**
 * Tests für die Klasse {@linkplain SectionDataParserShort}.
 */
public class SectionDataParserShortTest {

	public static final Logger LOGGER = LogManager.getLogger(SectionDataParserShortTest.class);

	/** Das zu testende Objekt */
	public final SectionDataParser<Short> sectionDataParser = new SectionDataParserShort();

	/** Das zu erstellende Programm */
	public final SVMProgram<Short> svmProgramm = new SVMProgramShort();

	/** Ein SVM-Programm mit einer Zeile */
	@SuppressWarnings("serial")
	public static final ArrayList<LineInfo> STD_LINE_INFO = new ArrayList<>() {{
		add(new LineInfo(1, "	&code", new ArrayList<Symbol>() {{ add(SYM_TOKEN_CODE); }}));
	}};


	/** Stellt sicher, dass der Startindex >= 0 ist */
	@Test
	public void testeStartIndexKleinerNull() {
		var ex = assertThrows(SVMException.class, () -> sectionDataParser.parse(svmProgramm, STD_LINE_INFO, -1));
		assertThat(ex.getLocalizedMessage(), containsString("Der Startindex muss zwischen 0 und"));
	}

	/** Stellt sicher, dass der Startindex < Länge InfoList ist */
	@Test
	public void testeStartIndexKleinerGroesserGleichInfoList() {
		var ex = assertThrows(SVMException.class, () -> sectionDataParser.parse(svmProgramm, STD_LINE_INFO, 1));
		assertThat(ex.getLocalizedMessage(), containsString("Der Startindex muss zwischen 0 und 0 liegen"));
	}

	/** Stellt sicher, dass bei <b>null</b>-Werten beim Umwandeln von Datendefinitionen extern -> intern ein definierter Fehler erzeugt wird */
	@Test
	public void testeDatenVonExternNachInternUmwandelnNull() {
		var ex = assertThrows(NullPointerException.class, () -> sectionDataParser.getSvmData(null));
		assertThat(ex.getLocalizedMessage(), containsString("data"));
	}

	/** Stellt sicher, dass bei falschen Symbolen beim Umwandeln von Datendefinitionen extern -> intern ein definierter Fehler erzeugt wird */
	@Test
	public void testeDatenVonExternNachInternUmwandelnFalschesSymbol() {
		var ex = assertThrows(IllegalArgumentException.class, () -> sectionDataParser.getSvmData(SVMLexer.SYM_COMMA));
		assertThat(ex.getLocalizedMessage(), containsString("Symbol vom Typ 'DATA' erwartet"));
	}

	/** Stellt sicher, dass das Umwandeln von Datendefinitionen extern -> intern funktioniert */
	@Test
	public void testeDatenVonExternNachInternUmwandeln() {
		assertThat(sectionDataParser.getSvmData(new Symbol(Token.DATA, "abc")), equalTo(new Short[] { (short) 'a', (short) 'b', (short) 'c' }));
	}

	/** Stellt sicher, dass ein fehlendes {@linkplain SVMProgram} einen definierten Fehler liefert */
	@Test
	public void testeSVMProgramNullValue() {
		var ex = assertThrows(NullPointerException.class, () -> sectionDataParser.parse(null, STD_LINE_INFO));
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
		svmProgramm.addData(new SVMProgram.Data<Short>(new SVMProgram.Label(LabelType.DATA, "xyz"), new Short[] {1}, null));
		var ex = assertThrows(SVMException.class, () -> sectionDataParser.parse(svmProgramm, STD_LINE_INFO));
		assertThat(ex.getLocalizedMessage(), containsString("Sektion im SVM-Programm ist nicht leer"));
	}

	/** Stellt sicher, dass das SVM-Programm mit der Datensektion beginnt */
	@Test
	public void testeSVMProgramStartsWithDataSection() {
		var ex = assertThrows(SVMException.class, () -> sectionDataParser.parse(svmProgramm, STD_LINE_INFO));
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
		assertThat(ex.getLocalizedMessage(), containsString("Nach einer Labeldefinition wird eine Datendefinition erwartet"));
	}

	/** Stellt sicher, dass Datendefinitionen korrekt in die SVM übernommen werden */
	@Test
	@SuppressWarnings("serial")
	public void testeDataCorrect() throws SVMException {
		int index = sectionDataParser.parse(svmProgramm, new ArrayList<LineInfo>() {{
			add(new LineInfo(1, "	&data", new ArrayList<Symbol>() {{ add(SYM_TOKEN_DATA); }}));
			add(new LineInfo(2, ".label1", new ArrayList<Symbol>() {{ add(new Symbol(Token.LABEL, "label1")); }}));
			add(new LineInfo(3, "	abc", new ArrayList<Symbol>() {{ add(new Symbol(Token.DATA, "abc")); }}));
			add(new LineInfo(4, ".label2", new ArrayList<Symbol>() {{ add(new Symbol(Token.LABEL, "label2")); }}));
			add(new LineInfo(5, "	123", new ArrayList<Symbol>() {{ add(new Symbol(Token.DATA, "123")); }}));
			add(new LineInfo(6, "	&code", new ArrayList<Symbol>() {{ add(SYM_TOKEN_CODE); }}));
		}});
		assertThat(index, equalTo(5));
		assertThat(svmProgramm.getDataList().size(), equalTo(2));
		assertThat(svmProgramm.getDataList().get(0), equalTo(new Data<Short>(new Label(LabelType.DATA, "label1"), new Short[] { (short) 'a', (short) 'b', (short) 'c' }, null)));
		assertThat(svmProgramm.getDataList().get(1), equalTo(new Data<Short>(new Label(LabelType.DATA, "label2"), new Short[] { (short) '1', (short) '2', (short) '3' }, null)));
	}

}
