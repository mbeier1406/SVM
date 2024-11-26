package com.github.mbeier1406.svm.instructions;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Definiert das Interface zu einem von der {@linkplain ALU} ausführbaren Maschinebefehl.
 * Eine solche Instruktion ist immer ein Byte lang (also max. 256 Instruktionen) und bildet
 * den ersten Teil des Speicherwortes, auf den der Instructionpointer der {@linkplain ALU}
 * gerade zeigt. Gefolgt wird das Byte von n-Bytes, die die Parameter zum Maschinenbefehl
 * bilden und ggf. weitere, direkt folgende, Speicherworte (je nach Größe des Speicherworts
 * <b>T</b>) benötigen.
 * <pre></code>
 * | Speicherwort I                                    | Speicherwort II    | ...
 * | &lt;Instruction>[&lt;Parameter I>[&lt;Parameter II>[...]]] | [&lt;Parameter n>]... | ...
 * </code></pre><p/>
 * @param <T> Die Wortgröße der {@linkplain ALU} und des Speicher {@linkplain MEM}
 * @see Instruction
 */
public interface InstructionInterface<T> {

	/**
	 * Diese Aufzählung bietet eine Zuordnung des Codes (das byte im {@linkplain MEM Speicher},
	 * das die nächste, auszuführende {@linkplain InstructionInterface Instruktion} enthält)
	 * zu alle bekannten Maschinenbefehlen/Instruktionen.
	 * Die {@linkplain InstructionFactory} kann prinzipiell auch weitere Instruktionen liefern,
	 * von diesen müsste dann aber der entsprechende Code über die Annotation {@linkplain Instruction#code()}
	 * ausgelesen werden.
	 */
	public static enum Codes {
		NOP((byte) Nop.class.getAnnotation(Instruction.class).code()),
		INT((byte) Int.class.getAnnotation(Instruction.class).code());
		private byte code;
		private Codes(byte code) {
			this.code = code;
		}
		public byte getCode() {
			return this.code;
		}
	}

	/**
	 * Liefert die Länge des Maschinenbefehls (mit seinen Parametern) in Anzahl
	 * Speicherworten (Typ <b>T</b>). Der Instructionpointer der {@linkplain ALU}
	 * wird nach Ausführung des Befehls um die angegebene Anzahl der Speicherworte
	 * erhöht, um auf den danach folgenden Befehl zu zeigen.
	 * @return Länge der Instruktion in Anzahl Speicherworten
	 */
	public T getLength();

	/**
	 * Führt den Maschinenbefehl/die Instruktion aus. Als Parameter werden die im
	 * Hauptspeicher direkt dem Byte mit dem Maschinenbefehl ({@linkplain Instruction#code()}
	 * folgendes Bytes übergeben. Die Länge dieses Feldes ist maschinebefehlsspezifisch und
	 * ergibt sich aus der Ausgabe von <code>{@linkplain #getLength()}-1</code> (minus eins
	 * da dieses Byte den Maschinebefehl identifiziert.
	 * @param params
	 * @return ein befehlsspezifichen Return-Code
	 * @throws bei technischen (internen) Fehlern
	 */
	public int execute(final byte[] params) throws SVMException;

	/**
	 * Da die Maschinenbefehle Zugriff auf bestimmte Funktionen der ALU benötigen, erhalten sie alle
	 * die notwendigen Funktionen zum Zugriff.
	 * @param mem Das Interface mit den Zugriffsmethoden für die {@linkplain ALU}
	 * @see InstructionBase
	 */
	public void setAlu(final ALU.Instruction<T> alu);

	/**
	 * Da die Maschinenbefehle Zugriff auf bestimmte Funktionen des Hauptspeichers benötigen, erhalten sie alle
	 * die notwendigen Funktionen zum Zugriff.
	 * @param mem Das Interface mit den Speicherzugriffsmethoden
	 * @see InstructionBase
	 */
	public void setMemory(final MEM.Instruction<T> mem);

}
