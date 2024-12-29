package com.github.mbeier1406.svm.loader;

import static org.apache.logging.log4j.CloseableThreadContext.put;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionInterface;

/**
 * 
 */
public class SVMLoaderShort implements SVMLoader<Short> {

	public static final Logger LOGGER = LogManager.getLogger(SVMLoaderShort.class);


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
			svmProgram.validate(mem);
			this.prgAddr = mem.getHighAddr();
	
			/* Schritt II: Programmcode einspielen */
			LOGGER.debug("Code schreiben...");
			for ( var cmd : svmProgram.getInstructionList() ) {
				int indexParameter = 0, anzahlParameter = cmd.instruction().getAnzahlParameter();
				short speicherWort = (short) ((cmd.instruction().getCode() << 8) | ( anzahlParameter > 0 ? cmd.params()[indexParameter] : 0x0));
				LOGGER.trace("prgAddr={}; indexParameter={}: {}", prgAddr, indexParameter, SVM.BD_SHORT.getBinaerDarstellung(speicherWort));
				mem.getInstructionInterface().write(this.prgAddr, speicherWort);
				for ( ; ++indexParameter < anzahlParameter; ) {
					
				}
			};

		}
		catch ( Exception e ) {
			
		}

	}

}
