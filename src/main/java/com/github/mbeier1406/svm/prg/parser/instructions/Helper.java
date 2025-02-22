package com.github.mbeier1406.svm.prg.parser.instructions;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;

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

	/**
	 * 
	 * @param index
	 * @param given
	 * @param expected
	 * @throws SVMException
	 */
	public static void checkParameterSymbol(int index, final Token given, final Token[] expected) throws SVMException {
		if ( !Arrays.asList(requireNonNull(expected, "expected")).contains(requireNonNull(given, "given")) )
			throw new SVMException("Index "+index+": erhaltenes Token: "+given+"; erwartet: "+Arrays.toString(expected));
	}

}
