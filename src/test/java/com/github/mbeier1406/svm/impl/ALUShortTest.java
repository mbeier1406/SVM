package com.github.mbeier1406.svm.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

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
		alu.init();
		LOGGER.info("aluShort={}", alu);
	}

	/** Bei Setzen des Stopp-Flags muss das oberste Bit des Statusregisters gesetzt werden */
	@Test
	public void testeStatusRegister() {
		assertThat(alu.toString(), containsString("Status-Register: 0000000000000000"));
		alu.setStopFlag();
		LOGGER.info("aluShort={}", alu);
		assertThat(alu.toString(), containsString("Status-Register: 1000000000000000"));
	}

	@Test
	public void testeInstr() throws SVMException {
		mem.write(mem.getHighAddr(), (short) 257);
		LOGGER.info("aluShort={}", alu);
	}

}
