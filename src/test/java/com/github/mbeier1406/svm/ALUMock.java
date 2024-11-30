package com.github.mbeier1406.svm;

import com.github.mbeier1406.svm.ALU.Instruction;
import com.github.mbeier1406.svm.instructions.Int;

/**
 * Eine Implementierung der {@linkplain ALU} für Tests.
 */
public class ALUMock implements Instruction<Short> {

	/** Für den Testfall {@linkplain Int} mit Code (Syscall) 0x1 -> Exit */
	public static int stopFlag = 0;

	/** Für den Testfall {@linkplain Int} mit Code (Syscall) 0x1 -> Returncode für den Exit */
	public static int returnCode = 0;

	private short[] register = new short[4];
	@Override
	public void setStopFlag() {
		ALUMock.stopFlag = 0x1;
		ALUMock.returnCode = this.register[0];
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

}
