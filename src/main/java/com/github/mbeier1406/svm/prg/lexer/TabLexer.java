package com.github.mbeier1406.svm.prg.lexer;

import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPart;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPartLexer;

/**
 * Definiert die Funktion zur lexikalischen Analyse eines {@linkplain TokenPart#TAB}.
 */
public class TabLexer {

	/**
	 * Funktion zur Verarbeitung eines Leerzeichens:
	 * <ul>
	 * <li>In die Liste der {@linkplain Symbol}e wird {@linkplain SYM_TAB} eingefügt</li>
	 * <li>Wenn gerade ein Symbol gelesen wird (lastTokenType ist nicht <b>null</b> handelt es sich um einen Fehler</li>
	 * <li>Es wird wieder {@linkplain TokenPart#TAB} zurückgegeben, da es sich um ein einleitendes Token handelt</li>
	 * </ul>
	 * Ein Tabulator leitet die Definition einer Sektion (z. B. {@linkplain SVMLexer#SYM_TOKEN_DATA}) oder einer
	 * {@linkplain InstructionDefinition Instruktion}, oder Daten, ... ein.
	 */
	@SuppressWarnings("unused")
	public static final TokenPartLexer TOKEN_SCANNER = (symbolList, tokenValue, lastTokenType) -> {
		if ( lastTokenType != null ) {
			throw new IllegalArgumentException("Tabualtor gefunden während folgendes Sysmbol gelesen wurde: "+lastTokenType);
		}
		else {
			// nichts zu tun
		}
		return TokenPart.TAB; // Beginn eines Labels, einer Sektion, einer Instruktion, Daten usw.
	};

}
