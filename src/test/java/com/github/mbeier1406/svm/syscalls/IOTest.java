package com.github.mbeier1406.svm.syscalls;

import static com.github.mbeier1406.svm.syscalls.IO.OutStream.STDOUT;
import static com.github.mbeier1406.svm.syscalls.IO.OutStream.TEMP_FILE;
import static com.github.mbeier1406.svm.syscalls.SyscallFactory.SYSCALLS;
import static com.github.mbeier1406.svm.syscalls.SyscallInterface.Codes.IO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;

/**
 * Test für die Klasse {@linkplain IO}.
 */
public class IOTest extends TestBase {

	/** Initialisiert den Speicher mit zwei Strings, setzt das Tempfile */
	@BeforeEach
	public void init() throws SVMException {
		/* Das zu testende Objekt */
		syscall = SYSCALLS.get(IO.getCode());
		/* Tempfile für definierte Testumgebung zurücksetzen */
		((IO) syscall).setTempFile(null);
		/* Der Speicher, mit dem der Syscall ausgeführt wird */
		mem.write(0, (short) 'a');
		mem.write(1, (short) 'b');
		mem.write(2, (short) 'c');
		mem.write(3, (short) '\n');
		mem.write(4, (short) 'x');
		mem.write(5, (short) '\n');
		/* Diese Instruktion verwendet drei Parameter */
		super.testeParam1Null();
		super.testeParam2Null();
		super.testeParam3Null();
	}

	/** Gibt einen Text über Stdout aus */
	@Test
	public void testeSyscallStdout() throws SVMException {
		syscall.execute((short) STDOUT.ordinal(), (short) 0, (short) 6);
	}

	/** Stellt sicher, dass der in {@linkplain #init()} erzeugte Test in die Tempdatei geschrieben wird */
	@Test
	public void testeSyscallTempfile() throws SVMException, IOException {
		File f = File.createTempFile("svm", "tmp");
		f.deleteOnExit();
		try ( PrintStream p = new PrintStream(f) ) {
			((IO) syscall).setTempFile(p);
			syscall.execute((short) TEMP_FILE.ordinal(), (short) 0, (short) 6);
			try ( BufferedReader lineReader = new BufferedReader(new FileReader(f)) ) {
				assertThat(lineReader.readLine(), equalTo("abc"));
				assertThat(lineReader.readLine(), equalTo("x"));
				assertThat(lineReader.readLine(), nullValue());
			}
		}
	}

	/** Stellt sicher, dass der Zugriff auf das nicht-gesetzte Tempfile einen korrekten Fehler liefert */
	@Test
	public void testeNullTempfile() throws SVMException {
		/* zuvor kein Tempfile gesetzt */
		NullPointerException ex = assertThrows(NullPointerException.class, () -> syscall.execute((short) TEMP_FILE.ordinal(), (short) 0, (short) 6));
		assertThat(ex.getMessage(), equalTo("Output '3'!"));
	}

}
