package com.github.mbeier1406.svm;

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
		 * Dieser Aufruf wird von der Instruktion {@linkplain com.github.mbeier1406.Int.instructions.Syscall}
		 * in Zusammenhang mit dem Syscall {@linkplain com.github.mbeier1406.SVM.syscalls.Exit} verwendet,
		 * um das Stop-Flag zum Beenden der SVM zu bewirken.<br/>
		 * Der <i>Return-Code</i> mit welchem die SVM beendet werden soll, wird aus dem Register
		 * {@linkplain #getRegisterValue(int)} <b>0</b> gelesen und muss entsprechend zuvor gesetzt sein.
		 */
		public void setStopFlag();

		/**
		 * Setzt das angegebene Register auf den übergebenen Wert.
		 * @param register Das Register (0..)
		 * @param value Den Wert
		 * @throws SVMException wenn ein ungültiges Register angegeben wird
		 */
		public void setRegisterValue(int register, T value) throws SVMException;

		/**
		 * Liefert den Wert, den das angegebene Register enthält (10..).
		 * @param register Nummer des registers
		 * @return Den Wert des Registers
		 * @throws SVMException wenn ein ungültiges Register angegeben wird
		 */
		public T getRegisterValue(int register) throws SVMException;
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

	/** Liefert die Referenz auf den Hauptspeicher, den die ALU benutzt */
	public MEM<T> getMEM();

}
