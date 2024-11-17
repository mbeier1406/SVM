package com.github.mbeier1406.SVM.syscalls;

import static com.github.mbeier1406.SVM.syscalls.SyscallFactory.SYSCALLS;
import static com.github.mbeier1406.SVM.syscalls.SyscallInterface.Codes.EXIT;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.SVM.SVMException;

/**
 * Test für die Klasse {@linkplain Exit}.
 */
public class ExitTest {

	/** Beendet die JVM mit Exitcode 0 */
	@Test
	public void testeSyscall() throws SVMException {
		SYSCALLS.get(EXIT.getCode()).execute((short) 0, null, null);
		throw new SVMException("Syscall funktioniert nicht: " + EXIT.name());
	}

}
