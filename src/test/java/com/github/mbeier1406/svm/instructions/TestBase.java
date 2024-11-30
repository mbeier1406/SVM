package com.github.mbeier1406.svm.instructions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.ALUMock;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.MEMMock;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.syscalls.SyscallFactory;

/**
 * Basisfunktionen für alle Tests für {@linkplain InstructionInterface Instructions}.
 */
public abstract class TestBase {

	/** Das zu testende Objekt */
	protected InstructionInterface<Short> instruction;

	/** Die Test-ALU mit Zugriff auf interne Variablen */
	protected ALU.Instruction<Short> alu = new ALUMock();

	/** das test-MEM */
	protected MEM.Instruction<Short> mem = new MEMMock();

	/** Lädt alle Instructions und initialisiert sie mit ALU und Speicher und fügt ein paar Strings ein */
	protected TestBase() {
		SyscallFactory.init(alu, mem);
		InstructionFactory.init(alu, mem);
		ALUMock.stopFlag = 0; // Definierte Testumgebung schaffen
		ALUMock.returnCode = 0;
		/* Der Speicher, mit dem der int/Syscall ausgeführt wird */
		try {
			mem.write(0, (short) 'a');
			mem.write(1, (short) 'b');
			mem.write(2, (short) 'c');
			mem.write(3, (short) '\n');
			mem.write(4, (short) 'x');
			mem.write(5, (short) '\n');
		} catch (SVMException e) {
			throw new RuntimeException(e);
		}
	}

	/** Prüft die Exception für param1 == <b>null</b> */
	@Test
	protected void testeParam1Null() {
		NullPointerException ex = assertThrows(NullPointerException.class, () -> this.instruction.execute(null));
		assertThat(ex.getMessage(), equalTo("param1"));
	}

}
