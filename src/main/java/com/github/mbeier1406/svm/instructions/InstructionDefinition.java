package com.github.mbeier1406.svm.instructions;

import static java.util.Objects.requireNonNull;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.SVM;

/**
 * Neben dem eigentlichen Maschinenbefehl/der Instruktion (definiert in {@linkplain InstructionInterface}
 * gehören auch desse/deren Parameter (als Array von Bytes) zur vollständigen Darstellung im Speicher
 * <p>
 * Wenn eine Instruktion mit {@linkplain InstructionReaderInterface#getInstruction(com.github.mbeier1406.svm.MEM.Instruction, int)}
 * eingelesen sird, muss einerseits die {@linkplain InstructionInterface Instruktion} selber, die zu übergebenden Parameter
 * (als Liste von Bytes) und die Länge der Instruktion (mit Paramteren) im Speicher in Wortlänge der {@linkplain SVM}
 * geliefert werden (eine Instruktion "weiß" das nicht, da sie die Wortlänge des Speichers nicht kennt).
 * Diese Länge wird benötigt, der der Instructionpointer der {@linkplain ALU} um diesen Betrag verschoben werdne muss, um auf die
 * nächste, auszuführende Instruktion zu zeigen.
 * </p>
 * <p>
 * Schreiben einer Instruktion:
 * </p>
 * der {@linkplain SVM}.
 * @param instruction die Instruktion / der Maschinenbefehl
 * @param params Liste der Parameter zur Instruktion
 * @param lenInWords die gesamte Länge der Instruktion mit Parametern in Wortlänge der SVM
 * @param <T> Die Wortlänge der {@linkplain SVM}
 */
public record InstructionDefinition<T>(InstructionInterface<T> instruction, byte[] params, int lenInWords) {
	public InstructionDefinition {
		requireNonNull(instruction, "instruction");
		requireNonNull(params, "params");
	}
}

