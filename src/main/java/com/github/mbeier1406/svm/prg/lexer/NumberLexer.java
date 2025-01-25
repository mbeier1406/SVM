package com.github.mbeier1406.svm.prg.lexer;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPart;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPartLexer;

/**
 * Definiert die Funktion zur lexikalischen Analyse eines {@linkplain TokenPart#NUMBER}.
 */
public class NumberLexer {

	/**
	 * Funktion zur Verarbeitung einer Zahl:
	 * <ul>
	 * <li>In die Liste der {@linkplain Symbol}e wird ein Wert mit der Zahl eingefügt</li>
	 * <li>Eine Zahl ohne vorangestelltes Dollar-Zeichen ($) erzeugt einen Fehler</li>
	 * <li>Es wird wieder <b>null</b> zurückgegeben, da es ein finales Token ist</li>
	 * </ul>
	 * Eine Zahl bildet einen Parameter einer {@linkplain InstructionDefinition Instruktion}.
	 */
	public static final TokenPartLexer TOKEN_SCANNER = (symbolList, tokenValue, lastTokenType) -> {
		if ( lastTokenType != null && lastTokenType == TokenPart.DOLLAR ) {
			symbolList.add(new SVMLexer.Symbol(SVMLexer.Token.CONSTANT, tokenValue));
		}
		else if ( lastTokenType != null && lastTokenType == TokenPart.PERCENT ) {
			symbolList.add(new SVMLexer.Symbol(SVMLexer.Token.REGISTER, tokenValue));
		}
		else {
			throw new SVMException("Vor einem '"+TokenPart.NUMBER+"' ("+tokenValue+") muss ein Dollar-Zeichen ($) stehen!");
		}
		return null; // Token fertig
	};

}
