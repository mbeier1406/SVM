package com.github.mbeier1406.SVM.impl;

import com.github.mbeier1406.SVM.MEM;
import com.github.mbeier1406.SVM.MEM.Instruction;
import com.github.mbeier1406.SVM.Runtime;
import com.github.mbeier1406.SVM.SVMException;

public class RuntimeShort implements Runtime<Short> {

	private final MEM.Instruction<Short> mem;

	public RuntimeShort(Instruction<Short> mem) {
		super();
		this.mem = mem;
	}

	/** {@inheritDoc} */
	@Override
	public int syscall(Short code, Short param1, Short param2, Short param3) throws SVMException {
		return 0;
	}


}
