package com.github.mbeier1406.svm.prg;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionInterface;

/**
 * Dies Klasse speichert die interne Repräsentation eines Programms, das durch die
 * {@linkplain SVM} ausgeführt werden kann. Es enthält die Liste der {@linkplain InstructionInterface Instruktionen}
 * und {@linkplain SVMProgram#addData(com.github.mbeier1406.svm.prg.SVMProgram.Data) Daten}.
 */
public class SVMProgramShort implements SVMProgram<Short> {

	public static final Logger LOGGER = LogManager.getLogger(SVMProgramShort.class);

	/** Die Liste der virtuellen Instruktionen (Adressen sind ggf. noch nicht definiert; {@linkplain Label} */
	private List<VirtualInstruction<Short>> instructionList = new ArrayList<>();

	/** Liste (statischer) Daten, werden über {@linkplain Label} adressiert */
	private List<Data<Short>> dataList = new ArrayList<>();

	/** {@inheritDoc} */
	@Override
	public void addInstruction(VirtualInstruction<Short> instruction) {
		LOGGER.trace("Index {}: instruction={}", this.instructionList.size(), instruction);
		this.instructionList.add(instruction);
	}

	/** {@inheritDoc} */
	@Override
	public List<VirtualInstruction<Short>> getInstructionList() {
		return this.instructionList;
	}

	/** {@inheritDoc} */
	@Override
	public void addData(Data<Short> data) {
		LOGGER.trace("Index {}: data={}", this.dataList.size(), data);
		this.dataList.add(data);
	}

	/** {@inheritDoc} */
	@Override
	public List<Data<Short>> getDataList() {
		return this.dataList;
	}

	/** {@inheritDoc} */
	@Override
	public void validate() throws SVMException {
		try {
			LOGGER.debug("SVMProgramm={}", this);
			final List<Label> labelList = new ArrayList<>();

			/* Schritt I: Daten prüfen; Label eindeutig */
			for ( int i=0; i < this.dataList.size(); i++ ) {
				Label labelZuPruefen = this.dataList.get(i).label();
				int indexOfLabel = labelList.indexOf(labelZuPruefen);
				LOGGER.trace("labelZuPruefen={}; indexOfLabel={}: ", labelZuPruefen, indexOfLabel);
				String s = "[Data] Index "+i+": Label "+labelZuPruefen;
				if ( indexOfLabel >= 0 ) throw new SVMException(s+": Label doppelt (an Index "+indexOfLabel+")!");
				labelList.add(labelZuPruefen);
			};

//			int anzahlParameterErwartet = requireNonNull(instruction, "instruction").instruction().getAnzahlParameter();
//			int anzahlParameterErhalten = instruction.params().length;
//			if ( anzahlParameterErwartet != anzahlParameterErhalten )
//				throw new SVMException(
//						"instruction="+instruction+
//						" (Index "+this.instructionList.size()+")"+
//						": erwartete Parameter "+anzahlParameterErwartet+
//						"; erhalten "+anzahlParameterErhalten);

			// TODO letzte Instruktion syscall exit
		}
		catch ( Exception e ) {
			LOGGER.error("{}", this, e);
			if ( e instanceof SVMException ) throw e;
			throw new SVMException("SVMProgramm="+this, e);
		}
	}

	@Override
	public String toString() {
		return "SVMProgramShort [instructionList=" + instructionList + ", dataList=" + dataList + "]";
	}

}
