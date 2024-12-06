package com.github.mbeier1406.svm;

/**
 * Definiert die Schnittstelle zum (Haupt-)Speicher der {@linkplain SVM}. Die Größe des Hauptspeichers
 * wird über desse Adressierbarkeit ({@linkplain Integer} festgelegt. Der Einfachheit haber kann immer nur
 * ein Speicherwort geschrieben oder gelesen werden.<p/>
 * Im Speicher wird von der obersten Adresse her das auszuführende Programm abgelegt. Von der untersten
 * (niedrigensten) Adresse her wächst der Stack.<p/>
 * <b>Offen</b>: Heap Memory wird in dieser Version noch nicht implementiert. 
 * @param <T> Legt die Wortgröße des Speichers fest, z. B. {@linkplain Short} für eine 16-Bit-Wort Speicher
 */
public interface MEM<T> {

	/**
	 * Die Funktionen in diesem Teil des Interfaces sind für den Zugriff
	 * der Machinenbefehle in {@linkplain Instruction} vorgesehen.
	 * Diese Befehle sollen nur eingeschränkten Zugriff auf den Hauptspecher haben.
	 * Dies gilt zum Beispiel für die Instruction {@linkplain com.github.mbeier1406.Int.instructions.Syscall},
	 * die beispielsweise den Syscall {@linkplain com.github.mbeier1406.SVM.syscalls.IO} aufruft, der
	 * aus dem Hauptspeicher liest.
	 * @param <T> Legt die Wortgröße des Speichers fest
	 */
	public interface Instruction<T> {
		/**
		 * Liest ein Speicherwort von der angegebenen Adresse.
		 * @param addr die Speicheradresse, von der ein Wort gelesen werden soll
		 * @return Das Wort an der vorgegebenen Speicheradresse
		 * @throws SVMException wenn von einer ungültige Adresse gelesen wird
		 */
		public T read(int addr) throws SVMException;
	
		/**
		 * Speichert ein Wort an der vorgegebenen Adresse.
		 * @param addr die Adresse, an die geschrieben werden soll.
		 * @param data die zu schreibenden Daten
		 * @throws SVMException wenn an eine ungültige Adresse geschrieben wird
		 */
		public void write(int addr, T data) throws SVMException;
	}

	/**
	 * Liefert die oberste Adresse des Hauptspeichers.
	 * Hier beginnt jeweils die Programmausführung.
	 * @return die oberste Adresse
	 */
	public int getHighAddr();

	/**
	 * Liefert die niedrigste Adresse des Hauptspeichers.
	 * Hier beginnt der erste Stackframe des auszuführenden Programms.
	 * @return die niedrigste Adresse
	 */
	public default int getLowAddr() {
		return 0;
	}

	/**
	 * Liefert den Inhalt des Speichers in {@linkplain BinaerDarstellung} an einer
	 * vorgegebenen Adresse mit einer definierten Länge in Binärdarstellung.
	 * @param addr Adresse, ab der der Inhalt ausgegeben werdne soll
	 * @param len Länge der Ausgabe in Speicherwörtern
	 * @return den Inhalt als Binärstring
	 * @throws SVMException wenn von einer ungültige Adresse gelesen wird
	 */
	public String getBinaryContentStringAt(int addr, int len) throws SVMException;

}
