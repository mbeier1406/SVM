package com.github.mbeier1406.svm.prg;

import static com.github.mbeier1406.svm.prg.SVMProgram.LabelType.DATA;
import static org.apache.logging.log4j.CloseableThreadContext.put;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;

/**
 * 
 */
public class SVMLoaderShort implements SVMLoader<Short> {

	public static final Logger LOGGER = LogManager.getLogger(SVMLoaderShort.class);


	/**
	 * Die in {@linkplain #addData(com.github.mbeier1406.svm.prg.SVMProgram.Data)} übergebenen
	 * statischen Daten werden im {@linkplain MEM Hauptspeicher} beginnend ab Adresse <b>0</b>
	 * aufsteigend abgelegt. Diese Variable speichert diejenige, an dem das nächste, übergebene
	 * Datum abgelegt werden soll. Es wird zusammen mit dem Bezeichner im {@linkplain Label}
	 * gespeichert und in der Liste {@linkplain #labelList} für den späteren Zugriff durch das
	 * Programm abgelegt. Die Adresse wächst also im Speicher "von unten nach oben" (zu den
	 * höheren Adressen), dem Programmcode entgegen.
	 */
	private int dataAddr = 0;



	/**
	 * Speichert die Adresse, an die der nächste {@linkplain InstructionInterface SVM-Befehl}
	 * gespeichert werden soll. Diese beginnt immer an der höchsten Adresse des {@linkplain MEM Speichers}
	 * und wird absteigend gespeichert.
	 */
	private int prgAddr;

	/**	{@inheritDoc} */
	@Override
	public void load(final MEM<Short> mem, final SVMProgram<Short> svmProgram) throws SVMException {

		try ( @SuppressWarnings("unused") CloseableThreadContext.Instance ctx = put("mem", mem.toString()).put("svmProgramm", svmProgram.toString()) ) {

			/* Schritt I: Sicherstellen, dass das Programm in sich konsistent ist und Initialisierung */
			LOGGER.debug("Validierung...");
			svmProgram.validate();
//			/* I: Prüfen, ob die Adressen aller Label sich in den Grenzen des Speichers befinden */
//			LOGGER.trace("Prüfung I");
//			this.labelList.forEach(label -> {
//				if ( label.addr() < mem.getLowAddr() || label.addr() > mem.getHighAddr() )
//					throw new IllegalArgumentException("label="+label+": ungültige Adresse!");
//			});

//			var labelBereitsVorhanden = this.labelList
//					.stream()
//					.map(Label::label)
//					.filter(l -> l.equals(requireNonNull(data, "data").label()))
//					.findAny();
//				if ( labelBereitsVorhanden.isPresent() )
//					throw new SVMException("Label '" + data.label() +"' ("+data+") existiert bereits: "+this.labelList);
//				this.dataList.add(data);
//				this.labelList.add(new Label(DATA, data.label(), this.dataAddr)); // Das Datum wird später an dieser Adresse abgelegt
//				this.dataAddr += data.data().length; // nächstes eingereichtes Datum wird direkt danach gespeichert

			this.prgAddr = mem.getHighAddr();
	
			/* Schritt II: Programmcode einspielen */
			LOGGER.debug("Code schreiben...");
			for ( var cmd : svmProgram.getInstructionList() ) {
			};

		}
		catch ( Exception e ) {
			
		}

	}

}
