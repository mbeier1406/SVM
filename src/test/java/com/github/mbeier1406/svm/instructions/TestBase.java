package com.github.mbeier1406.svm.instructions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Basisfunktionen für alle Tests für {@linkplain InstructionInterface Instructions}.
 */
public abstract class TestBase {

	/** Das zu testende Objekt */
	protected InstructionInterface<Short> instruction;

	/** Die ALU, mit dem der Syscall ausgeführt wird */
	protected final ALU.Instruction<Short> alu = new ALU.Instruction<Short>() {
		@Override
		public void setStopFlag(Short code) {
		}
		@Override
		public Short getRegisterValue(int register) throws SVMException {
			return null;
		}		
	};

	/** Der Speicher, mit dem der Syscall ausgeführt wird */
	protected final MEM.Instruction<Short> mem = new MEM.Instruction<Short>() {
		private Short[] ram = new Short[100];
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

	/** Lädt alle Instructions und initialisiert sie mit ALU und Speicher */
	protected TestBase() {
		InstructionFactory.init(alu, mem);
	}

	/** Prüft die Exception für param1 == <b>null</b> */
	@Test
	protected void testeParam1Null() {
		NullPointerException ex = assertThrows(NullPointerException.class, () -> this.instruction.execute(null));
		assertThat(ex.getMessage(), equalTo("param1"));
	}

}
