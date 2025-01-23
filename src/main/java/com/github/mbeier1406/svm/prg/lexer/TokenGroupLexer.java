package com.github.mbeier1406.svm.prg.lexer;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenType;

/**
 * Definiert die Funktion zur lexikalischen Analyse einer Tokengruppe.
 * Hierbei handelt sich es um eine durch Leerzeichen ({@linkplain SVMLexer.TokenType#SPACE})
 * getrennte Serie von Zeichen, die jeweils einen oder mehrere {@linkplain SVMLexer.TokenType}s
 * bilden. Beispiel: "{@code &data}" ist eine Tokengruppe, die aus {@linkplain SVMLexer.TokenType#AMPERSAND}
 * gefolgt von einem {@linkplain SVMLexer.TokenType#STRING} besteht. Das sich haus dieser Tokengruppe
 * ergebende Symbol ist dann {@linkplain SVMLexer#SYM_TOKEN_DATA}.<p/>
 * Die Tokengruppe ist Bestandteil der gerade zu scannenden Zeile.
 */
public class TokenGroupLexer {

	public static final Logger LOGGER = LogManager.getLogger(TokenGroupLexer.class);

	/** Die regex-Pattern zur Erkennung aller {@linkplain SVMLexer.TokenType}s */
	public static final Pattern tokenTypePattern = Pattern.compile(SVMLexer.getTokenTypePattern());

	/**
	 * Liefert zu einer als String repräsentierten Tokengruppe das/die zugehörige/n Symbol/e in
	 * der übergebene Liste. Liefert <b>true</b>, wenn das weitere Scannen der aktuellen Zeile
	 * abgebrochen werden muss (wenn ein Kommentar-Zeichen gelesen wurde), sonst <b>false</b>.
	 */
	public static final SVMLexer.TokenGroupLexer TOKEN_GROUP_LEXER = (symbols, tokenGroup) -> {
		LOGGER.trace("  tokenGroup='{}'", tokenGroup);
		TokenType lastTokenType = null; // hier merken, ob wie gerade in Token lesen, dass aus mehren TokenTypen besteht
		while ( tokenGroup.length() > 0 ) {
			try ( var tokenScanner = new Scanner(tokenGroup) ) {
				var nextToken = tokenScanner.findInLine(tokenTypePattern);
				LOGGER.trace("    nextToken='{}'", nextToken);
				if ( nextToken != null ) {
					Matcher matcher = tokenTypePattern.matcher(nextToken);
					if ( matcher.matches() ) {
						if ( nextToken.startsWith(TokenType.HASH.getText()) )
							return true; // Kommentar: Lesen der Zeile abbrechen
						for ( var type : SVMLexer.TokenType.values() ) {
							LOGGER.trace("      Test type='{}' ('{}'); lastTokenType={}", type, nextToken, lastTokenType);
							if ( matcher.group(type.toString()) != null ) {
								LOGGER.trace("        Gefunden: '{}'; parse...", type);
								lastTokenType = type.getTokenTypeParser().scanTokenType(symbols, nextToken, lastTokenType);
								LOGGER.trace("        lastTokenType'{}'; symbols={}", lastTokenType, symbols);
								break; // nächstes Token lesen
							}
						}
					}
					tokenGroup = tokenGroup.substring(nextToken.length());
				}
				else
					throw new SVMException("Ungültige(s) Token: '"+tokenGroup+"'!");
			}
		}
		return false; // Kein Kommentar erkannt: nicht abbrechen, mit der nächsten TokenGroup der Zeile weitermachen
	};

}
