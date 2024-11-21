package com.github.mbeier1406.svm.syscalls;

import static com.github.mbeier1406.svm.syscalls.SyscallFactory.SYSCALLS;
import static com.github.mbeier1406.svm.syscalls.SyscallInterface.Codes.EXIT;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;

/**
 * Test für die Klasse {@linkplain Exit}.
 */
public class ExitTest extends TestBase {

	/** Initialisiert den Speicher mit zwei Strings, setzt das Tempfile */
	@BeforeEach
	public void init() throws SVMException {
		/* Das zu testende Objekt */
		syscall = SYSCALLS.get(EXIT.getCode());
		/* Diese Instruktion verwendet einen Parameter */
		super.testeParam1Null();
	}

	/** Testet, ob der Exitcode gestzt wird */
	@Test
	public void testeSyscall() throws SVMException {
		assertThat(returnCode, equalTo((short) 0));
		syscall.execute((short) 1, null, null);
		assertThat(returnCode, equalTo((short) 1));
	}

}
