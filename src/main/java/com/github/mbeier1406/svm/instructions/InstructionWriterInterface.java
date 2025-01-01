package com.github.mbeier1406.svm.instructions;

import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Schreibt eine {@linkplain InstructionInterface Instruction} (einen Maschinenbefehl)
 * in dem Hauptspeicher {@linkplain MEM} bzw. erzeugt die Byte-Folge, die in den Speicher
 * geschrieben wird.
 * @param <T> Die Wortlänge der {@linkplain SVM}
 */
public interface InstructionWriterInterface<T> {

	/**
	 * Erzeugt aus einer Instruktion und deren Parameter eine Bytefolge, die in den Hauptspeicher
	 * geschrieben werden kann.
	 * @param instr die Instruktion und die Parameterliste der Instruktion
	 * @return Liste der Speicherwörter aus {@linkplain InstructionInterface#getCode() Code} und Parametern
	 * @throws SVMException wenn die Parameterliste nicht zur Instruktion passt
	 */
	public T[] instruction2Array(final InstructionDefinition<T> instr) throws SVMException;

	/**
	 * Schreibt eine Instruktion mit Parametern in den Hauptspeicher.
	 * @param mem der Hauptspeicher
	 * @param addr die Adresse, an die geschrieben werden soll
	 * @param instr die zu schreibende Instruktion und die Parameterliste zur Instruktion
	 * @throws SVMException wenn die Parameterliste nicht zur Instruktion passt oder es sich um oder einen ungültigen Speicherbereich handelt
	 * @see #instruction2Array(InstructionDefinition, byte[])
	 */
	public void writeInstruction(final MEM.Instruction<T> mem, int addr, final InstructionDefinition<T> instr) throws SVMException;

}
