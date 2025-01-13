package com.github.mbeier1406.svm.prg;

import static com.github.mbeier1406.svm.instructions.InstructionFactory.INSTRUCTIONS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.instructions.Int;

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
			final List<Label> labelListDaten = new ArrayList<>();

			/* Schritt I: Daten prüfen; Label eindeutig */
			LOGGER.trace("[Data] prüfen...");
			for ( int i=0; i < this.dataList.size(); i++ ) {
				Label labelZuPruefen = this.dataList.get(i).label();
				int indexOfLabel = labelListDaten.indexOf(labelZuPruefen);
				LOGGER.trace("[Data] labelZuPruefen={}; indexOfLabel={}: ", labelZuPruefen, indexOfLabel);
				if ( indexOfLabel >= 0 ) throw new SVMException("[Data] Index "+i+": Label "+labelZuPruefen+": Label doppelt (an Index "+indexOfLabel+")!");
				labelListDaten.add(labelZuPruefen);
			};

			/* Schritt II: Programm muss mindestens eine Instruktion enthalten  */
			LOGGER.trace("[Instr] prüfen...");
			if ( this.instructionList.isEmpty() ) throw new SVMException("[Instr] Leeres Programm: mindestens eine Instruktion wird erwartet!");

			/* Schritt III: Label als Ziel von Sprungbefehlen dürfen nicht doppel oder in den Daten verwendet worden sein */
			LOGGER.trace("[Instr] Ziel-Label prüfen...");
			final List<Label> labelListInstr = new ArrayList<>();
			for ( int i=0; i < this.instructionList.size(); i++ )
				if ( this.instructionList.get(i).label().isPresent() ) {
					Label labelZuPruefen = this.instructionList.get(i).label().get();
					int indexOfLabel = labelListDaten.indexOf(labelZuPruefen);
					LOGGER.trace("[Instr] in Daten: labelZuPruefen={}; indexOfLabel={}: ", labelZuPruefen, indexOfLabel);
					if ( indexOfLabel >= 0 ) throw new SVMException("[Instr] in Daten: Index "+i+": Label "+labelZuPruefen+": Label doppelt (an Index "+indexOfLabel+")!");
					indexOfLabel = labelListInstr.indexOf(labelZuPruefen);
					LOGGER.trace("[Instr] labelZuPruefen={}; indexOfLabel={}: ", labelZuPruefen, indexOfLabel);
					if ( indexOfLabel >= 0 ) throw new SVMException("[Instr] Index "+i+": Label "+labelZuPruefen+": Label doppelt (an Index "+indexOfLabel+")!");
					labelListInstr.add(labelZuPruefen);
				}

			/* Schritt IV: für alle in Instruktionen verwendeten Label muss es eine Definition geben */
			// TODO: Label muss zur Instruktion passen: JMP -> INSTRUCTION; SYSCALL IO -> DATA usw.
			LOGGER.trace("[Instr] Label-Referenzen prüfen...");
			for ( int i=0; i < this.instructionList.size(); i++ ) {
				var virtualInstruction = this.instructionList.get(i);
				for ( int j=0; j < virtualInstruction.labelList().length; j++ ) {
					Optional<Label> label = virtualInstruction.labelList()[j];
					if ( label.isPresent() ) {
						var labelName = label.get().label();
						LOGGER.trace("[Instr] {}; Index {}: pruefe Label '{}'", virtualInstruction, j, labelName);
						/*
						 * Da Wortlänge <Short> aus zwei Bytes (Byte[] = Parameterliste der Instruktion!!!) besteht, darf nur max. jedes zweite einen Label verwenden.
						 * Erster Parameter gehört immer zur Instruktion und verwendet keine Label
						 */
						if ( j%2 == 0 ) throw new SVMException("[Instr] "+virtualInstruction+" Index "+j+": Label "+labelName+": Label an diesem Index nicht erlaubt!");
						AtomicBoolean labelIstDefiniert = new AtomicBoolean(false);
						Stream.of(labelListDaten, labelListInstr).forEach(labelList -> {
							if ( labelList.stream().map(Label::label).filter(l -> l.equals(labelName)).findAny().isPresent() )
								labelIstDefiniert.set(true);
						});
						if ( !labelIstDefiniert.get() )
							throw new SVMException("[Instr] Index "+i+" ("+virtualInstruction+"): Label '"+labelName+"' ist nicht definiert!");
					}
				}
			}

			/* letzte Instruktion muss INT/Syscall sein (EXIT, Werte der Register werden aber nicht geprüft) */
			var anzInstr = this.instructionList.size();
			var lastInstr = this.instructionList.get(anzInstr-1);
			LOGGER.trace("[Instr] anzInstr={}; lastInstr={}", anzInstr, lastInstr);
			if ( !lastInstr.instruction().instruction().equals(INSTRUCTIONS.get(Int.CODE)) )
				throw new SVMException("[Instr] INT(1) wird als letzte Instruktion erwartet: "+lastInstr.instruction().instruction());

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
