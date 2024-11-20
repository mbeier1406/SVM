package com.github.mbeier1406.SVM.syscalls;

import com.github.mbeier1406.SVM.ALU;
import com.github.mbeier1406.SVM.MEM;
import com.github.mbeier1406.SVM.SVM;
import com.github.mbeier1406.SVM.SVMException;

/**
 * Definiert das Interface zu einem Aufruf in die Laufzeitumgebung.
 * Die Parameter entahlten die Werte der Register 2-4 (Register 1 enthält
 * den Code des Syscalls, der gerade ausgeführt wird) aus der {@linkplain ALU}.
 * @param <T> Die Wortgröße der {@linkplain ALU} und des Speicher {@linkplain MEM}
 * @see Syscall
 */
public interface SyscallInterface<T> {

	/**
	 * Definiert alle bekannten Systemaufrufe, d. h. die Funktionen
	 * aus der Laufzeitumgebung der {@linkplain SVM}, auf die ein
	 * ausgeführtes Programm Zugriff hat (z. B. I/O usw.).
	 * Alle bekannten Systemaufrufe werden mit ihrem Code (Parameter für
	 * {@linkplain com.github.mbeier1406.SVM.instructions.Syscall}.
	 * aufgelistet.
	 */
	public static enum Codes {
		EXIT((byte) Exit.class.getAnnotation(Syscall.class).code()),
		IO((byte) IO.class.getAnnotation(Syscall.class).code());
		private byte code;
		private Codes(byte code) {
			this.code = code;
		}
		public byte getCode() {
			return this.code;
		}
	};

	/**
	 * Führt einen Systemaufruf der Laufzeitumgebung der {@linkplain SVM} aus.
	 * Dieses kann beispielsweise EXIT (Beendigung des Programmms) oder I/O
	 * (Aus- oder Eingabe) sein.
	 * @param param1 erster Parameter des Syscalls aus Register 2 der {@linkplain ALU}
	 * @param param2 zweiter Parameter des Syscalls aus Register 3 der {@linkplain ALU}
	 * @param param3 dritter Parameter des Syscalls aus Register 4 der {@linkplain ALU}
	 * @return
	 * @throws SVMException
	 */
	public int execute(T param1, T param2, T param3) throws SVMException;

	/**
	 * Da die Systemaufrufe Zugriff auf bestimmte Funktionen der ALU benötigen, erhalten sie alle
	 * die notwendigen Funktionen zum Zugriff.
	 * @param mem Das Interface mit den Zugriffsmethoden für die {@linkplain ALU}
	 * @see SyscallBase
	 */
	public void setAlu(final ALU.Instruction<T> alu);

	/**
	 * Da die Systemaufrufe Zugriff auf bestimmte Funktionen des Hauptspeichers benötigen, erhalten sie alle
	 * die notwendigen Funktionen zum Zugriff.
	 * @param mem Das Interface mit den Speicherzugriffsmethoden
	 * @see SyscallBase
	 */
	public void setMemory(final MEM.Instruction<T> mem);

}
