package com.github.mbeier1406.svm.instructions;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Liest eine {@linkplain InstructionInterface Instruction} (einen Maschinenbefehl)
 * aus dem Hauptspeicher {@linkplain MEM} ein.
 * @param <T> Die Wortlänge der {@linkplain SVM}
 */
public interface InstructionReaderInterface<T> {

	/**
	 * Liefert den nächsten, auszuführenden Maschinenbefehl aus dem Hauptspeicher.
	 * @param mem Die Referenz auf den Speicher, es darf nur {@linkplain MEM.Instruction#read(int)} aufgerufen werden!
	 * @param addr Die Adresse, auf die der InstructionPointer der {@linkplain ALU} gerade zeigt.
	 * @return die nächste auszuführende Instruction
	 * @throws SVMException wenn es sich um eine unbekannte Instruction handelt oder einen ungültigen Speicherbereich
	 */
	public InstructionDefinition<T> getInstruction(final MEM.Instruction<T> mem, int addr) throws SVMException;

}
