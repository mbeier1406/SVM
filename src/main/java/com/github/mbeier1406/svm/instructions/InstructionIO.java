package com.github.mbeier1406.svm.instructions;

import java.util.Objects;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.SVM;

/**
 * Definiert grundlegende Funktionen für das Lesen und Schreiben von {@linkplain InstructionInterface Instruktionen}.
 * @param <T>
 */
public interface InstructionIO<T> {

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
