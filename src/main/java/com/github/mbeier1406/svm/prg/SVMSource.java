package com.github.mbeier1406.svm.prg;

import java.util.List;

import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.instructions.InstructionInterface;

public interface SVMSource<T> {

	/**
	 * Diese Methode übernimmt die interne Repräsentation einer {@linkplain InstructionInterface Instruktion}
	 * mit ihrenen Parametern und berechnet daraus die Repräsentation im {@linkplain MEM} als Liste von
	 * Hauptspeicherworten.
	 * @param instructionDefinition die Instruktion mit Parametern
	 * @return Liste der Speicherworte
	 * @throws SVMException falls die Anzahl der Parameter nicht zur Länge der Parameterliste passt
	 */
	public List<T> instruction2Mem(final InstructionDefinition<T> instructionDefinition) throws SVMException;


}
