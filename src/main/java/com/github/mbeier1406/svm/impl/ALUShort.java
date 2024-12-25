package com.github.mbeier1406.svm.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.ALU.Instruction;
import com.github.mbeier1406.svm.BinaerDarstellung;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.instructions.InstructionReaderInterface;
import com.github.mbeier1406.svm.instructions.InstructionReaderShort;

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
	private short statusRegister;

	/**
	 * Instruction-Pointer (IP), zeigt auf die nächste auszuführende Anweisung.
	 * Da vom typ {@linkplain Integer}, Darf der Speicher ({@linkplain #mem}) nicht
	 * größer als {@linkplain Integer#MAX_VALUE} sein!
	 */
	private int ip;

	/**
	 * Der {@linkplain InstructionReaderShort} liest die nächste, auszuführende
	 * {@linkplain InstructionInterface Instruktion} ein, auf die der {@linkplain #ip}
	 * gerade zeigt.
	 */
	private InstructionReaderInterface<Short> instructionReader = new InstructionReaderShort();


	public ALUShort(MEMShort mem) {
		this.mem = mem;
		this.init();
	}

	/** {@inheritDoc} */
	@Override
	public void init() {
		this.statusRegister = 0; // Statusregister löschen
		this.ip = this.mem.getHighAddr(); // Programme werdne "von oben nach unten" ausgeführt
	}

	/** {@inheritDoc} */
	@Override
	public void setStopFlag() {
		this.statusRegister |= Short.MIN_VALUE; // Oberstes Bit setzen
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
			final var instr = instructionReader.getInstruction(mem, ip);
			LOGGER.trace("instr={}; len={} ({})", instr, instr.len(), bdByte.getBinaerDarstellung(instr.instr().getCode()));
			instr.instr().execute(null);
			this.ip -= instr.len();
		}
		return 0;
	}		


	/** Prüft, ob das oberste Bit im Statusregister gesetzt ist */
	private boolean isStopped() {
		return (this.statusRegister & Short.MIN_VALUE) == Short.MIN_VALUE;
	}

	@Override
	public String toString() {
		var sb = new StringBuilder(100);
		sb.append("ALU:\n\tStatus-Register: ");
		sb.append(bdShort.getBinaerDarstellung(statusRegister));
		sb.append("\n\tInstruktion: ");
		Short instr = null;
		try {
			instr = mem.read(this.ip);
		} catch (SVMException e) {
			sb.append(e.getLocalizedMessage());
		}
		sb.append(instr);
		sb.append(" (");
		sb.append(bdShort.getBinaerDarstellung(instr));
		sb.append(")");
		sb.append("\n");
		return sb.toString();
	}

	/** Dient der Darstellung von Werten in Registern oder Speicherworten in Binärdarstellung */
	private BinaerDarstellung<Short> bdShort = new BinaerDarstellung<>();

	/** Dient der Darstellung von Instruktionscodes in Binärdarstellung */
	private BinaerDarstellung<Byte> bdByte = new BinaerDarstellung<>();

}
