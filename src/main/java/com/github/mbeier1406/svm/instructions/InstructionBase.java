package com.github.mbeier1406.svm.instructions;

import static java.util.Objects.requireNonNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Enthält grundlegende Funktionen und Daten für alle {@linkplain InstructionInterface Maschinenbefehle}.
 */
public abstract class InstructionBase implements InstructionInterface<Short> {

	public static final Logger LOGGER = LogManager.getLogger(InstructionBase.class);

	/** Erlaubt den Systemaufrufen den Zugriff auf die ALU */
	protected ALU.Instruction<Short> alu;

	/** Erlaubt den Systemaufrufen den Zugriff auf den Hauptspeicher */
	protected MEM.Instruction<Short> mem;

	/** {@inheritDoc} */
	@Override
	public void setAlu(final ALU.Instruction<Short> alu) {
		this.alu = alu;
	}

	/** {@inheritDoc} */
	@Override
	public void setMemory(final MEM.Instruction<Short> mem) {
		this.mem = mem;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object anotherObject) {
		return anotherObject != null && this.getClass().getName().equals(anotherObject.getClass().getName());
	}

	/** Protokolliert den Aufruf der Instruktion und prüft die Parameterliste */
	protected void checkParameter(byte[] params, int len) throws SVMException {
		LOGGER.trace("{}: '{}'", this.getClass().getSimpleName(), params);
		if ( requireNonNull(params, "param1").length != len ) throw new SVMException(this.getClass().getSimpleName()+" erwartet "+len+" Parameter!");
	}

}
