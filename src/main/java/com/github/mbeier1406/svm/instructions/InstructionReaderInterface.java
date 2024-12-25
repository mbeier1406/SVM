package com.github.mbeier1406.svm.instructions;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Liest eine {@linkplain InstructionInterface Instruction} (einen Maschinenbefehl)
 * aus dem Hauptspeicher {@linkplain MEM} ein.InstructionInterface<T> ins
 * @param <T> Die Wortlänge der {@linkplain SVM}
 */
public interface InstructionReaderInterface<T> {

	/**
	 * Wenn eine Instruktion mit {@linkplain InstructionReaderInterface#getInstruction(com.github.mbeier1406.svm.MEM.Instruction, int)}
	 * eingelesen sird, muss einerseits die {@linkplain InstructionInterface Instruktion} selber, und deren Länge (mit Paramteren) in
	 * Wortlänge der {@linkplain SVM} geliefert werden (eine Instruktion "weiß" das nicht, da sie die Wortlänge des Speichers nicht kennt).
	 * Diese Länge wird benötigt, der der Instructionpointer der {@linkplain ALU} um diesen Betrag verschoben werdne muss, um auf die
	 * nächste, auszuführende Instruktion zu zeigen.
	 * @param <T> Wortlänge der {@linkplain SVM}
	 */
	public static record InstructionDefinition<T>(InstructionInterface<T> instr, int len) {
		public InstructionDefinition {
			requireNonNull(instr, "instruction");
		}
	}

	/**
	 * Liefert den nächsten, auszuführenden Maschinenbefehl aus dem Hauptspeicher.
	 * @param mem Die Referenz auf den Speicher, es darf nur {@linkplain MEM.Instruction#read(int)} aufgerufen werden!
	 * @param addr Die Adresse, auf die der InstructionPointer der {@linkplain ALU} gerade zeigt.
	 * @return die nächste auszuführende Instruction
	 * @throws SVMException wenn es sich um eine unbekannte Instruction handelt oder einen ungültigen Speicherbereich
	 */
	public InstructionDefinition<T> getInstruction(final MEM.Instruction<T> mem, int addr) throws SVMException;

	/**
	 * Berechnet, wie viele Speicherwörter eine Instruktion benötigt (ggf. sind überzählige Bytes zum Ende eines Speicherwortes
	 * mit Nullen aufgefüllt). Beispiel: Wortlänge der {@linkplain SVM} ist {@linkplain Short} (zwei Bytes) und eine Instruktion
	 * hat zwei Parameter: Länge ist drei (1 Byte Code der Instruktion und zwei bytes für zwei Paraemter), es werden zwei
	 * Speicherwörter (gleich vier Bytes) benötigt.
	 * @param instruction die {@linkplain InstructionInterface Instruktion}, deren Länge berechnet werden soll.
	 * @param wordLenInBytes Wortlänge der {@linkplain ALU}
	 * @return Anzahl Speicherwörter, die die Instruktion benötigt
	 */
	public default int getInstrLenInWords(InstructionInterface<T> instruction, int wordLenInBytes) {
		return (1 + Objects.requireNonNull(instruction, "instruction").getAnzahlParameter()) / wordLenInBytes +
				(((1 + instruction.getAnzahlParameter()) % wordLenInBytes) > 0 ? 1 : 0);
	}

}
