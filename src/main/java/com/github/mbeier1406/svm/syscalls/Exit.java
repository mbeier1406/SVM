package com.github.mbeier1406.svm.syscalls;

import java.util.Objects;

import com.github.mbeier1406.svm.SVMException;

/**
 * Beendet das gerade ausgeführte Programm mit dem ExitCode aus Parameter 1<p/>
 */
@Syscall(code = 0x1)
public class Exit extends SyscallBase implements SyscallInterface<Short> {

	/** {@inheritDoc} */
	@Override
	public int execute(Short param1, Short param2, Short param3) throws SVMException {
		super.alu.setStopFlag(Objects.requireNonNull(param1, "param1"));
		return 0;
	}

}
