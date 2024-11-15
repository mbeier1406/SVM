package com.github.mbeier1406.SVM;

/**
 * Definiert die Schnittstelle zum (Haupt-)Speicher der {@linkplain SVM}. Die Größe des Hauptspeichers
 * wird über desse Adressierbarkeit ({@linkplain Long} festgelegt. Der Einfachheit haber kann immer nur
 * ein Speicherwort geschrieben oder gelesen werden.<p/>
 * Im Speicher wird von der obersten Adresse her das auszuführende Programm abgelegt. Von der untersten
 * (niedrigensten) Adresse her wächst der Stack.<p/>
 * <b>Offen</b>: Heap Memory wird in dieser Version noch nicht implementiert. 
 * @param <T> Legt die Wortgröße des Speichers fest, z. B. {@linkplain Short} für eine 16-Bit-Wort Speicher
 */
public interface MEM<T> {

	/**
	 * Liest ein Speicherwort von der angegebenen Adresse.
	 * @param addr die Speicheradresse, von der ein Wort gelesen werden soll
	 * @return Das Wort an der vorgegebenen Speicheradresse
	 * @throws SVMException wenn von einer ungültige Adresse gelesen wird
	 */
	public T read(long addr) throws SVMException;

	/**
	 * Speichert ein Wort an der vorgegebenen Adresse.
	 * @param addr die Adresse, an die geschrieben werden soll.
	 * @param data die zu schreibenden Daten
	 * @throws SVMException wenn an eine ungültige Adresse geschrieben wird
	 */
	public void write(long addr, T data) throws SVMException;

	/**
	 * Liefert die oberste Adresse des Hauptspeichers.
	 * Hier beginnt jeweils die Programmausführung.
	 * @return die oberste Adresse
	 */
	public long getHighAddr();

	/**
	 * Liefert die niedrigste Adresse des Hauptspeichers.
	 * Hier beginnt der erste Stackframe des auszuführenden Programms.
	 * @return die niedrigste Adresse
	 */
	public default long getLowAddr() {
		return 0L;
	}

}
