package com.github.mbeier1406.SVM.syscalls;

import static com.github.mbeier1406.SVM.syscalls.IO.OutStream.STDOUT;
import static com.github.mbeier1406.SVM.syscalls.IO.OutStream.TEMP_FILE;
import static com.github.mbeier1406.SVM.syscalls.SyscallFactory.SYSCALLS;
import static com.github.mbeier1406.SVM.syscalls.SyscallInterface.Codes.IO;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.SVM.MEM;
import com.github.mbeier1406.SVM.SVMException;
import com.github.mbeier1406.SVM.impl.MEMShort;

/**
 * Test für die Klasse {@linkplain IO}.
 */
public class IOTest {

	/** Der Speicher, mit dem der Syscall ausgeführt wird */
	public MEM.Instruction<Short> mem = new MEMShort();

	/** Das zu testende Objekt */
	private SyscallInterface<Short> ioSycall = SYSCALLS.get(IO.getCode());

	/** Initialisiert den Speicher mit zwei Strings */
	@BeforeEach
	public void init() throws SVMException {
		mem.write(0, (short) 'a');
		mem.write(1, (short) 'b');
		mem.write(2, (short) 'c');
		mem.write(3, (short) '\n');
		mem.write(4, (short) 'x');
		mem.write(5, (short) '\n');
		SyscallFactory.setMem(mem);
	}

	/** Gibt einen Text über Stdout aus */
	@Test
	public void testeSyscallStdout() throws SVMException {
		ioSycall.execute((short) STDOUT.ordinal(), (short) 0, (short) 6);
	}

	/** Stellt sicher, dass der in {@linkplain #init()} erzeugte Test in die Tempdatei geschrieben wird */
	@Test
	public void testeSyscallTempfile() throws SVMException, IOException {
		File f = File.createTempFile("svm", "tmp");
		f.deleteOnExit();
		try ( PrintStream p = new PrintStream(f) ) {
			((IO) ioSycall).setTempFile(p);
			ioSycall.execute((short) TEMP_FILE.ordinal(), (short) 0, (short) 6);
			try ( BufferedReader lineReader = new BufferedReader(new FileReader(f)) ) {
				assertThat(lineReader.readLine(), equalTo("abc"));
				assertThat(lineReader.readLine(), equalTo("x"));
				assertThat(lineReader.readLine(), nullValue());
			}
		}
	}

}
