package com.github.mbeier1406.svm.prg.parser.instructions;

import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

/**
 * Bietet einige Hilfsfunktionen für das erzeugen virtueller Instruktionen
 */
public class Helper {

	/**
	 * Erzeugt aus einem Label aus der externen Programmrepräsentation
	 * einen Label für eine {@linkplain VirtualInstruction}.
	 * @param label der Label aus dem Programmtext (kann <b>null</b> sein)
	 * @return der Label für die virtuelle Instruktion oder <b>null</b>, falls kein Symbol übergeben wurde
	 */
	public static Label getLabel(Symbol label) {
		return label == null ? null : new Label(LabelType.INSTRUCTION, label.value());
	}

}
