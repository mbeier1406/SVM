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
 * Der Instruction-Pointer (IP) der {@linkplain ALU} zeigt immer auf den Beginn eines Speicherwortes.
 * Endet eine Instruktion mit Parametern also nicht genau am Ende eines Speicheworts, dann werden
 * beim Laden eines Programms die restlichen Bytes mit Null aufgefüllt.<p/>
 * <p><u>Beispiele</u></p>
 * <h3>Maschinenbefehl ohne Parameter bei Wortlänge {@linkplain Short}</h3>
 * <pre></code>
 * | Speicherwort 1         |
 * | Byte 1        | Byte 2 |
 * | &lt;Instruction> | &lt;NULL> |
 * </code></pre>
 * <h3>Maschinenbefehl mit einem Parameter bei Wortlänge {@linkplain Short}</h3>
 * <pre></code>
 * | Speicherwort 1                |
 * | Byte 1        | Byte 2        |
 * | &lt;Instruction> | &lt;Parameter 1> |
 * </code></pre><p/>
 * <h3>Maschinenbefehl mit zwei Parametern bei Wortlänge {@linkplain Short}</h3>
 * <pre></code>
 * | Speicherwort 1                | Speicherwort 2         |
 * | Byte 1        | Byte 2        | Byte 3        | Byte 4 |
 * | &lt;Instruction> | &lt;Parameter 1> | &lt;Parameter 2> | &lt;NULL> |
 * </code></pre><p/>
 *
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
	 * Liefert die Anzahl der Parameter des Maschinenbefehls (änge jeweils 1 Byte).
	 * Der Instructionpointer der {@linkplain ALU}
	 * wird nach Ausführung des Befehls um die folgende Anzahl von Speicherworten
	 * erhöht, um auf den danach folgenden Befehl zu zeigen:
	 * <p>{@code (1+<Anzahl Parameter>)/<Wortlänge Speicher> + (mod((1+<Anzahl Parameter>), <Wortlänge Speicher>) > 0 ? 1 : 0)}</p>
	 * Sobald also der letzte Parameter die Wortlänge des Speichers überschreitet, wird der rest des Worts mit Nullen aufgefüllt und
	 * die nächste Instruction beginnt beim nächsten Speicherwort.
	 * @return Anzahl der Parameter mit Länge Bytes
	 */
	public T getAnzahlParameter();

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
