package com.github.mbeier1406.svm.syscalls;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;

/**
 * Enthält grundlegende Funktionen und Daten für alle {@linkplain SyscallInterface Syscalls}.
 */
public abstract class SyscallBase implements SyscallInterface<Short> {

	/** Erlaubt den Systemaufrufen den Zugriff auf die ALU */
	protected ALU.Instruction<Short> alu;

	/** Erlaubt den Systemaufrufen den Zugriff auf den Hauptspeicher */
	protected MEM.Instruction<Short> mem;

	/** {@inheritDoc} */
	@Override
	public void setAlu(final ALU.Instruction<Short> alu) {
		this.alu = alu;
	}

	/** {@inheritDoc} */
	@Override
	public void setMemory(final MEM.Instruction<Short> mem) {
		this.mem = mem;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object anotherObject) {
		return anotherObject != null && this.getClass().getName().equals(anotherObject.getClass().getName());
	}

}
