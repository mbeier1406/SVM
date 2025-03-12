package com.github.mbeier1406.svm;

import com.github.mbeier1406.svm.cmd.CommandInterface;
import com.github.mbeier1406.svm.cmd.SVMCli;
import com.github.mbeier1406.svm.impl.SVMShort;

/**
 * Definiert die Schnittstelle zur <i>Simple Virtual Machine</i> (SVM).
 * @see SVMShort
 */
public interface SVM {

	/** Für die Protokollierung der Speicherinhalte */
	public static final BinaerDarstellung<Short> BD_SHORT = new BinaerDarstellung<>();

	/** Für die Protokollierung der Codes von Maschinenbefehlen */
	public static final BinaerDarstellung<Byte> BD_BYTE = new BinaerDarstellung<>();


	/**
	 * Startet die {@linkplain SVMCli CLI} der SVM zur Eingabe von {@linkplain CommandInterface Kommandos}.
	 * @throws SVMException weist auf einen technischen Fehler bei der Ausführung von Kommandos hin
	 */
	public void start() throws SVMException;

}
