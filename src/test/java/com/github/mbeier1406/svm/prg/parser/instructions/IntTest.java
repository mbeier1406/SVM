package com.github.mbeier1406.svm.prg.parser.instructions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.instructions.InstructionFactory;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;

/**
 * Tests für die Klasse {@linkplain Int}.
 */
public class IntTest {

	/** Das zu testende Objekt */
	public Int intr = new Int();

	/** Die Konstante "1" als Ergebnis der lexikalischen Analyse */
	public SVMLexer.Symbol const1 = new SVMLexer.Symbol(Token.CONSTANT, "1");

	/** Korrekte Programmzeile als Symbolliste; {@code int $1} */
	@SuppressWarnings("serial")
	public List<Symbol> correctSymbols = new ArrayList<Symbol>() {{ add(SVMLexer.SYM_INT); add(const1); }};

	/** Fehlerhafte Programmzeile als Symbolliste: zusätzliche Parameter */
	@SuppressWarnings("serial")
	public List<Symbol> incorrectSymbols1 = new ArrayList<Symbol>() {{ add(SVMLexer.SYM_INT); add(const1); add(SVMLexer.SYM_COMMA); }};

	/** Fehlerhafte Programmzeile als Symbolliste: fehlender Parameter */
	@SuppressWarnings("serial")
	public List<Symbol> incorrectSymbols2 = new ArrayList<Symbol>() {{ add(SVMLexer.SYM_INT); }};

	/** Fehlerhafte Programmzeile als Symbolliste: fehlerhafter Parameter */
	@SuppressWarnings("serial")
	public List<Symbol> incorrectSymbols3 = new ArrayList<Symbol>() {{ add(SVMLexer.SYM_INT); add(SVMLexer.SYM_COMMA); }};

	/** Fehlerhafte Programmzeile als Symbolliste: nicht INT */
	@SuppressWarnings("serial")
	public List<Symbol> incorrectSymbolsInt = new ArrayList<Symbol>() {{ add(SVMLexer.SYM_COMMA); add(const1); }};


	/** Bei Null-LineInfo definierte Meldung schreiben */
	@Test
	public void testeNullParameter() {
		var ex = assertThrows(NullPointerException.class, () -> intr.getVirtualInstruction(null, null, null));
		assertThat(ex.getLocalizedMessage(), containsString("lineInfo"));
	}

	/** Die Instruktion Int erwartet einen Parameter */
	@Test
	public void testeFalscheZahlParameter() {
		Stream.of(incorrectSymbols1, incorrectSymbols2).forEach(list -> {
			var ex = assertThrows(SVMException.class, () -> intr.getVirtualInstruction(null, new LineInfo(1, "", list), null));
			assertThat(ex.getLocalizedMessage(), containsString("INT erwartet einen Parameter"));
		});
	}

	/** Die Instruktion Int erwartet einen Number-Parameter */
	@Test
	public void testeKeineZahlParameter() {
		var ex = assertThrows(SVMException.class, () -> intr.getVirtualInstruction(null, new LineInfo(1, "", incorrectSymbols3), null));
		assertThat(ex.getLocalizedMessage(), containsString("INT erwartet Number-Parameter"));
	}

	/** Die Instruktion Int wird erwartet */
	@Test
	public void testeFalschesSymbol() {
		var ex = assertThrows(SVMException.class, () -> intr.getVirtualInstruction(null, new LineInfo(1, "", incorrectSymbolsInt), null));
		assertThat(ex.getLocalizedMessage(), containsString("INT erwartet Symbol"));
	}

	/** Testet das Parsen der Instruktion <b>ohne</b> vorangestelltes Label als Sprungmarke */
	@Test
	public void testeOhneLabel() throws SVMException {
		var virtualInstruction = intr.getVirtualInstruction(null, new LineInfo(1, "	int $1", correctSymbols), null);
		assertAll("virtualInstruction",
				() -> assertEquals(virtualInstruction.instruction(), new InstructionDefinition<>(InstructionFactory.INT, new byte[]{(byte) 1}, null)),
				() -> assertEquals(virtualInstruction.label(), null),
				() -> assertEquals(virtualInstruction.labelList().length, 1));
	}

	/** Testet das Parsen der Instruktion <b>mit</b> vorangestelltes Label als Sprungmarke */
	@Test
	@SuppressWarnings("serial")
	public void testeMitLabel() throws SVMException {
		var virtualInstruction = intr.getVirtualInstruction(new Symbol(Token.LABEL, "label1"), new LineInfo(1, "	int $1", correctSymbols), null);
		assertAll("virtualInstruction",
				() -> assertEquals(virtualInstruction.instruction(), new InstructionDefinition<>(InstructionFactory.INT, new byte[]{(byte) 1}, null)),
				() -> assertEquals(virtualInstruction.label(), new Label(LabelType.INSTRUCTION, "label1")),
				() -> assertIterableEquals(Arrays.asList(virtualInstruction.labelList()), new ArrayList<Label>(){{add(null);}}));
	}

}
