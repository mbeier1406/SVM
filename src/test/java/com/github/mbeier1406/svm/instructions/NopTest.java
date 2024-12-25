package com.github.mbeier1406.svm.instructions;

import static com.github.mbeier1406.svm.instructions.InstructionFactory.INSTRUCTIONS;
import static com.github.mbeier1406.svm.instructions.InstructionInterface.Codes.NOP;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;

/**
 * Tests für die Klasse {@linkplain Nop}.
 */
public class NopTest extends TestBase {

	/** Instruktion setzen */
	@BeforeEach
	public void init() {
		this.instruction = INSTRUCTIONS.get(NOP.getCode());
	}

	/** Korrekter Aufruf erwartet 0 als Return-Code */
	@Test
	public void testeKorrektenParameter() throws SVMException {
		int code = this.instruction.execute(new byte[0]);
		assertThat(code, equalTo(0));
	}

	/** Aufruf mit zu vielen Parametern löst einen Fehler aus. */
	@Test
	public void testeFehlerhaftenParameter() throws SVMException {
		var ex = assertThrows(SVMException.class, () -> this.instruction.execute(new byte[1]));
		assertThat(ex.getLocalizedMessage(), equalTo("'Nop' erwartet 0 Parameter; erhalten '[0]'!"));
	}

	/** Stellt sicher, dass die Instruktion den korrekten Code liefert  */
	@Test
	public void testeGetCode() throws SVMException {
		byte erwarteterCode = 0x1;
		assertThat(this.instruction.getClass().getAnnotation(Instruction.class).code(), equalTo(erwarteterCode));
		assertThat(this.instruction.getCode(), equalTo(erwarteterCode));
	}

}
