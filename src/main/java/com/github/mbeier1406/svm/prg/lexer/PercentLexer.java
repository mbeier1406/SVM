package com.github.mbeier1406.svm.prg.lexer;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPart;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenTypeLexer;

/**
 * Definiert die Funktion zur lexikalischen Analyse eines {@linkplain TokenPart#PERCENT}.
 */
public class PercentLexer {

	/**
	 * Funktion zur Verarbeitung eines Prozent-Zeichens (<b>%</b>):
	 * <ul>
	 * <li>In die Liste der {@linkplain Symbol}e wird nichts eingefügt</li>
	 * <li>Wenn gerade ein Symbol gelesen wird (lastTokenType ist nicht <b>null</b> handelt es sich um einen Fehler</li>
	 * <li>Es wird {@linkplain TokenPart#PERCENT} zurückgegeben, damit aus dem nachfolgenden String die Nummer des Registers
	 * {@linkplain ALU.Instruction#setRegisterValue(int, Object)} im {@linkplain SVMLexer.Symbol} mit Token
	 * {@linkplain SVMLexer.Token#REGISTER} gebildet werden kann</li>
	 * </ul>
	 * Das <b>%</b>-Zeichen leitet die Definition eines Registers der {@linkplain ALU} ein.
	 */
	@SuppressWarnings("unused")
	public static final TokenTypeLexer TOKEN_SCANNER = (symbolList, tokenValue, lastTokenType) -> {
		if ( lastTokenType != null ) {
			throw new IllegalArgumentException("Prozent (%) gefunden während folgendes Sysmbol gelesen wurde: "+lastTokenType);
		}
		else {
			// Nichts zu tun
		}
		return TokenPart.PERCENT; // Damit im nähsten Schritt die nummer des registers werden kann
	};

}
