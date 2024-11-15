package com.github.mbeier1406.SVM.syscalls;

import com.github.mbeier1406.SVM.SVMException;

/**
 * Beendet das gerade ausgeführte Programm mit dem ExitCode aus Parameter 1<p/>
 */
@Syscall(code = 0x1)
public class Exit extends SyscallBase implements SyscallInterface<Short> {

	/** {@inheritDoc} */
	@Override
	public int execute(Short param1, Short param2, Short param3) throws SVMException {
		System.exit((short) param1);
		throw new SVMException("Exit: "+param1);
	}

}
