package com.github.mbeier1406.svm.prg;

import java.util.Map;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.ALU.Instruction;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.syscalls.IO;

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
	 * ladens mit {@linkplain #load(MEM, SVMProgram)} gepflegt.
	 * @return Liste der im geladenen Programm verwendeten labeln mit Adresse
	 */
	public Map<Label, Integer> getLabelList();

}
