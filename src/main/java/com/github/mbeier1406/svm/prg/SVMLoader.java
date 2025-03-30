package com.github.mbeier1406.svm.prg;

import java.util.HashMap;
import java.util.Map;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.ALU.Instruction;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.syscalls.IO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Lädt ein SVM-Programm in interner Repräsentation ({@linkplain SVMProgram}) zur Ausführung über
 * {@linkplain ALU#start()} in den Hauptspeicher ({@linkplain MEM}) einer {@linkplain SVM}.
 * Dazu müssen
 * <ol>
 * <li>die statischen Daten in den unteren Bereich des Speichers, und</li>
 * <li>der Programmcode von den oberen Adressen her</li>
 * </ol>
 * in den Speicher geladen werden. Hierbei müssen die im Programm verwendeten {@linkplain SVMProgram.Label}
 * durch konkrete Adressen ersetzt werden.
 * @see SVMProgram
 */
public interface SVMLoader<T> {

	/**
	 * Lädt das Programm in den Hauptspeicher, d. h. es wird {@linkplain InstructionInterface der Programmcode}
	 * von der {@linkplain MEM#getHighAddr() obersten Adresse} her absteigend kopiert, und die statischen Daten
	 * von der {@linkplain MEM#getLowAddr() untersten Adresse} her aufsteigend. Dabei müssen entsprechende
	 * Referenzen ({@linkplain Label}) durch die sich ergebende, tatsächliche Adresse ersetzt werden.
	 * Beispiel: der Syscall {@linkplain IO} verwendet die Adresse der auszugebenden Daten in
	 * {@linkplain Instruction#setRegisterValue(int, Object)}.
	 * @param mem der Speicher, in den das Programm geladen wird
	 * @param svmProgram das SVM-Programm in interner Darstellung
	 * @throws SVMException falls {@linkplain SVMProgram#validate(MEM)} fehlschlägt
	 */
	public void load(final MEM<T> mem, final SVMProgram<T> svmProgram) throws SVMException;

	/**
	 * Nach dem Laden eines {@linkplain SVMProgram}s müssen allen virtuellen Adressen ({@linkplain Label}n)
	 * die konkreten Adressen im Speicher zugeordnet sein. Diese Zuordnungstabelle wird während des
	 * Ladens mit {@linkplain #load(MEM, SVMProgram)} gepflegt.
	 * @return Liste der im geladenen Programm verwendeten labeln mit Adresse
	 */
	public Map<Label, Integer> getLabelList();

	/**
	 * Dieses Objekt speichert die {@linkplain Data Daten} und {@linkplain VirtualInstruction Instruktionen}
	 * eines {@linkplain SVMProgram SVM-Programms} (interne Darstellung) zusammen mit deren Adressen im {@linkplain MEM Speicher}
	 * nach dem Laden über {@linkplain SVMLoader#load(MEM, SVMProgram)}.<p/>
	 * Diese Information wird von der {@linkplain ALU} zur Anzeige von Debugging-Informationen verwendet.
	 * @param <T> Die Wortlänge der {@linkplain SVM}
	 * @see {@linkplain ALU#setDebugMode(boolean)}
	 */
	@Getter
	@ToString
	@NoArgsConstructor
	public static class DebuggingInfo<T> {
		/** Zuordnung Speicheradresse zu einem definierten Datenbereich */
		private final Map<Integer, Data<T>> dataAdresses = new HashMap<>();
		/** Zuordnung Speicheradresse zu einer Instruktion */
		private final Map<Integer, VirtualInstruction<T>> instructionAdresses = new HashMap<>();
	}

	/**
	 * Liefert die Liste der Original-SVM-Anweisungen und Daten mit den Adressen im Speicher {@linkplain MEM}.
	 * Diese wird zum Debugging in der {@linkplain ALU} verwendet.
	 */
	public DebuggingInfo<T> getDebuggingInfo();

}
