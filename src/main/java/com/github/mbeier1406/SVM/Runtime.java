package com.github.mbeier1406.SVM;

import com.github.mbeier1406.SVM.instructions.Instruction;

/**
 * Diese Schnittstelle definiert die Funktionen der Laufzeitumgebung (Betriebssystem)
 * auf die ien in der {@linkplain SVM} ausgeführtes Programm Zugriff hat.
 * @param <T> Die Wortgröße von Prozessor (ALU) und Speicher (MEM).
 * @see ALU
 * @see MEM
 */
public interface Runtime<T> {

	/**
	 * Definiert alle bekannten Systemaufrufe, d. h. die Funktionen
	 * aus der Laufzeitumgebung der {@linkplain SVM}, auf die ein
	 * ausgeführtes Programm Zugriff hat (z. B. I/O usw.).
	 */
	public enum SyscallCode {
		/** Beendet das gerade ausgeführte Programm */
		EXIT((byte) 0x1, "EXIT");
		private byte code;
		private String name;
		private SyscallCode(byte code, String name) {
			this.code = code;
			this.name = name;
		}
		public String toString() {
			return name;
		}
		/** Wandelt einen Befehl aus {@linkplain Instruction} in einen {@linkplain SyscallCode} um */
		public static SyscallCode getSyscallCode(byte code) throws SVMException {
			for ( SyscallCode c : SyscallCode.values() )
				if ( code == c.code )
					return c;
			throw new SVMException("Kein Syscall für code: "+code);
		}
	}

	/**
	 * Führt den in {@code code} definierten Systemaufruf mit den entsprechendne Parametern aus.
	 * Nicht jeder <i>Syscall</i> verwendet alle Parameter, entsprechend können diese ignoriert
	 * werden. Es ist Verantwortung des Aufrufers, die für die jeweils verwendete Funktion passenden
	 * Parameter mitzuliefern. Beispiele:
	 * <ul>
	 * <li>code <code>0x1</code>: exit - beendet das Programm / die Ausführung in {@linkplain SVM#run(java.net.URL)}
	 * 	<ul>
	 * 		<li><code>param1</code>: der exit-code für die Ausführung</li>
	 * 	</ul>
	 * </li>
	 * </ul>
	 * 
	 * @param code der Code des auszuführenden Aufrufs (aus Register 1 der {@linkplain ALU})
	 * @param param1 Paramter 1 zum Aufruf (aus Register 2 der {@linkplain ALU})
	 * @param param2 Paramter 2 zum Aufruf (aus Register 3 der {@linkplain ALU})
	 * @param param3 Paramter 3 zum Aufruf (aus Register 4 der {@linkplain ALU})
	 * @return liefert eine Code, der den Erfolg des jeweiligen Aufrufs anzeigt
	 * @throws SVMException bei technischen Fehlern
	 */
	public int syscall(SyscallCode code, T param1, T param2, T param3) throws SVMException;

}
