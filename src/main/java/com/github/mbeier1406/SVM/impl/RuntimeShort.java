package com.github.mbeier1406.SVM.impl;

import com.github.mbeier1406.SVM.Runtime;
import com.github.mbeier1406.SVM.SVMException;

public class RuntimeShort implements Runtime<Short> {

	/** {@inheritDoc} */
	@Override
	public int syscall(SyscallCode code, Short param1, Short param2, Short param3) throws SVMException {
		return 0;
	}


}
