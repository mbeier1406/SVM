package com.github.mbeier1406.svm.instructions;

import static com.github.mbeier1406.svm.instructions.InstructionFactory.INSTRUCTIONS;
import static com.github.mbeier1406.svm.instructions.InstructionInterface.Codes.INT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.ALUMock;
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
		assertThat(ALUMock.stopFlag, equalTo(0));
		assertThat(ALUMock.returnCode, equalTo(0));
		this.alu.setRegisterValue(0, (short) 0x1); // Funktion Exit
		this.alu.setRegisterValue(1, (short) 0x3); // ReturnCode 3
		this.instruction.execute(new byte[] { (byte) 0x1 /* Modul Syscalls */ });
		assertThat(ALUMock.stopFlag, equalTo(1));
		assertThat(ALUMock.returnCode, equalTo(3));
	}

	/** Testet den Syscall IO mit Ausgabe auf den Bildschirm */
	@Test
	public void TesteIntSyscallIO() throws SVMException {
		this.alu.setRegisterValue(0, (short) 0x2); // Funktion IO
		this.alu.setRegisterValue(1, (short) 0x1); // Ausgabekanal 1
		this.alu.setRegisterValue(2, (short) 0x0); // Ausgabe ab Adresse 0
		this.alu.setRegisterValue(3, (short) 0x6); // X Bytes ausgeben
		this.instruction.execute(new byte[] { (byte) 0x1 /* Modul Syscalls */ });
	}

	/** Aufruf mit zu vielen/wenigen Parametern löst einen Fehler aus. */
	@Test
	public void testeFehlerhaftenParameter() throws SVMException {
		var ex = assertThrows(SVMException.class, () -> this.instruction.execute(new byte[0]));
		assertThat(ex.getLocalizedMessage(), equalTo("'Int' erwartet 1 Parameter; erhalten '[]'!"));
		ex = assertThrows(SVMException.class, () -> this.instruction.execute(new byte[2]));
		assertThat(ex.getLocalizedMessage(), equalTo("'Int' erwartet 1 Parameter; erhalten '[0, 0]'!"));
	}

}
