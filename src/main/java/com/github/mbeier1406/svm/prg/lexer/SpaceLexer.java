package com.github.mbeier1406.svm.prg.lexer;

import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPart;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPartLexer;

/**
 * Definiert die Funktion zur lexikalischen Analyse eines {@linkplain TokenPart#SPACE}.
 */
public class SpaceLexer {

	/**
	 * Funktion zur Verarbeitung eines Leerzeichens:
	 * <ul>
	 * <li>In die Liste der {@linkplain Symbol}e wird nichts eingefügt</li>
	 * <li>Wenn gerade ein Symbol gelesen wird (lastTokenType ist nicht <b>null</b> bzw. ungleich {@linkplain TokenPart#SPACE} handelt es sich um einen Fehler</li>
	 * <li>Es wird wieder {@linkplain TokenPart#SPACE} zurückgegeben, da mehrere Leerzeichen hintereinander erlaubt sind</li>
	 * </ul>
	 * Das Leerzeichen trennt {@linkplain TokenGroupLexer Tokengruppen}.
	 */
	@SuppressWarnings("unused")
	public static final TokenPartLexer TOKEN_SCANNER = (symbolList, tokenValue, lastTokenType) -> {
		if ( lastTokenType != null ) {
			if ( lastTokenType != TokenPart.SPACE )
				throw new IllegalArgumentException("Leerezeichen gefunden während folgendes Sysmbol gelesen wurde: "+lastTokenType);
		}
		else {
			// Nichts zu tun
		}
		return TokenPart.SPACE; // Damit mehrere Leerzeichen hintereinander gelesen werden können
	};

}
