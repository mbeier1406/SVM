package com.github.mbeier1406.svm.syscalls;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.impl.MEMShort;

/**
 * Basisfunktionen für alle Tests für {@linkplain SyscallInterface Syscalls}.
 */
public abstract class TestBase {

	/** Das zu testende Objekt */
	protected SyscallInterface<Short> syscall;

	/** Für den Test {@linkplain ExitTest} */
	protected Short returnCode = 0;

	/** Die ALU, mit dem der Syscall ausgeführt wird */
	protected final ALU.Instruction<Short> alu = new ALU.Instruction<Short>() {
		@Override
		public void setStopFlag(Short code) {
			returnCode = code;
		}
		
	};

	/** Der Speicher, mit dem der Syscall ausgeführt wird */
	protected final MEM.Instruction<Short> mem = new MEMShort();

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
