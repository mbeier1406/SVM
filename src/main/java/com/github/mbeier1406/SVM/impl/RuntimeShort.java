package com.github.mbeier1406.SVM.impl;

import static com.github.mbeier1406.SVM.syscalls.SyscallFactory.SYSCALLS;
import static com.github.mbeier1406.SVM.syscalls.SyscallInterface.Codes.IO;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import com.github.mbeier1406.SVM.MEM;
import com.github.mbeier1406.SVM.MEM.Instruction;
import com.github.mbeier1406.SVM.Runtime;
import com.github.mbeier1406.SVM.SVMException;
import com.github.mbeier1406.SVM.syscalls.IO;
import com.github.mbeier1406.SVM.syscalls.SyscallFactory;
import com.github.mbeier1406.SVM.syscalls.SyscallInterface;

public class RuntimeShort implements Runtime<Short> {

	/** {@linkplain SyscallInterface Syscalls} haben Zugriff auf den Hauptspeicher */
	private final MEM.Instruction<Short> mem;

	/** In diese Datei kann der Syscall {@linkplain IO} schreiben */
	private File tempFile;


	public RuntimeShort(Instruction<Short> mem) throws IOException {
		super();

		/* Zugriff auf den Hauptspeicher für Syscalls */
		this.mem = mem;
		SyscallFactory.setMem(this.mem);

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
