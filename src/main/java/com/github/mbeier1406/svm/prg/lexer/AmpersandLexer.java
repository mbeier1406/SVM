package com.github.mbeier1406.svm.prg.lexer;

import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenType;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenTypeLexer;

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
	 * Der Ampersand <b>&</b> leitet die Definition einer Programmsektion ({@code &data} bzw. {@code &code}) ein.
	 */
	@SuppressWarnings("unused")
	public static final TokenTypeLexer TOKEN_SCANNER = (symbolList, tokenValue, lastTokenType) -> {
		if ( lastTokenType != null ) {
			throw new IllegalArgumentException("Ampersand (&) gefunden während folgendes Sysmbol gelesen wurde: "+lastTokenType);
		}
		else {
			// Nichts zu tun
		}
		return TokenType.AMPERSAND; // Damit im nähsten Schritt die Sektion (&data, &code) ermittelt werdne kann
	};

}
