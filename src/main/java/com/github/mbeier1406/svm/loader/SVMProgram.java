package com.github.mbeier1406.svm.loader;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.instructions.InstructionInterface;

/**
 * Definiert alle Datenstrukturen und Methoden zur Speicherung
 * und zum Laden eines durch die {@linkplain SVM} ausführbaren Programms.
 * Es handelt sich um die interne Repräsentation.
 */
public interface SVMProgram<T> {

	public static enum LabelType { INSTRUCTION, DATA };

	public static record Label(LabelType labelType, String label, int addr) {
		public Label {
			if ( requireNonNull(label, "label").isEmpty() ) throw new IllegalArgumentException("label");
		}
	}

	public void addInstruction(final InstructionDefinition<T> instruction) throws SVMException;

	/**
	 * Diese Methode übernimmt die interne Repräsentation einer {@linkplain InstructionInterface Instruktion}
	 * mit ihrenen Parametern und berechnet daraus die Repräsentation im {@linkplain MEM} als Liste von
	 * Hauptspeicherworten.
	 * @param instructionDefinition die Instruktion mit Parametern
	 * @return Liste der Speicherworte
	 * @throws SVMException falls die Anzahl der Parameter nicht zur Länge der Parameterliste passt
	 */
	public List<T> instruction2Mem(final InstructionDefinition<T> instructionDefinition) throws SVMException;

	public List<InstructionDefinition<T>> getInstructionList();

	public static record Data<T>(String label, T[] data) {
		public Data {
			if ( requireNonNull(label, "label").isEmpty() ) throw new IllegalArgumentException("label");
			if ( requireNonNull(data, "data").length == 0 ) throw new IllegalArgumentException("data");
			for ( int i=0; i < data.length; i++ )
				requireNonNull(data[i], "data["+i+"]");
		}
	}

	public void addData(final Data<T> data) throws SVMException;

	public List<Data<T>> getDataList();

	/**
	 * Stellt sicher, dass das Programm in sich konsitent ist (z. B. für jede Referenz/jeden Label
	 * auch eine Definition existiert usw.) und der Zielspecher groß genug ist, das Programm zu speichern.
	 * @param mem der Zielspeicher, in den das Programm geladen werden soll
	 * @throws SVMException bei fehlerhaften Programmdefinitionen mit einer entsprechenden Fehlermeldung
	 */
	public void validate(final MEM<T> mem) throws SVMException;

}
