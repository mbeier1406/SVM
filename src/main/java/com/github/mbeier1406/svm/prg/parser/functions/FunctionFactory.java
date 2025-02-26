package com.github.mbeier1406.svm.prg.parser.functions;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;

/**
 * Beim Parsen einen {@linkplain SVMProgram}s in externer Form (Beispiel
 * {@code /SVM/src/test/resources/com/github/mbeier1406/svm/prg/example.svm})
 * können Funktionen verwendet werden, die das Schreiben von SVM-Programmen
 * erleichtern. Beispiel {@code len()} für die Länge eines Datenbereichs:
 * <pre><code>
 * 	&data
 * ...
 * .text2
 * 	XY
 * ...
 * 	&code
 * ...
 * 	mov <b>len(text2)</b>, %3
 * ...
 * </code></pre>
 * Diese Klasse liefert für den Namen einer Funktion die entsprechende Logik.
 * Funktionen liefern immer einen {@linkplain Integer} als Wert.
 * @see Function
 */
public class FunctionFactory {

	/**
	 * Dieses Interface definiert die Signator der Funktionen,
	 * die in SVM-programmen verwendet werdne können.
	 */
	@FunctionalInterface
	public interface Function {
		/**
		 * Wendet eine Funktion an und liefert das Ergebnis.
		 * @param symbol Das Symbol zwischen den Klammern der Funktion, zB bei {@code len(text2)} ein {@linkplain SVMLexer.Token#LABEL_REF} mit Wert <i>test2</i>
		 * @param svmProgram Das SVM-programm, das gerade erstellt wird
		 * @return den berechneten Wert
		 * @throws SVMException Falls das Symbol nicht zur Funktion passt, ungültige Label referenziert werden usw.
		 */
		public int apply(final Symbol symbol, final SVMProgram<Short> svmProgram) throws SVMException;
	}

	/**
	 * Diese Liste enthält die bekannten SVM-Parser Funktionen
	 */
	@SuppressWarnings({ "serial" })
	private static final Map<String, Function> MAP = new HashMap<String, Function>() {{

		/*
		 * Die Funktion len() erhält ein Symbol mit einer Labelreferenz als Parameter
		 * und liefert die Länge der zugehörigen Datendefinition als Wert.
		 * Beispiel: /SVM/src/test/resources/com/github/mbeier1406/svm/prg/example.svm
		 * 	&data
		 * ...
		 * .text2
		 * 	XY
		 * ...
		 * &code
		 * ...
		 * 	mov len(text2), %3
		 * ...
		 * Liefert 2 ale Länge für "XY".
		 */
		put("len", ( symbol, svmProgram ) -> {
			if ( requireNonNull(symbol, "symbol").token().equals(Token.LABEL_REF) )
				throw new SVMException("len(): symbol "+symbol+": es wird ein Symbol mit folgendem Token erwartet: "+Token.LABEL_REF);
			return requireNonNull(svmProgram, "svmProgram")
				.getDataList()
				.stream()
				.filter(dat -> dat.label().label().equals(symbol.getStringValue().get()))
				.findAny()
				.orElseThrow(() -> new SVMException("len(): symbol "+symbol+" den Label gibt es nicht in svmProgram="+svmProgram))
				.dataList()
				.length;
		});

	}};


	/**
	 * Liefert die Parserfunktion für den vorgegebenen Namen.
	 * @param f der Name der Funktion, zB <i>len</i>, darf nicht null sein
	 * @return die Funktion
	 * @throws SVMException falls es für den Funktionsnamen keine Implementierung gibt
	 */
	public static Function getFunction(String f) throws SVMException {
		return Optional
				.ofNullable(MAP.get(requireNonNull(f, "Keine Funktion angegeben!")))
				.orElseThrow(() -> new SVMException("Keine Funktion '"+f+"' definiert!"));
	}

}
