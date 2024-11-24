package com.github.mbeier1406.svm.instructions;

import java.util.HashMap;
import java.util.Map;

import com.github.mbeier1406.svm.syscalls.SyscallFactory;
import com.github.mbeier1406.svm.syscalls.SyscallInterface;

/**
 * Dieses Interface definiert die externen Module, die über
 * die {@linkplain Int}-Instruktion aufgerufen werden können.
 */
public interface IntInterface<T> {

	/**
	 * Die {@linkplain Int}-Instruktion erhält einen Parameter, der
	 * einen Code darstellt, welcher das Modul identifziert, das die
	 * weitere Programmausführung übernimmt. Es wird der Code und
	 * das Modul (die Factory) mit der {@linkplain Map} definiert,
	 * welches die Serviceklasse enthält, die die weitere Ausführung
	 * übernimmt. Identifiziert wird diese durch den Wert in <u>Register 1</u>.<p/>
	 * Beispiel:<br/>
	 * {@code INT 0x1} ruft einen {@linkplain SyscallInterface Syscall} auf.
	 * Der Wert in Register 1 identifiziert den auszuführenden
	 * {@linkplain com.github.mbeier1406.svm.syscalls.Syscall#code() Syscall}.
	 * Beispielsweise {@code 0x2} für
	 * {@linkplain com.github.mbeier1406.svm.syscalls.IO eine Ausgabe}.
	 * <p>Servicemodul und -funktion</p>
	 * Es wird vorausgesetzt und über die <i>Reflection-API</i> im Servicemodul
	 * des Interrupts eine Methode mit folgender Signatur aufgerufen:<br/>
	 * {@code public int execute(T param1, T param2, T param3) throws SVMException;}
	 * Diese erhält die Werte aus register 2-4 als Paramter.
	 */
	public enum ModulCodes {
		SYSCALLS((byte) 0x1, SyscallFactory.toIntMap());
		private byte modulCode;
		private Map<Byte, IntInterface<Short>> functions;
		private ModulCodes(Byte modulCode, Map<Byte, IntInterface<Short>> functions) {
			this.modulCode = modulCode;
			this.functions = functions;
		}
		public byte getModulCode() {
			return this.modulCode;
		}
		public Map<Byte, IntInterface<Short>> getFunctions() {
			return this.functions;
		}
	};

	/** Definiert die über {@linkplain Int} aufrufbaren Module mit dem Paramter für den {@code INT}-Maschinenbefehl */
	@SuppressWarnings("serial")
	public Map<Byte, ModulCodes> MODULES = new HashMap<Byte, ModulCodes>() {{
		put((byte) 0x1, ModulCodes.SYSCALLS);
	}};

}
