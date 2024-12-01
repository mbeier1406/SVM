package com.github.mbeier1406.svm.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.ALU.Instruction;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Eine Implementierung der {@linkplain ALU} für Tests.
 */
public class ALUShort implements ALU<Short>, Instruction<Short> {

	public static final Logger LOGGER = LogManager.getLogger(ALUShort.class);


	/** Der Hauptspeicher für Programm und Daten */
	private final MEMShort mem;

	/** Die Arbeitsregister */
	private short[] register = new short[4];

	/**
	 * Interner Status der ALU. Bedeutung der Bits vom obersten her:
	 * <ol>
	 * <li>Stop-Flag: 0=Programm weiter ausführen, 1=SVM anhalten</li>
	 * </ol>
	 */
	private byte statusRegister;

	/**
	 * Instruction-Pointer (IP), zeigt auf die nächste auszuführende Anweisung.
	 * Da vom typ {@linkplain Integer}, Darf der Speicher ({@linkplain #mem}) nicht
	 * größer als {@linkplain Integer#MAX_VALUE} sein!
	 */
	private int ip;


	public ALUShort(MEMShort mem) {
		this.mem = mem;
		this.init();
	}

	/** {@inheritDoc} */
	@Override
	public void init() {
		this.statusRegister = 0;
		this.ip = this.mem.getHighAddr(); // Programme werdne "von oben nach unten" ausgeführt
	}

	/** {@inheritDoc} */
	@Override
	public void setStopFlag() {
		this.statusRegister |= -128;
	}

	/** {@inheritDoc} */
	@Override
	public void setRegisterValue(int register, Short value) throws SVMException {
		if ( register < 0 || register >= this.register.length )
			throw new SVMException("register="+register);
		this.register[register] = value;
	}

	/** {@inheritDoc} */
	@Override
	public Short getRegisterValue(int register) throws SVMException {
		if ( register < 0 || register >= this.register.length )
			throw new SVMException("register="+register);
		return this.register[register];
	}

	/** {@inheritDoc} */
	@Override
	public int start() throws SVMException {
		for ( ; !isStopped(); ) {
			short instr = mem.read(this.ip); // Der aktuelle Machinenbefehl, ein Speicherwort = 1 Byte (Instruction) + ggf. 1 Byte Parametr 1
			LOGGER.trace("instr={} ({})", instr, getBinaerDarstellung(instr));
		}
		return 0;
	}		


	/** Prüft, ob das oberste Bit im Statusregister gesetzt ist */
	private boolean isStopped() {
		return (this.statusRegister & -128) == -128;
	}

	@Override
	public String toString() {
		var sb = new StringBuilder(100);
		sb.append("ALU:\n\tStatus-Register: ");
		sb.append(getBinaerDarstellung(statusRegister));
		sb.append("\n\tInstruktion: ");
		Short instr = null;
		try {
			instr = mem.read(this.ip);
		} catch (SVMException e) {
			sb.append(e.getLocalizedMessage());
		}
		sb.append(instr);
		sb.append(" (");
		sb.append(getBinaerDarstellung(instr));
		sb.append(")");
		sb.append("\n");
		return sb.toString();
	}

}
