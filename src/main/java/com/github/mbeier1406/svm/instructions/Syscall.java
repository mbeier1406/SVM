package com.github.mbeier1406.svm.instructions;

import com.github.mbeier1406.svm.ALU;

/**
 * <b>Syscall</b> Systemaufruf - die Instruktion ruft einen
 * {@linkplain com.github.mbeier1406.svm.syscalls.SyscallInterface Syscall} auf.
 * Folgende Register der {@linkplain ALU} werden verwendet:
 * <ol>
 * <li><u>Register 1</u>: enthält die Nummer des aufzurufenden Syscalls {@linkplain com.github.mbeier1406.svm.syscalls.Syscall#code()}</li>
 * <li><u>Register 2</u>: enthält den ersten Parameter für den jeweiligen Syscall</li>
 * <li><u>Register 3</u>: enthält den zweiten Parameter für den jeweiligen Syscall</li>
 * <li><u>Register 4</u>: enthält den dritten Parameter für den jeweiligen Syscall</li>
 * </ol>
 * @see {@linkplain com.github.mbeier1406.svm.syscalls.Exit}
 * @see {@linkplain com.github.mbeier1406.svm.syscalls.IO}
 */
@Instruction(code=0x2)
public class Syscall extends InstructionBase implements InstructionInterface<Short> {

	/**
	 * <i>Syscall</i> benutzt einen Parameter (den {@linkplain com.github.mbeier1406.svm.syscalls.Syscall#code() Code}
	 * des Syscalls und kommt daher mit einem Speicherwort ({@linkplain Short} == zwei Byte) aus:<br/>
	 * <pre><code>
	 * Speicherwort I
	 * Byte I             Byte II
	 * &lt;0x2> (syscall)    &lt;Code> (Nummer des Syscalls)  
	 * </code></pre><p/>
	 * {@inheritDoc}
	 */
	@Override
	public Short getLength() {
		return 1;
	}

}
