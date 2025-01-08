package com.github.mbeier1406.svm.instructions;

import static com.github.mbeier1406.svm.impl.RuntimeShort.WORTLAENGE_IN_BYTES;
import static java.util.Objects.requireNonNull;
import static org.apache.logging.log4j.CloseableThreadContext.put;

import java.util.Arrays;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.MEM.Instruction;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Die Implementierung mit der Wortlänge {@linkplain Short}.
 */
public class InstructionWriterShort implements InstructionWriterInterface<Short> {

	public static final Logger LOGGER = LogManager.getLogger(InstructionWriterShort.class);

	/** {@inheritDoc} */
	@Override
	public Short[] instruction2Array(InstructionDefinition<Short> instr) throws SVMException {
		try ( @SuppressWarnings("unused") CloseableThreadContext.Instance ctx = put("instr", requireNonNull(instr, "instr").toString()) ) {
			int erwarteteAnzahlParameter = instr.instruction().getAnzahlParameter();
			int erhalteneAnzahlParameter = instr.params().length;
			if ( erwarteteAnzahlParameter != erhalteneAnzahlParameter )
				throw new SVMException("instr="+instr+": erwartete Parameter: "+erwarteteAnzahlParameter+"; erhalteneAnzahlParameter: "+erhalteneAnzahlParameter);
			int indexBuffer = 0, indexParameter = 0, anzahlParameter = instr.instruction().getAnzahlParameter();
			Short[] buf = new Short[instr.instruction().getInstrLenInWords(instr.instruction(), WORTLAENGE_IN_BYTES)]; // Short = 2 Byte len
			buf[indexBuffer] = (short) ((instr.instruction().getCode() << 8) | ( anzahlParameter > 0 ? instr.params()[indexParameter] : 0x0));
			LOGGER.trace("indexBuffer={}; indexParameter={}: {}", indexBuffer, indexParameter, SVM.BD_SHORT.getBinaerDarstellung(buf[indexBuffer]));
			for ( ++indexBuffer ; ++indexParameter < anzahlParameter; indexBuffer++ ) {
				buf[indexBuffer] = (short) ((instr.params()[indexParameter] << 8) | ( anzahlParameter > indexParameter ? instr.params()[indexParameter+1] : 0x0));
				indexParameter++;
				LOGGER.trace("indexBuffer={}; indexParameter={}: {}", indexBuffer, indexParameter, SVM.BD_SHORT.getBinaerDarstellung(buf[indexBuffer]));
			}
			LOGGER.trace("Fertig.");
			return buf;
		}
	}

	/** {@inheritDoc} */
	@Override
	public void writeInstruction(Instruction<Short> mem, int addr, InstructionDefinition<Short> instr) throws SVMException {
		try ( @SuppressWarnings("unused") CloseableThreadContext.Instance ctx =
				put("mem", requireNonNull(mem, "mem").toString()).put("instr", requireNonNull(instr, "instr").toString()).put("addr", String.valueOf(addr)) ) {
			Short[] instrInWords = instruction2Array(instr);
			LOGGER.trace("instrInWords={}", Arrays.toString(instrInWords));
			for ( short s : instrInWords )
				mem.write(addr++, s);
		}
	}

}
