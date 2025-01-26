package com.github.mbeier1406.svm.prg.lexer;

import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPart;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPartLexer;

/**
 * Definiert die Funktion zur lexikalischen Analyse eines {@linkplain TokenPart#RIGHTPAR}.
 */
public class RightparLexer {

	/**
	 * Funktion zur Verarbeitung einer schließenden Klammer:
	 * <ul>
	 * <li>In die Liste der {@linkplain Symbol}e wird {@linkplain SVMLexer#SYM_RIGHTPAR} eingefügt</li>
	 * <li>Wenn gerade ein Symbol gelesen wird (lastTokenType ist nicht <b>null</b> handelt es sich um einen Fehler</li>
	 * <li>Es wird wieder <b>null</b> zurückgegeben, da es sich um finales Token handelt</li>
	 * </ul>
	 * Die schließende Klammer wird für Funktionsaufrufe verwendet..
	 */
	@SuppressWarnings("unused")
	public static final TokenPartLexer TOKEN_SCANNER = (symbolList, tokenValue, lastTokenType) -> {
		if ( lastTokenType != null ) {
			throw new IllegalArgumentException("Schließende Klammer gefunden während folgendes Sysmbol gelesen wurde: "+lastTokenType);
		}
		else {
			symbolList.add(SVMLexer.SYM_RIGHTPAR);
		}
		return null;
	};

}
