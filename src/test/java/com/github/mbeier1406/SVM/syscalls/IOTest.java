package com.github.mbeier1406.SVM.syscalls;

import static com.github.mbeier1406.SVM.syscalls.IO.OutStream.STDOUT;
import static com.github.mbeier1406.SVM.syscalls.SyscallFactory.SYSCALLS;
import static com.github.mbeier1406.SVM.syscalls.SyscallInterface.Codes.IO;

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
		SYSCALLS.get(IO.getCode()).execute((short) STDOUT.ordinal(), (short) 0, (short) 6);
	}

//	public static class 
}
