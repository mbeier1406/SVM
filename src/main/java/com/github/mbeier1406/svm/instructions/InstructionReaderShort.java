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

	public static final Logger LOGGER = LogManager.getFormatterLogger(InstructionReaderShort.class);

	/** Für die Protokollierung der Speicherinhalte */
	private BinaerDarstellung<Short> bdShort = new BinaerDarstellung<>();

	/** Für die Protokollierung der Codes von Maschinenbefehlen */
	private BinaerDarstellung<Byte> bdByte = new BinaerDarstellung<>();

	/** {@inheritDoc} */
	@Override
	public InstructionInterface<Short> getInstruction(MEM.Instruction<Short> mem, int addr) throws SVMException {
		try ( CloseableThreadContext.Instance ctx = put("mem", mem.toString()) ) {
			short nextWord = requireNonNull(mem, "mem").read(addr);
			LOGGER.trace("nextWord={}", bdShort.getBinaerDarstellung(nextWord));
			byte cmd = (byte) (nextWord << 8);
			LOGGER.trace("cmd={}", bdByte.getBinaerDarstellung(cmd));
			return null;
		}
		catch ( Exception e ) {
			throw new SVMException();
		}
	}

}
