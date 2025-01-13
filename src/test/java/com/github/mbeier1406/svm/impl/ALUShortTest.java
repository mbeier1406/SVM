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
import com.github.mbeier1406.svm.prg.SVMLoader;
import com.github.mbeier1406.svm.prg.SVMLoaderShort;
import com.github.mbeier1406.svm.prg.SVMLoaderShortTest;
import com.github.mbeier1406.svm.prg.SVMProgram;
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

	/** Führt ein manuell zusammengebautes Programm aus */
	@Test
	public void testeInstrManuell() throws SVMException {
		/* Speicher mit Konstanten füllen */
		this.mem.write(this.mem.getLowAddr()+3, (short) '\n');
		this.mem.write(this.mem.getLowAddr()+2, (short) 'c');
		this.mem.write(this.mem.getLowAddr()+1, (short) 'b');
		this.mem.write(this.mem.getLowAddr()+0, (short) 'a');

		/* Programm laden */
		int addr = this.mem.getHighAddr();

		/* NOP: No Operation */
		this.mem.write(addr--, (short) 256); // NOP

		/* INT(SYSCALL): IO */
		this.mem.write(addr--, (short) 801); // MOV CONST->REG
		this.mem.write(addr--, (short) 2);   // CONST $2
		this.mem.write(addr--, (short) 0);   // REG(0) == this.alu.setRegisterValue(0, (short) 2); -- Funktion IO
		this.mem.write(addr--, (short) 801); // MOV CONST->REG
		this.mem.write(addr--, (short) 1);   // CONST $1
		this.mem.write(addr--, (short) 1);   // REG(1) == this.alu.setRegisterValue(1, (short) 1); -- Ausgabe stdout
		this.mem.write(addr--, (short) 801); // MOV CONST->REG
		this.mem.write(addr--, (short) this.mem.getLowAddr());   // CONST $0
		this.mem.write(addr--, (short) 2);   // REG(2) == this.alu.setRegisterValue(2, (short) 0); -- Ausgabe Startadresse
		this.mem.write(addr--, (short) 801); // MOV CONST->REG
		this.mem.write(addr--, (short) 4);   // CONST $4
		this.mem.write(addr--, (short) 3);   // REG(3) == this.alu.setRegisterValue(3, (short) 4); -- Ausgabe Länge
		this.mem.write(addr--, (short) 513); // INT 1 (Syscall)
		
		/* INT(SYSCALL): EXIT */
		this.mem.write(addr--, (short) 801); // MOV CONST->REG
		this.mem.write(addr--, (short) 1);   // CONST $1
		this.mem.write(addr--, (short) 0);   // REG(0) == this.alu.setRegisterValue(0, (short) 1); -- Funktion EXIT
		this.mem.write(addr--, (short) 801); // MOV CONST->REG
		this.mem.write(addr--, (short) 55);  // CONST $55
		this.mem.write(addr--, (short) 1);   // REG(1) == this.alu.setRegisterValue(1, (short) 55); -- Return Code 55
		this.mem.write(addr--, (short) 513); // INT 1 (Syscall)
		
		LOGGER.info("aluShort={}", alu);
		int exitCode = this.alu.start();
		LOGGER.info("exitCode={}", exitCode);
		assertThat(exitCode, equalTo(55));
	}

	@Test
	public void testeProgrammAusInternerDarstellungAusfuehren() throws SVMException {
		SVMLoader<Short> svmLoader = new SVMLoaderShort();
		SVMProgram<Short> korrektesProgramm = SVMLoaderShortTest.getKorrektesProgramm();
		svmLoader.load(this.mem, korrektesProgramm);
		LOGGER.info("aluShort={}", alu);
		int exitCode = this.alu.start();
		LOGGER.info("exitCode={}", exitCode);
		assertThat(exitCode, equalTo(55));
	}

}
