package com.github.mbeier1406.svm.prg.lexer;

import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenType;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenTypeLexer;

/**
 * Definiert die Funktion zur lexikalischen Analyse eines {@linkplain TokenType#COMMA}.
 */
public class CommaLexer {

	/**
	 * Funktion zur Verarbeitung eines Kommas:
	 * <ul>
	 * <li>In die Liste der {@linkplain Symbol}e wird {@linkplain SVMLexer#SYM_COMMA} eingefügt</li>
	 * <li>Wenn gerade ein Symbol gelesen wird (lastTokenType ist nicht <b>null</b> handelt es sich um einen Fehler</li>
	 * <li>Es wird wieder <b>null</b> zurückgegeben, da es sich um ein finales Token handelt</li>
	 * </ul>
	 * Das Komma trennt die Parameter einer {@linkplain InstructionInterface Instruktion}.
	 */
	@SuppressWarnings("unused")
	public static final TokenTypeLexer TOKEN_SCANNER = (symbolList, tokenValue, lastTokenType) -> {
		if ( lastTokenType != null ) {
			throw new IllegalArgumentException("Komma gefunden während folgendes Sysmbol gelesen wurde: "+lastTokenType);
		}
		else {
			symbolList.add(SVMLexer.SYM_COMMA);
		}
		return null; // Finales Token
	};

}
