package com.github.mbeier1406.svm.instructions;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Enthält grundlegende Funktionen und Daten für alle {@linkplain InstructionInterface Maschinenbefehle}.
 */
public abstract class InstructionBase implements InstructionInterface<Short> {

	private static final long serialVersionUID = 1743282704168266537L;
	public static final Logger LOGGER = LogManager.getLogger(InstructionBase.class);

	/** Erlaubt den Systemaufrufen den Zugriff auf die ALU */
	protected transient ALU.Instruction<Short> alu;

	/** Erlaubt den Systemaufrufen den Zugriff auf den Hauptspeicher */
	protected transient MEM.Instruction<Short> mem;

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
	protected void checkParameter(byte[] params) throws SVMException {
		LOGGER.trace("{}: erwartete Parameterlänge {}, erhalten '{}'", this.getClass().getSimpleName(), requireNonNull(getAnzahlParameter(), "getAnzahlParameter"), params);
		if ( requireNonNull(params, "param1").length != getAnzahlParameter() )
			throw new SVMException("'"+this.getClass().getSimpleName()+"' erwartet "+getAnzahlParameter()+" Parameter; erhalten '"+Arrays.toString(params)+"'!");
	}

	/** Fasst zwei Bytes aus der Parameterliste zu einem Short zusammen */
	protected static short bytes2Short(byte links, byte rechts) {
		return (short) ((links << 8) | rechts);
	}

}
