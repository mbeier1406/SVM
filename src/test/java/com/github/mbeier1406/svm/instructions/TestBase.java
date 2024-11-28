package com.github.mbeier1406.svm.instructions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.syscalls.SyscallFactory;

/**
 * Basisfunktionen für alle Tests für {@linkplain InstructionInterface Instructions}.
 */
public abstract class TestBase {

	/** Das zu testende Objekt */
	protected InstructionInterface<Short> instruction;

	/** Für den Testfall {@linkplain Int} mit Code (Syscall) 0x1 -> Exit */
	protected static int stopFlag = 0;

	/** Für den Testfall {@linkplain Int} mit Code (Syscall) 0x1 -> Returncode für den Exit */
	protected static int returnCode = 0;

	/** Die ALU, mit dem der Syscall ausgeführt wird */
	protected final ALU.Instruction<Short> alu = new ALU.Instruction<Short>() {
		private short[] register = new short[4];
		@Override
		public void setStopFlag() {
			TestBase.stopFlag = 0x1;
			TestBase.returnCode = this.register[0];
		}
		@Override
		public void setRegisterValue(int register, Short value) throws SVMException {
			if ( register < 0 || register >= this.register.length )
				throw new SVMException("register="+register);
			this.register[register] = value;
		}		
		@Override
		public Short getRegisterValue(int register) throws SVMException {
			if ( register < 0 || register >= this.register.length )
				throw new SVMException("register="+register);
			return this.register[register];
		}		
	};

	/** Größe des Speichers {@linkplain MEM} ist {@value} Speicherworte. */
	public static final int MEMSIZE = 100;

	/** Der Speicher, mit dem der Syscall ausgeführt wird */
	protected final MEM.Instruction<Short> mem = new MEM.Instruction<Short>() {
		private Short[] ram = new Short[MEMSIZE];
		@Override
		public Short read(int addr) throws SVMException {
			if ( addr < 0 || addr >= ram.length )
				throw new SVMException("addr="+addr);
			return ram[addr];
		}
		@Override
		public void write(int addr, Short data) throws SVMException {
			if ( addr < 0 || addr >= ram.length )
				throw new SVMException("addr="+addr);
			ram[addr] = data;
		}
	};

	/** Lädt alle Instructions und initialisiert sie mit ALU und Speicher und fügt ein paar Strings ein */
	protected TestBase() {
		SyscallFactory.init(alu, mem);
		InstructionFactory.init(alu, mem);
		TestBase.stopFlag = 0; // Definierte Testumgebung schaffen
		TestBase.returnCode = 0;
		/* Der Speicher, mit dem der int/Syscall ausgeführt wird */
		try {
			mem.write(0, (short) 'a');
			mem.write(1, (short) 'b');
			mem.write(2, (short) 'c');
			mem.write(3, (short) '\n');
			mem.write(4, (short) 'x');
			mem.write(5, (short) '\n');
		} catch (SVMException e) {
			throw new RuntimeException(e);
		}
	}

	/** Prüft die Exception für param1 == <b>null</b> */
	@Test
	protected void testeParam1Null() {
		NullPointerException ex = assertThrows(NullPointerException.class, () -> this.instruction.execute(null));
		assertThat(ex.getMessage(), equalTo("param1"));
	}

}
