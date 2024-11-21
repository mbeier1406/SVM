package com.github.mbeier1406.svm.instructions;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;

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
	 * Liefert die Länge des Maschinenbefehls (mit seinen Parametern) in Anzahl
	 * Speicherworten (Typ <b>T</b>). Der Instructionpointer der {@linkplain ALU}
	 * wird nach Ausführung des Befehls um die angegebene Anzahl der Speicherworte
	 * erhöht, um auf den danach folgenden Befehl zu zeigen.
	 * @return Länge der Instruktion in Anzahl Speicherworten
	 */
	public T getLength();

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
