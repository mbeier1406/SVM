package com.github.mbeier1406.svm.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;

/**
 * Tests für die Klasse {@linkplain ALUShort}.
 */
public class ALUShortTest {

	public static final Logger LOGGER = LogManager.getLogger(ALUShortTest.class);

	public MEMShort mem;
	public ALUShort alu;

	@BeforeEach
	public void init() {
		this.mem = new MEMShort();
		this.alu = new ALUShort(mem);
	}

	@Test
	public void testeStatusRegister() {
		LOGGER.info("aluShort={}", alu);
		alu.setStopFlag();
		LOGGER.info("aluShort={}", alu);
	}

	@Test
	public void testeInstr() throws SVMException {
		mem.write(mem.getHighAddr(), (short) 257);
		LOGGER.info("aluShort={}", alu);
	}

}
