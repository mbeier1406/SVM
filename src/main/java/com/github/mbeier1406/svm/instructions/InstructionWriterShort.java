package com.github.mbeier1406.svm.instructions;

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
		try ( CloseableThreadContext.Instance ctx = CloseableThreadContext.put("instr", instr.toString()) ) {
			int indexBuffer = 0, indexParameter = 0, anzahlParameter = instr.instruction().getAnzahlParameter();
			Short[] buf = new Short[instr.instruction().getInstrLenInWords(instr.instruction(), 2)]; // Short = 2 Byte len
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
		// TODO Auto-generated method stub
		
	}

}
