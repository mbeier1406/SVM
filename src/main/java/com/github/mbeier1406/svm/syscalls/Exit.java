package com.github.mbeier1406.svm.syscalls;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;

/**
 * Beendet das gerade ausgeführte Programm mit dem ExitCode aus Parameter 1<p/>
 */
@Syscall(code = 0x1)
public class Exit extends SyscallBase implements SyscallInterface<Short> {

	public static final Logger LOGGER = LogManager.getLogger(Exit.class);

	/** {@inheritDoc} */
	@Override
	public int execute(Short param1, Short param2, Short param3) throws SVMException {
		Short returnCode = Objects.requireNonNull(param1, "param1");
		LOGGER.trace("SYSCALL {}: returnCode={}", getClass().getSimpleName(), returnCode);
		super.alu.setRegisterValue(0, returnCode);
		super.alu.setStopFlag();
		return 0;
	}

}
