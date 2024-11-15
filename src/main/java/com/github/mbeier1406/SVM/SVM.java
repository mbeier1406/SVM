package com.github.mbeier1406.SVM;

import java.net.URL;

/**
 * Definiert die Schnittstelle zur <i>Simple Virtual Machine</i> (SVM).
 * @see ALU ALU - die Arithmetisch-Logische Einheit (CPU) der Maschine
 */
public interface SVM {

	/**
	 * Führt ein Programm in der SVM aus.
	 * @param programm Lädt das auszuführend programm von dieser Adresse
	 * @return der Return-Codes des ausgeführten Programms
	 * @throws SVMException weist auf einen technischen fehler bei der Ausführung eines Programms hin
	 */
	public int run(URL programm) throws SVMException;

}
