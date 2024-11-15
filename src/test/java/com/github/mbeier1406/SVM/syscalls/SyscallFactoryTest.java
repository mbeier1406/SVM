package com.github.mbeier1406.SVM.syscalls;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain SyscallFactory}.
 */
public class SyscallFactoryTest {

	public static final Logger LOGGER = LogManager.getLogger(SyscallFactoryTest.class);

	/** Stellt sicher, dass die Factory alle definierten Syscalls lädt */
	@SuppressWarnings("unchecked")
	@Test
	public void pruefeSyscalls() {
		LOGGER.info("Syscalls: {}", SyscallFactory.SYSCALLS);
		assertThat(SyscallFactory.SYSCALLS.values(), contains(new Exit(), new IO()));
	}

}
