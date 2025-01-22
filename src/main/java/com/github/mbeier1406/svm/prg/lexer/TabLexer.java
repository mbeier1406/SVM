package com.github.mbeier1406.svm.prg.lexer;

import com.github.mbeier1406.svm.prg.SVMLexer;
import com.github.mbeier1406.svm.prg.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.SVMLexer.TokenType;
import com.github.mbeier1406.svm.prg.SVMLexer.TokenTypeParser;

/**
 * Definiert die Funktion zur lexikalischen Analyse eines {@linkplain TokenType#TAB}.
 */
public class TabLexer {

	/**
	 * Funktion zur Verarbeitung eines Leerzeichens:
	 * <ul>
	 * <li>In die Liste der {@linkplain Symbol}e wird {@linkplain SYM_TAB} eingefügt</li>
	 * <li>Wenn gerade ein Symbol gelesen wird (lastTokenType ist nicht <b>null</b> handelt es sich um einen Fehler</li>
	 * <li>Es wird wieder <b>null</b> zurückgegeben, da es sich um ein finales Token handelt</li>
	 * </ul>
	 */
	@SuppressWarnings("unused")
	public static final TokenTypeParser TOKEN_PORCESSOR = (symbolList, tokenValue, lastTokenType) -> {
		if ( lastTokenType != null ) {
			throw new IllegalArgumentException("Tabualtor gefunden während folgendes Sysmbol gelesen wurde: "+lastTokenType);
		}
		else {
			symbolList.add(SVMLexer.SYM_TAB);
		}
		return null; // Finales Token
	};

}
