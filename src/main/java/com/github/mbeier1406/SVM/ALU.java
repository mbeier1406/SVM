package com.github.mbeier1406.SVM;

/**
 * Definiert die Schnittstelle zu Arithmetisch-, logischen Einheit (ALU),
 * also zur CPU der SVM. Die in der ALU verwendeten Register sollen der Einfachheit
 * halber die gleiche Wortgröße die die des Hauptspeichers {@linkplain MEM} haben.
 */
public interface ALU<T> {

	/**
	 * Die Funktionen in diesem Teil des Interfaces sind für den Zugriff
	 * der Machinenbefehle in {@linkplain Instruction} vorgesehen.
	 * Diese Befehle sollen nur eingeschränkten Zugriff auf die Funktionen der ALU haben.
	 * @param <T> Legt die Wortgröße der Register in der ALU fest
	 */
	public interface Instruction<T> {
		/**
		 * Dieser Aufruf wird von der Instruktion {@linkplain com.github.mbeier1406.SVM.instructions.Syscall}
		 * in Zusammenhang mit dem Syscall {@linkplain com.github.mbeier1406.SVM.syscalls.Exit} verwendet,
		 * um das Stop-Flag zum Beenden der SVM zu bewirken.
		 * @param returnCode mit welchem Return-Code die SVM beendet werdne soll
		 */
		public void setStopFlag(T returnCode);
	}

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
