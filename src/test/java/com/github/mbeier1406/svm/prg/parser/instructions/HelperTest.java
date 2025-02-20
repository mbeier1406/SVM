package com.github.mbeier1406.svm.prg.parser.instructions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;

/**
 * Tests für die Klasse {@linkplain Helper}.
 */
public class HelperTest {

	/** Stellt sicher, dass für einen gelesenen Label (Symbol) ein korrekter Label für den SVMLoader erzeugt wird */
	@Test
	public void testeLabelErzeugen() {
		Label label = Helper.getLabel(new Symbol(Token.LABEL, "test1"));
		assertAll("label",
				() -> assertEquals(LabelType.INSTRUCTION, label.labelType()),
				() -> assertEquals("test1", label.label()));
	}

	/** Null-Werte sollen keine Exception werfen! */
	@Test
	public void testeNulltesteLabelErzeugen() {
		assertEquals(null, Helper.getLabel(null));
	}

}
