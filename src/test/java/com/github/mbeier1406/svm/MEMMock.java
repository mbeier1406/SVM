package com.github.mbeier1406.svm;

import com.github.mbeier1406.svm.MEM.Instruction;

/**
 * Eine Implementierung des {@linkplain MEM} für Tests.
 */
public class MEMMock implements Instruction<Short> {

	/** Größe des Speichers {@linkplain MEM} ist {@value} Speicherworte. */
	public static final int MEMSIZE = 100;

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

}
