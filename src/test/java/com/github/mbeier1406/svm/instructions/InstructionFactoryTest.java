package com.github.mbeier1406.svm.instructions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain InstructionFactory}.
 */
public class InstructionFactoryTest {

	public static final Logger LOGGER = LogManager.getLogger(InstructionFactoryTest.class);

	/** Stellt sicher, dass die Factory alle definierten {@linkplain InstructionInterface Maschinenbefehle} lädt */
	@SuppressWarnings("unchecked")
	@Test
	public void pruefeSyscalls() {
		LOGGER.info("Instructions: {}", InstructionFactory.INSTRUCTIONS);
		assertThat(InstructionFactory.INSTRUCTIONS.values(), contains(new Nop(), new Int(), new Mov()));
	}

}
