package com.github.mbeier1406.svm.prg.lexer;

import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPart;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPartLexer;

/**
 * Definiert die Funktion zur lexikalischen Analyse eines {@linkplain TokenPart#DOLLAR}.
 */
public class DollarLexer {

	/**
	 * Funktion zur Verarbeitung eines Dollar-Zeichens (<b>$</b>):
	 * <ul>
	 * <li>In die Liste der {@linkplain Symbol}e wird nichts eingefügt</li>
	 * <li>Wenn gerade ein Symbol gelesen wird (lastTokenType ist nicht <b>null</b> handelt es sich um einen Fehler</li>
	 * <li>Es wird {@linkplain TokenPart#DOLLAR} zurückgegeben, damit aus dem nachfolgenden String der Label
	 * {@linkplain SVMLexer.Symbol} mit Token {@linkplain SVMLexer.Token#LABEL} gebildet werden kann</li>
	 * </ul>
	 * Das <b>$</b>-Zeichen leitet die Definition einer (ganzen) Zahl ein.
	 */
	@SuppressWarnings("unused")
	public static final TokenPartLexer TOKEN_SCANNER = (symbolList, tokenValue, lastTokenType) -> {
		if ( lastTokenType != null ) {
			throw new IllegalArgumentException("Dollar ($) gefunden während folgendes Sysmbol gelesen wurde: "+lastTokenType);
		}
		else {
			// Nichts zu tun
		}
		return TokenPart.DOLLAR; // Damit im nähsten Schritt die Zahlenkonstante ermittelt werden kann
	};

}
