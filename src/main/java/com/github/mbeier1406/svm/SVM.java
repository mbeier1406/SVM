package com.github.mbeier1406.svm;

import java.net.URL;

/**
 * Definiert die Schnittstelle zur <i>Simple Virtual Machine</i> (SVM).
 * @see ALU ALU - die Arithmetisch-Logische Einheit (CPU) der Maschine
 */
public interface SVM {

	/** Für die Protokollierung der Speicherinhalte */
	public static final BinaerDarstellung<Short> BD_SHORT = new BinaerDarstellung<>();

	/** Für die Protokollierung der Codes von Maschinenbefehlen */
	public static final BinaerDarstellung<Byte> BD_BYTE = new BinaerDarstellung<>();


	/**
	 * Führt ein Programm in der SVM aus.
	 * @param programm Lädt das auszuführend programm von dieser Adresse
	 * @return der Return-Codes des ausgeführten Programms
	 * @throws SVMException weist auf einen technischen fehler bei der Ausführung eines Programms hin
	 */
	public int run(URL programm) throws SVMException;

}
