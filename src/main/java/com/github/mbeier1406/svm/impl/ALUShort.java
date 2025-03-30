package com.github.mbeier1406.svm.impl;

import static com.github.mbeier1406.svm.SVM.BD_BYTE;
import static com.github.mbeier1406.svm.SVM.BD_SHORT;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.ALU.Instruction;
import com.github.mbeier1406.svm.MEM;
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
		this.mem.clear(); // Speicher löschen
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
			LOGGER.trace("ip={}", ip);
			final var instrDef = instructionReader.getInstruction(mem, ip);
			LOGGER.trace("instr={}; len={} ({})", instrDef, instrDef.lenInWords(), BD_BYTE.getBinaerDarstellung(instrDef.instruction().getCode()));
			instrDef.instruction().execute(instrDef.params());
			this.ip -= instrDef.getLenInMemoryInWords();
		}
		LOGGER.debug("Stopp: {}", this);
		return this.register[0];
	}

	/** {@inheritDoc} */
	@Override
	public MEM<Short> getMEM() {
		return this.mem;
	}

	/** {@inheritDoc} */
	@Override
	public Instruction<Short> getInstructionInterface() {
		return (Instruction<Short>) this;
	}


	/** Prüft, ob das oberste Bit im Statusregister gesetzt ist */
	private boolean isStopped() {
		return (this.statusRegister & Short.MIN_VALUE) == Short.MIN_VALUE;
	}

	@Override
	public String toString() {
		var sb = new StringBuilder(100);
		sb.append("ALU:\n\tStatus-Register: ");
		sb.append(BD_SHORT.getBinaerDarstellung(statusRegister));
		sb.append("\n\tInstruktion: ");
		Short instr = null;
		try {
			instr = mem.read(this.ip);
		} catch (SVMException e) {
			sb.append(e.getLocalizedMessage());
		}
		sb.append(instr);
		sb.append(" (");
		sb.append(BD_SHORT.getBinaerDarstellung(instr));
		sb.append(")");
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public void setDebugInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDebugMode(boolean on) {
		// TODO Auto-generated method stub
		
	}

}
