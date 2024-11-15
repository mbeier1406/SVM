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

}
