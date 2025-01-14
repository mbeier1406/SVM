package com.github.mbeier1406.svm.instructions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;

/**
 * <b>NOP</b> No Operation - die Instruktions macht nichts,
 * die {@linkplain ALU} fährt mit dem nächsten Befehl fort.
 */
@Instruction(code = Nop.CODE)
public class Nop extends InstructionBase implements InstructionInterface<Short> {

	private static final long serialVersionUID = 1441433188003990456L;
	public static final Logger LOGGER = LogManager.getLogger(Nop.class);

	/** Der Code im {@linkplain MEM Speicher}, die diesen Maschinenbefehl idebntifiziert */
	public static final byte CODE = 0x1;

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
	public int getAnzahlParameter() {
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public int execute(byte[] params) throws SVMException {
		checkParameter(params);
		return 0; // NOP tut nichts
	}

}
