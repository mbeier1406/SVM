package com.github.mbeier1406.svm.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain ALUShort}.
 */
public class ALUShortTest {

	public static final Logger LOGGER = LogManager.getLogger(ALUShortTest.class);

	public MEMShort memShort;
	public ALUShort alu;

	@BeforeEach
	public void init() {
		this.memShort = new MEMShort();
		this.alu = new ALUShort(memShort);
	}

	@Test
	public void testeToString() {
		LOGGER.info("aluShort={}", alu);
		alu.setStopFlag();
		LOGGER.info("aluShort={}", alu);
	}

}
