package com.github.mbeier1406.SVM;

import com.github.mbeier1406.SVM.syscalls.IO;
import com.github.mbeier1406.SVM.syscalls.SyscallInterface;

/**
 * Diese Schnittstelle definiert die Funktionen der Laufzeitumgebung (Betriebssystem)
 * auf die ien in der {@linkplain SVM} ausgeführtes Programm Zugriff hat.<p/>
 * Folgende Funktionen werden bereitgestellt:
 * <ul>
 * 	<li>
 * 		Bereitstellung einer temporären Datei (wird mit Beenden der SVM gelöscht), in die der
 * 		Syscall {@linkplain IO} schreiben kann
 * 	</li>
 * 	<li>
 * 		Zugriff auf Systemfunktionen über {@linkplain #syscall(Object, Object, Object, Object)};
 * 	</li>
 * </ul>
 * @param <T> Die Wortgröße von Prozessor (ALU) und Speicher (MEM).
 * @see ALU
 * @see MEM
 */
public interface Runtime<T> {


	/**
	 * Führt den in {@code code} definierten Systemaufruf mit den entsprechendne Parametern aus.
	 * Nicht jeder <i>Syscall</i> verwendet alle Parameter, entsprechend werden diese ggf. von
	 * dem jeweiligen Syscall ignoriert.
	 * Es ist Verantwortung des Aufrufers, die für die jeweils verwendete Funktion passenden
	 * Parameter mitzuliefern. Beispiele:
	 * @param code der Code des auszuführenden Aufrufs (aus Register 1 der {@linkplain ALU})
	 * @param param1 Paramter 1 zum Aufruf (aus Register 2 der {@linkplain ALU})
	 * @param param2 Paramter 2 zum Aufruf (aus Register 3 der {@linkplain ALU})
	 * @param param3 Paramter 3 zum Aufruf (aus Register 4 der {@linkplain ALU})
	 * @return liefert eine Code, der den Erfolg des jeweiligen Aufrufs anzeigt
	 * @throws SVMException bei technischen Fehlern
	 * @see SyscallInterface
	 */
	public int syscall(T code, T param1, T param2, T param3) throws SVMException;

}
