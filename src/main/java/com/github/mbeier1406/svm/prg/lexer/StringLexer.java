package com.github.mbeier1406.svm.prg.lexer;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenType;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenTypeLexer;

/**
 * Definiert die Funktion zur lexikalischen Analyse eines {@linkplain TokenType#STRING}.
 */
public class StringLexer {

	/**
	 * Funktion zur Verarbeitung eines Strings:
	 * <ul>
	 * <li>In die Liste der {@linkplain Symbol}e wird je nach voranstehendem Qualifier ein Wert (z. B. {@linkplain SVMLexer#SYM_TOKEN_CODE}) eingefügt</li>
	 * <li>Ein String ohne Qualifier erzeugt einen Fehler</li>
	 * <li>Es wird wieder <b>null</b> zurückgegeben, da es ein finales Token ist</li>
	 * </ul>
	 */
	@SuppressWarnings("unused")
	public static final TokenTypeLexer TOKEN_SCANNER = (symbolList, tokenValue, lastTokenType) -> {
		if ( lastTokenType != null ) {
			if ( lastTokenType == TokenType.AMPERSAND ) {
				if ( tokenValue.equals("data") )
					symbolList.add(SVMLexer.SYM_TOKEN_DATA);
				else if ( tokenValue.equals("code") )
					symbolList.add(SVMLexer.SYM_TOKEN_CODE);
				else
					throw new SVMException("Nach '"+TokenType.AMPERSAND+"' muss eine Sektion (data/code) folgen: "+tokenValue);
			}
		}
		else {
			throw new SVMException("Vor einem '"+TokenType.STRING+"' ("+tokenValue+") muss ein Qualifier (&, .) stehen!");
		}
		return null; // Token fertig
	};

}
