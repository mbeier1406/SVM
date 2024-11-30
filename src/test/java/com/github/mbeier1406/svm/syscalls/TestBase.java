package com.github.mbeier1406.svm.syscalls;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.ALUMock;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.MEMMock;

/**
 * Basisfunktionen für alle Tests für {@linkplain SyscallInterface Syscalls}.
 */
public abstract class TestBase {

	/** Das zu testende Objekt */
	protected SyscallInterface<Short> syscall;

	/** Die Test-ALU mit Zugriff auf interne Variablen */
	protected ALU.Instruction<Short> alu = new ALUMock();

	/** das test-MEM */
	protected MEM.Instruction<Short> mem = new MEMMock();


	/** Lädt alle Syscalls und initialisiert sie mit ALU und Speicher */
	protected TestBase() {
		SyscallFactory.init(alu, mem);
	}

	/** Prüft die Exception für param1 == <b>null</b> */
	protected void testeParam1Null() {
		NullPointerException ex = assertThrows(NullPointerException.class, () -> syscall.execute(null, (short) 1, (short) 1));
		assertThat(ex.getMessage(), equalTo("param1"));
	}

	/** Prüft die Exception für param2 == <b>null</b> */
	protected void testeParam2Null() {
		NullPointerException ex = assertThrows(NullPointerException.class, () -> syscall.execute((short) 1, null, (short) 1));
		assertThat(ex.getMessage(), equalTo("param2"));
	}

	/** Prüft die Exception für param3 == <b>null</b> */
	protected void testeParam3Null() {
		NullPointerException ex = assertThrows(NullPointerException.class, () -> syscall.execute((short) 1, (short) 1, null));
		assertThat(ex.getMessage(), equalTo("param3"));
	}

}
