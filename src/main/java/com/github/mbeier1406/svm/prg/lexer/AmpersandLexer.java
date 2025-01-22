package com.github.mbeier1406.svm.prg.lexer;

import com.github.mbeier1406.svm.prg.SVMLexer;
import com.github.mbeier1406.svm.prg.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.SVMLexer.TokenType;
import com.github.mbeier1406.svm.prg.SVMLexer.TokenTypeParser;

/**
 * Definiert die Funktion zur lexikalischen Analyse eines {@linkplain TokenType#AMPERSAND}.
 */
public class AmpersandLexer {

	/**
	 * Funktion zur Verarbeitung eines Ampersand (<b>&</b>):
	 * <ul>
	 * <li>In die Liste der {@linkplain Symbol}e wird nichts eingefügt</li>
	 * <li>Wenn gerade ein Symbol gelesen wird (lastTokenType ist nicht <b>null</b> handelt es sich um einen Fehler</li>
	 * <li>Es wird {@linkplain TokenType#AMPERSAND} zurückgegeben, damit aus dem nachfolgenden String die Sektion
	 * (z. B. {@linkplain SVMLexer#SYM_TOKEN_CODE}) ermittelt werden kann</li>
	 * </ul>
	 */
	@SuppressWarnings("unused")
	public static final TokenTypeParser TOKEN_PORCESSOR = (symbolList, tokenValue, lastTokenType) -> {
		if ( lastTokenType != null ) {
			throw new IllegalArgumentException("Ampersand (&) gefunden während folgendes Sysmbol gelesen wurde: "+lastTokenType);
		}
		else {
			// Nichts zu tun
		}
		return TokenType.AMPERSAND; // Damit im nähsten Schritt die Sektion (&data, &code) ermittelt werdne kann
	};

}
