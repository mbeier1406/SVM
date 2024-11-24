package com.github.mbeier1406.svm.instructions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.SVMException;

/**
 * <b>NOP</b> No Operation - die Instruktions macht nichts,
 * die {@linkplain ALU} fährt mit dem nächsten Befehl fort.
 */
@Instruction(code = 0x1)
public class Nop extends InstructionBase implements InstructionInterface<Short> {

	public static final Logger LOGGER = LogManager.getLogger(Nop.class);

	/**
	 * <i>NOP</i> benutzt keinen Parameter und kommt daher mit einem Speicherwort
	 * ({@linkplain Short} == zwei Byte) aus:<br/>
	 * <pre><code>
	 * Speicherwort I
	 * Byte I         Byte II
	 * &lt;0x1> (NOP)    &lt;0x0> (nicht verwendet)  
	 * </code></pre><p/>
	 * {@inheritDoc}
	 */
	@Override
	public Short getLength() {
		return 1;
	}

	/** {@inheritDoc} */
	@Override
	public int execute(byte[] params) throws SVMException {
		if ( params != null ) throw new SVMException("NOP erwartet keinen Parameter!");
		LOGGER.trace("NOP");
		return 0; // NOP tut nichts
	}

}
