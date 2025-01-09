package com.github.mbeier1406.svm.instructions;

import static com.github.mbeier1406.svm.SVM.BD_BYTE;
import static com.github.mbeier1406.svm.SVM.BD_SHORT;
import static java.util.Objects.requireNonNull;
import static org.apache.logging.log4j.CloseableThreadContext.put;

import java.util.Optional;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.impl.RuntimeShort;

/**
 * Implementierung für das Einlesen des nächsten, auszuführenden Maschinenbefehls
 * bei einer {@linkplain SVM} mit Wortlänge {@linkplain Short}.
 */
public class InstructionReaderShort implements InstructionReaderInterface<Short> {

	public static final Logger LOGGER = LogManager.getLogger(InstructionReaderShort.class);

	/** {@inheritDoc} */
	@Override
	public InstructionDefinition<Short> getInstruction(MEM.Instruction<Short> mem, int addr) throws SVMException {
		try ( CloseableThreadContext.Instance _ = put("mem", mem.toString()).put("addr", String.valueOf(addr)) ) {
			/* Teil I: die Instruktion anhand des Codes ermitteln */
			short nextWord = requireNonNull(mem, "mem").read(addr);
			LOGGER.trace("nextWord={}", BD_SHORT.getBinaerDarstellung(nextWord));
			byte cmd = (byte) (nextWord >> 8);
			LOGGER.trace("cmd={}", BD_BYTE.getBinaerDarstellung(cmd));
			var instr = InstructionFactory.INSTRUCTIONS.get(cmd);
			LOGGER.trace("instr={}", instr);
			if ( instr == null ) throw new SVMException("addr="+addr+", ungültiger Code: '"+cmd+"'");
			/* Teil II: Länge der Instruktion mit Parametern in Wortlänge der SVM (hier: Short) ermitteln */
			int lenInWords = instr.getInstrLenInWords(instr, RuntimeShort.WORTLAENGE_IN_BYTES);
			LOGGER.trace("lenInWords={}", lenInWords);
			/* Teil III: Parameterliste der Instruktion erstellen */
			var args = new byte[instr.getAnzahlParameter()];
			for ( int i=0; i < args.length; ) {
				args[i++] = (byte) ((nextWord << 8)>>8); // Rechtes Byte des aktuellen Speicherworts lesen
				LOGGER.trace("args[{}]={}", i-1, BD_BYTE.getBinaerDarstellung(args[i-1]));
				if ( i < args.length ) {
					nextWord = mem.read(--addr);
					LOGGER.trace("nextWord={}", BD_SHORT.getBinaerDarstellung(nextWord));
					args[i++] = (byte) (nextWord >> 8); // Linkes Byte des aktuellen Speicherworts lesen
					LOGGER.trace("args[{}]={}", i-1, BD_BYTE.getBinaerDarstellung(args[i-1]));
				}
			}
			/* Fertig */
			return new InstructionDefinition<Short>(instr, args, Optional.of(lenInWords));
		}
		catch ( Exception e ) {
			throw new SVMException("mem="+mem.toString()+"; addr="+addr, e);
		}
	}

}
