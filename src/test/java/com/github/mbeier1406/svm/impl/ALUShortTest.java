package com.github.mbeier1406.svm.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionFactory;
import com.github.mbeier1406.svm.syscalls.SyscallFactory;

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
		this.alu.init();
		LOGGER.info("aluShort={}", alu);
		InstructionFactory.init(alu, mem);
		SyscallFactory.init(alu, mem);
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
		this.mem.write(this.mem.getHighAddr(), (short) 256); // NOP
		this.mem.write(this.mem.getHighAddr()-1, (short) 513); // INT 1 (Syscall)
		this.mem.write(this.mem.getLowAddr()+3, (short) 'a');
		this.mem.write(this.mem.getLowAddr()+2, (short) 'b');
		this.mem.write(this.mem.getLowAddr()+1, (short) 'c');
		this.mem.write(this.mem.getLowAddr()+0, (short) '\n');
		this.alu.setRegisterValue(0, (short) 2); // Funktion IO
		this.alu.setRegisterValue(1, (short) 1); // Ausgabe stdout
		this.alu.setRegisterValue(2, (short) (this.mem.getLowAddr()+3)); // Ausgabe Adresse
		this.alu.setRegisterValue(3, (short) 4); // Ausgabe Länge
		this.mem.write(this.mem.getHighAddr()-2, (short) 513); // INT 1 (Syscall)
		this.alu.setRegisterValue(0, (short) 1); // Funktion EXIT
		this.alu.setRegisterValue(1, (short) 55); // Return Code 55
		LOGGER.info("aluShort={}", alu);
		int exitCode = this.alu.start();
		LOGGER.info("exitCode={}", exitCode);
		assertThat(exitCode, equalTo(55));
	}

}
