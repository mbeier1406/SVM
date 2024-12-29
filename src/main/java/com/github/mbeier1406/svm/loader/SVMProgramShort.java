package com.github.mbeier1406.svm.loader;

// import static com.github.mbeier1406.svm.loader.SVMProgram.LabelType.INSTRUCTION;
import static com.github.mbeier1406.svm.loader.SVMProgram.LabelType.DATA;
import static java.util.Objects.requireNonNull;
import static org.apache.logging.log4j.CloseableThreadContext.put;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.loader.SVMProgram.InstructionDefinition;

public class SVMProgramShort implements SVMProgram<Short> {

	public static final Logger LOGGER = LogManager.getLogger(SVMProgramShort.class);


	private List<SVMProgram.Label> labelList = new ArrayList<>();

	private List<InstructionDefinition<Short>> instructionList = new ArrayList<>();

	private List<Data<Short>> dataList = new ArrayList<>();

	/**
	 * Die in {@linkplain #addData(com.github.mbeier1406.svm.loader.SVMProgram.Data)} übergebenen
	 * statischen Daten werden im {@linkplain MEM Hauptspeicher} beginnend ab Adresse <b>0</b>
	 * aufsteigend abgelegt. Diese Variable speichert diejenige, an dem das nächste, übergebene
	 * Datum abgelegt werden soll. Es wird zusammen mit dem Bezeichner im {@linkplain Label}
	 * gespeichert und in der Liste {@linkplain #labelList} für den späteren Zugriff durch das
	 * Programm abgelegt. Die Adresse wächst also im Speicher "von unten nach oben" (zu den
	 * höheren Adressen), dem Programmcode entgegen.
	 */
	private int dataAddr = 0;


	/** {@inheritDoc} */
	@Override
	public void addCmd(InstructionDefinition<Short> instruction) throws SVMException {
		int anzahlParameterErwartet = requireNonNull(instruction, "instruction").instruction().getAnzahlParameter();
		int anzahlParameterErhalten = instruction.params().length;
		if ( anzahlParameterErwartet != anzahlParameterErhalten )
			throw new SVMException(
					"instruction="+instruction+
					" (Index "+this.instructionList.size()+")"+
					": erwartete Parameter "+anzahlParameterErwartet+
					"; erhalten "+anzahlParameterErhalten);
		this.instructionList.add(instruction);
		// this.labelList.add(new Label(INSTRUCTION, "", 0));
	}

	/** {@inheritDoc} */
	@Override
	public List<Short> instruction2Mem(final InstructionDefinition<Short> instructionDefinition) throws SVMException {
		return null;	
	}

	/** {@inheritDoc} */
	@Override
	public List<InstructionDefinition<Short>> getInstructionList() {
		return instructionList;
	}

	/** {@inheritDoc} */
	@Override
	public void addData(Data<Short> data) throws SVMException {
		var labelBereitsVorhanden = this.labelList
			.stream()
			.map(Label::label)
			.filter(l -> l.equals(requireNonNull(data, "data").label()))
			.findAny();
		if ( labelBereitsVorhanden.isPresent() )
			throw new SVMException("Label '" + data.label() +"' ("+data+") existiert bereits: "+this.labelList);
		this.dataList.add(data);
		this.labelList.add(new Label(DATA, data.label(), this.dataAddr)); // Das Datum wird später an dieser Adresse abgelegt
		this.dataAddr += data.data().length; // nächstes eingereichtes Datum wird direkt danach gespeichert
	}

	/** {@inheritDoc} */
	@Override
	public List<Data<Short>> getDataList() {
		return this.dataList;
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final MEM<Short> mem) throws SVMException {

		try ( @SuppressWarnings("unused") CloseableThreadContext.Instance ctx = put("mem", mem.toString()) ) {

			/* I: Prüfen, ob die Adressen aller Label sich in den Grenzen des Speichers befinden */
			LOGGER.trace("Prüfung I");
			this.labelList.forEach(label -> {
				if ( label.addr() < mem.getLowAddr() || label.addr() > mem.getHighAddr() )
					throw new IllegalArgumentException("label="+label+": ungültige Adresse!");
			});

		}
		catch ( Exception e ) {
			LOGGER.error("", e);
			throw new SVMException("mem="+mem+"; SVMProgramm="+this, e);
		}
	}

	@Override
	public String toString() {
		return "SVMProgramShort [labelList=" + labelList + ", instructionList=" + instructionList + ", dataList="
				+ dataList + ", dataAddr=" + dataAddr + "]";
	}

}
