package com.github.mbeier1406.svm.prg.lexer;

import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPart;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPartLexer;

/**
 * Definiert die Funktion zur lexikalischen Analyse eines {@linkplain TokenPart#DOT}.
 */
public class DotLexer {

	/**
	 * Funktion zur Verarbeitung eines Punktes (<b>.</b>):
	 * <ul>
	 * <li>In die Liste der {@linkplain Symbol}e wird nichts eingefügt</li>
	 * <li>Wenn gerade ein Symbol gelesen wird (lastTokenType ist nicht <b>null</b> handelt es sich um einen Fehler</li>
	 * <li>Es wird {@linkplain TokenPart#DOT} zurückgegeben, damit aus dem nachfolgenden String der Label
	 * {@linkplain SVMLexer.Symbol} mit Token {@linkplain SVMLexer.Token#LABEL} gebildet werden kann</li>
	 * </ul>
	 * Das <b>.</b>-Zeichen leitet die Definition eines Labels/Bezeichners ein.
	 */
	@SuppressWarnings("unused")
	public static final TokenPartLexer TOKEN_SCANNER = (symbolList, tokenValue, lastTokenType) -> {
		if ( lastTokenType != null ) {
			throw new IllegalArgumentException("Dot (.) gefunden während folgendes Sysmbol gelesen wurde: "+lastTokenType);
		}
		else {
			// Nichts zu tun
		}
		return TokenPart.DOT; // Damit im nähsten Schritt der Label (.label) ermittelt werden kann
	};

}
