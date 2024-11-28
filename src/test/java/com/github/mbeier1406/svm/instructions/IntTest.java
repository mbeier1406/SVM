package com.github.mbeier1406.svm.instructions;

import static com.github.mbeier1406.svm.instructions.InstructionFactory.INSTRUCTIONS;
import static com.github.mbeier1406.svm.instructions.InstructionInterface.Codes.INT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;

/**
 * Tests für die Klasse {@linkplain Int}.
 */
public class IntTest extends TestBase {

	/** Instruktion setzen */
	@BeforeEach
	public void init() {
		this.instruction = INSTRUCTIONS.get(INT.getCode());
	}

	/** Testet den Syscall Exit */
	@Test
	public void TesteIntSyscallExit() throws SVMException {
		assertThat(TestBase.stopFlag, equalTo(0));
		assertThat(TestBase.returnCode, equalTo(0));
		this.alu.setRegisterValue(0, (short) 0x1); // Funktion Exit
		this.alu.setRegisterValue(1, (short) 0x3); // ReturnCode 3
		this.instruction.execute(new byte[] { (byte) 0x1 /* Modul Syscalls */ });
		assertThat(TestBase.stopFlag, equalTo(1));
		assertThat(TestBase.returnCode, equalTo(3));
	}

	/** Aufruf mit zu vielen/wenigen Parametern löst einen Fehler aus. */
	@Test
	public void testeFehlerhaftenParameter() throws SVMException {
		var ex = assertThrows(SVMException.class, () -> this.instruction.execute(new byte[2]));
		assertThat(ex.getLocalizedMessage(), equalTo("'Int' erwartet 3 Parameter; erhalten '[0, 0]'!"));
		ex = assertThrows(SVMException.class, () -> this.instruction.execute(new byte[4]));
		assertThat(ex.getLocalizedMessage(), equalTo("'Int' erwartet 3 Parameter; erhalten '[0, 0, 0, 0]'!"));
	}

}
