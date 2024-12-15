package com.github.mbeier1406.svm.instructions;

import static java.util.Objects.requireNonNull;
import static org.apache.logging.log4j.CloseableThreadContext.put;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.BinaerDarstellung;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Implementierung für das Einlesen des nächsten, auszuführenden Maschinenbefehls
 * bei einer {@linkplain SVM} mit Wortlänge {@linkplain Short}.
 */
public class InstructionReaderShort implements InstructionReaderInterface<Short> {

	public static final Logger LOGGER = LogManager.getLogger(InstructionReaderShort.class);

	/** Für die Protokollierung der Speicherinhalte */
	private BinaerDarstellung<Short> bdShort = new BinaerDarstellung<>();

	/** Für die Protokollierung der Codes von Maschinenbefehlen */
	private BinaerDarstellung<Byte> bdByte = new BinaerDarstellung<>();

	/** {@inheritDoc} */
	@Override
	public InstructionDefinition<Short> getInstruction(MEM.Instruction<Short> mem, int addr) throws SVMException {
		try ( CloseableThreadContext.Instance _ = put("mem", mem.toString()).put("addr", String.valueOf(addr)) ) {
			/* Teil I: die Instruktion anhand des Codes ermitteln */
			short nextWord = requireNonNull(mem, "mem").read(addr);
			LOGGER.trace("nextWord={}", bdShort.getBinaerDarstellung(nextWord));
			byte cmd = (byte) (nextWord >> 8);
			LOGGER.trace("cmd={}", bdByte.getBinaerDarstellung(cmd));
			var instr = InstructionFactory.INSTRUCTIONS.get(cmd);
			LOGGER.trace("instr={}", instr);
			if ( instr == null ) throw new SVMException("addr="+addr+", ungültiger Code: '"+cmd+"'");
			/* Teil II: Länge der Instruktion mit Parametern in Wortlänge der SVM (hier: Short) ermitteln */
			int lenInWords = getInstrLenInWords(instr, 2);
			LOGGER.trace("lenInWords={}", lenInWords);
			return new InstructionDefinition<Short>(instr, lenInWords);
		}
		catch ( Exception e ) {
			throw new SVMException("mem="+mem.toString()+"; addr="+addr, e);
		}
	}

}
