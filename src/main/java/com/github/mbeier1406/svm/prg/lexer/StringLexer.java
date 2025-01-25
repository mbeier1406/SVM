package com.github.mbeier1406.svm.prg.lexer;

import java.util.Arrays;
import java.util.Optional;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPart;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPartLexer;

/**
 * Definiert die Funktion zur lexikalischen Analyse eines {@linkplain TokenPart#STRING}.
 */
public class StringLexer {

	/**
	 * Funktion zur Verarbeitung eines Strings:
	 * <ul>
	 * <li>In die Liste der {@linkplain Symbol}e wird je nach voranstehendem Qualifier ein Wert (z. B. {@linkplain SVMLexer#SYM_TOKEN_CODE}) eingefügt</li>
	 * <li>Ein String ohne Qualifier erzeugt einen Fehler</li>
	 * <li>Es wird wieder <b>null</b> zurückgegeben, da es ein finales Token ist</li>
	 * </ul>
	 * Eine Zeichenkette bildet den Teil einer Sektion (z. B. {@linkplain SVMLexer#SYM_TOKEN_CODE}) oder Labels ({@linkplain SVMLexer.Token#LABEL}).
	 */
	public static final TokenPartLexer TOKEN_SCANNER = (symbolList, tokenValue, lastTokenType) -> {
		if ( lastTokenType != null ) {
			if ( lastTokenType == TokenPart.AMPERSAND ) {	// Sektion (&code oder &data gefunden)
				if ( tokenValue.equals("data") )
					symbolList.add(SVMLexer.SYM_TOKEN_DATA);
				else if ( tokenValue.equals("code") )
					symbolList.add(SVMLexer.SYM_TOKEN_CODE);
				else
					throw new SVMException("Nach '"+TokenPart.AMPERSAND+"' muss eine Sektion (data/code) folgen: '"+tokenValue+"'!");
			}
			else if ( lastTokenType == TokenPart.DOT ) {	// Label .<NAME> gefunden
				symbolList.add(new SVMLexer.Symbol(SVMLexer.Token.LABEL, tokenValue));
			}
			else if ( lastTokenType == TokenPart.TAB ) {	// Instruktions- oder Datendefinition
				Optional<String> cmd = Arrays.stream(InstructionInterface.Codes.values())
						.map(i -> i.toString().toLowerCase())
						.peek(System.out::println)
						.filter(name -> name.equals(tokenValue))
						.findAny();
				symbolList.add(new SVMLexer.Symbol(cmd.isPresent()?SVMLexer.Token.CODE:SVMLexer.Token.DATA, tokenValue));
			}
			else
				throw new SVMException("Nach TokenPart '"+lastTokenType+"' darf kein String folgen: "+tokenValue);
		}
		else {
			throw new SVMException("Vor einem '"+TokenPart.STRING+"' ("+tokenValue+") muss ein Qualifier (&, .) stehen!");
		}
		return null; // Token fertig
	};

}
