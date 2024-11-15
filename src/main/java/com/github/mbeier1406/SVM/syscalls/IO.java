package com.github.mbeier1406.SVM.syscalls;

import java.io.PrintStream;

import com.github.mbeier1406.SVM.SVMException;

/**
 * Gibt die Zeichenkette an der Adresse Parameter 2 der Länge Parameter 3
 * in den Kanal Parameter 1 aus.
 */
@Syscall(code = 0x2)
public class IO extends SyscallBase implements SyscallInterface<Short> {

	/** {@inheritDoc} */
	@Override
	public int execute(Short param1, Short param2, Short param3) throws SVMException {
		PrintStream out;
		switch ( param1 ) {
			case 1: out = System.out; break;
			case 2: out = System.err; break;
			default: throw new SVMException("Ungültiger Kanal: "+param1);
		}
		for ( short i=0; i < param3; i++ )
			; // TODO: Zugriff MEM
		return 0;
	}

}
