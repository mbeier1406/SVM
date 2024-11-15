package com.github.mbeier1406.SVM.syscalls;

/**
 * Enthält grundlegende Funktionen und Daten für alle {@linkplain SyscallInterface Syscalls}.
 */
public class SyscallBase {

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object anotherObject) {
		return anotherObject != null && this.getClass().getName().equals(anotherObject.getClass().getName());
	}

}
