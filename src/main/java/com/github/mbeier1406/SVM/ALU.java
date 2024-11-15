package com.github.mbeier1406.SVM;

/**
 * Definiert die Schnittstelle zu Arithmetisch-, logischen Einheit (ALU),
 * also zur CPU der SVM. Die in der ALU verwendeten Register sollen der Einfachheit
 * halber die gleiche Wortgröße die die des Hauptspeichers {@linkplain MEM} haben.
 */
public interface ALU<T> {

	/**
	 * Initialisiert die ALU:
	 * <ul>
	 * <li>Der IP (instruction pointer) wird auf die oberste Adresse des {@linkplain MEM} gesetzt</li>
	 * <li>Der SP (stack pointer) und der BP (branch pointer) wird auf die unterste Adresse des {@linkplain MEM} gesetzt</li>
	 * </ul>
	 * <u>Hinweis:</u>: der Zustand der Register ist auch nach Aufruf von {@linkplain #init()} nicht definiert.
	 */
	public void init();

	/**
	 * Startet das zuvor mit {@linkplain SVM#run(java.net.URL)} geladene Programm.
	 * @return den Return-Code des geladenen Programms
	 * @throws SVMException Falls bei der Ausführung des Programms ein Fehler auftrat
	 */
	public int start() throws SVMException;

}
