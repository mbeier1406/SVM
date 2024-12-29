package com.github.mbeier1406.svm.loader;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.ALU.Instruction;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.loader.SVMProgram.Label;
import com.github.mbeier1406.svm.syscalls.IO;

/**
 * Lädt ein SVM-Programm in interner Repräsentation ({@linkplain SVMProgram}) zur Ausführung über
 * {@linkplain ALU#start()} in den Hauptspeicher ({@linkplain MEM} einer {@linkplain SVM}
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

}
