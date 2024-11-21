package com.github.mbeier1406.svm.impl;

import static com.github.mbeier1406.svm.syscalls.SyscallFactory.SYSCALLS;
import static com.github.mbeier1406.svm.syscalls.SyscallInterface.Codes.IO;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.MEM.Instruction;
import com.github.mbeier1406.svm.Runtime;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.syscalls.IO;
import com.github.mbeier1406.svm.syscalls.SyscallFactory;
import com.github.mbeier1406.svm.syscalls.SyscallInterface;

public class RuntimeShort implements Runtime<Short> {

	/** {@linkplain SyscallInterface Syscalls} haben Zugriff auf bestimmte Funktionen der ALU */
	private final ALU.Instruction<Short> alu;

	/** {@linkplain SyscallInterface Syscalls} haben Zugriff auf bestimmte Funktionen des Hauptspeichers */
	private final MEM.Instruction<Short> mem;

	/** In diese Datei kann der Syscall {@linkplain IO} schreiben */
	private File tempFile;


	public RuntimeShort(final ALU.Instruction<Short> alu, final Instruction<Short> mem) throws IOException {
		super();

		/* Zugriff auf die ALU und den Hauptspeicher für Syscalls */
		this.alu = alu;
		this.mem = mem;
		SyscallFactory.init(this.alu, this.mem);

		/* Temporäre Datei für den IO-Syscall */
		tempFile = File.createTempFile("svm", "tmp");
		tempFile.deleteOnExit();
		((IO) SYSCALLS.get(IO.getCode())).setTempFile(new PrintStream(this.tempFile));
	}

	/** {@inheritDoc} */
	@Override
	public int syscall(Short code, Short param1, Short param2, Short param3) throws SVMException {
		SYSCALLS.get(code.byteValue()).execute(param1, param2, param3);
		return 0;
	}

}
