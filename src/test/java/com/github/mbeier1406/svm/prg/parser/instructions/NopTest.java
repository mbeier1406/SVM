package com.github.mbeier1406.svm.prg.parser.instructions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

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
 * Tests für die Klasse {@linkplain Nop}.
 */
public class NopTest {

	/** Das zu testende Objekt */
	public Nop nop = new Nop();

	/** Korrekte Programmzeile als Symbolliste */
	@SuppressWarnings("serial")
	public List<Symbol> correctSymbols = new ArrayList<Symbol>() {{ add(SVMLexer.SYM_NOP); }};

	/** Fehlerhafte Programmzeile als Symbolliste: zusätzliche Parameter */
	@SuppressWarnings("serial")
	public List<Symbol> incorrectSymbols = new ArrayList<Symbol>() {{ add(SVMLexer.SYM_NOP); add(SVMLexer.SYM_COMMA); }};

	/** Fehlerhafte Programmzeile als Symbolliste: nicht NOP */
	@SuppressWarnings("serial")
	public List<Symbol> incorrectSymbolsNop = new ArrayList<Symbol>() {{ add(SVMLexer.SYM_COMMA); }};


	/** Bei Null-LineInfo definierte Meldung schreiben */
	@Test
	public void testeNullParameter() {
		var ex = assertThrows(NullPointerException.class, () -> nop.getVirtualInstruction(null, null));
		assertThat(ex.getLocalizedMessage(), containsString("lineInfo"));
	}

	/** Die Instruktion Nop erwartet keine Parameter */
	@Test
	public void testeFalscheZahlParameter() {
		var ex = assertThrows(SVMException.class, () -> nop.getVirtualInstruction(null, new LineInfo(1, "", incorrectSymbols)));
		assertThat(ex.getLocalizedMessage(), containsString("NOP erwartet keine Parameter"));
	}

	/** Die Instruktion Nop wird erwartet */
	@Test
	public void testeFalschesSymbol() {
		var ex = assertThrows(SVMException.class, () -> nop.getVirtualInstruction(null, new LineInfo(1, "", incorrectSymbolsNop)));
		assertThat(ex.getLocalizedMessage(), containsString("NOP erwartet Symbol"));
	}

	/** Testet das Parsen der Instruktion <b>ohne</b> vorangestelltes Label als Sprungmarke */
	@Test
	public void testeOhneLabel() throws SVMException {
		var virtualInstruction = nop.getVirtualInstruction(null, new LineInfo(1, "	nop", correctSymbols));
		assertAll("virtualInstruction",
				() -> assertEquals(virtualInstruction.instruction(), new InstructionDefinition<>(InstructionFactory.NOP, new byte[0], null)),
				() -> assertEquals(virtualInstruction.label(), null),
				() -> assertEquals(virtualInstruction.labelList().length, 0));
	}

	/** Testet das Parsen der Instruktion <b>mit</b> vorangestelltes Label als Sprungmarke */
	@Test
	public void testeMitLabel() throws SVMException {
		var virtualInstruction = nop.getVirtualInstruction(new Symbol(Token.LABEL, "label1"), new LineInfo(1, "	nop", correctSymbols));
		assertAll("virtualInstruction",
				() -> assertEquals(virtualInstruction.instruction(), new InstructionDefinition<>(InstructionFactory.NOP, new byte[0], null)),
				() -> assertEquals(virtualInstruction.label(), new Label(LabelType.INSTRUCTION, "label1")),
				() -> assertEquals(virtualInstruction.labelList().length, 0));
	}

}
