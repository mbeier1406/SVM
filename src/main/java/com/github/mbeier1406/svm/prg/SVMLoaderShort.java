package com.github.mbeier1406.svm.prg;

import static org.apache.logging.log4j.CloseableThreadContext.put;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;

/**
 * 
 */
public class SVMLoaderShort implements SVMLoader<Short> {

	public static final Logger LOGGER = LogManager.getLogger(SVMLoaderShort.class);

	/**
	 * Die in {@linkplain #addData(com.github.mbeier1406.svm.prg.SVMProgram.Data)} übergebenen
	 * statischen Daten werden im {@linkplain MEM Hauptspeicher} beginnend ab Adresse {@linkplain MEM#getLowAddr()}
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

	/**
	 * Innerhalb des {@linkplain SVMProgram}s werden bei {@linkplain SVMProgram.Data Daten} und
	 * {@linkplain SVMProgram.VirtualInstruction Instruktionen} Label verwendet, die virtuelle
	 * Adressen im Programm repräsentieren. Diese müssen beim Einspielen des Programms mit
	 * konkreten Adressen im {@linkplain MEM Speicher} versehen werden, und alle Referenzen darauf
	 * in den virtuellen Instruktionen durch konkrete Adressen für die {@linkplain InstructionInterface}
	 * ersetzt werden.
	 */
	private Map<Label, Integer> labelList = new HashMap<>();


	/**	{@inheritDoc} */
	@Override
	public void load(final MEM<Short> mem, final SVMProgram<Short> svmProgram) throws SVMException {

		try ( CloseableThreadContext.Instance ctx = put("mem", mem.toString()).put("svmProgramm", svmProgram.toString()) ) {

			/* Schritt I: Sicherstellen, dass das Programm in sich konsistent ist und Initialisierung */
			LOGGER.debug("Validierung...");
			svmProgram.validate();

			/* Schritt II: Daten einspielen */
			for ( Data data : svmProgram.getDataList() ) {
				LOGGER.trace("dataAddr={}; data={}", this.dataAddr, data);
				this.labelList.put(data.label(), this.dataAddr);
				for ( int i=0; i < data.dataList().length; i++ )
					mem.getInstructionInterface().write(this.dataAddr+i, (Short) data.dataList()[i]);
				this.dataAddr += data.dataList().length;
				if ( this.dataAddr >= this.prgAddr )
					throw new SVMException("Zu viele Daten: dataAddr="+this.dataAddr+"; prgAddr="+this.prgAddr);
			}

			/* Schritt III: Programmcode einspielen */
			this.prgAddr = mem.getHighAddr();
			ctx.put("prgAddr", String.valueOf(this.prgAddr));
			LOGGER.debug("Code schreiben...");
			for ( var cmd : svmProgram.getInstructionList() ) {
			};

		}
		catch ( Exception e ) {
			
		}

	}

}
