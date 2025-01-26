package com.github.mbeier1406.svm.prg.lexer;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.TokenPart;

/**
 * Definiert die Funktion zur lexikalischen Analyse einer Tokengruppe.
 * Hierbei handelt sich es um eine durch Leerzeichen ({@linkplain SVMLexer.TokenPart#SPACE})
 * getrennte Serie von Zeichen, die jeweils einen oder mehrere {@linkplain SVMLexer.TokenPart}s
 * bilden. Beispiel: "{@code &data}" ist eine Tokengruppe, die aus {@linkplain SVMLexer.TokenPart#AMPERSAND}
 * gefolgt von einem {@linkplain SVMLexer.TokenPart#STRING} besteht. Das sich haus dieser Tokengruppe
 * ergebende Symbol ist dann {@linkplain SVMLexer#SYM_TOKEN_DATA}.<p/>
 * Die Tokengruppe ist Bestandteil der gerade zu scannenden Zeile.
 */
public class TokenGroupLexer {

	public static final Logger LOGGER = LogManager.getLogger(TokenGroupLexer.class);

	/** Die regex-Pattern zur Erkennung aller {@linkplain SVMLexer.TokenPart}s */
	public static final Pattern TOKEN_TYPE_PATTERN = Pattern.compile(getTokenTypePattern());

	/** Erstellt den Regex um {@linkplain SVMLexer.TokenPart} zu scannen */
	public static String getTokenTypePattern() {
		final StringBuilder pattern = new StringBuilder("");
		Arrays.stream(TokenPart.values()).forEach(t -> {
			if ( !pattern.isEmpty() ) pattern.append("|"); // EInzelne Token durch '|' trennen
			pattern.append("(?<"); // Neue Gruppe beginnen
			pattern.append(t.toString()); // Gruppennanme = TokenTyp
			pattern.append(">"); // Gruppenname schließen
			pattern.append(t.getRegEx()); // Regulären Ausdruck zum Erkennen des Tokens
			pattern.append(")"); // Gruppefür den Tokentyp schließen
		});
		return pattern.toString();
	}

	/**
	 * Liefert zu einer als String repräsentierten Tokengruppe das/die zugehörige/n Symbol/e in
	 * der übergebene Liste. Liefert <b>true</b>, wenn das weitere Scannen der aktuellen Zeile
	 * abgebrochen werden muss (wenn ein Kommentar-Zeichen gelesen wurde), sonst <b>false</b>.
	 */
	public static final SVMLexer.TokenGroupLexer TOKEN_GROUP_LEXER = (symbols, tokenGroup) -> {
		LOGGER.trace("  tokenGroup='{}'", tokenGroup);
		TokenPart lastTokenType = null; // hier merken, ob wie gerade in Token lesen, dass aus mehren TokenTypen besteht
		while ( tokenGroup.length() > 0 ) {
			try ( var tokenGroupScanner = new Scanner(tokenGroup) ) {
				var nextToken = tokenGroupScanner.findInLine(TOKEN_TYPE_PATTERN);
				LOGGER.trace("    nextToken='{}'", nextToken);
				if ( nextToken != null ) {
					Matcher matcher = TOKEN_TYPE_PATTERN.matcher(nextToken);
					if ( matcher.matches() ) {
						if ( nextToken.startsWith(TokenPart.HASH.getRegEx()) )
							return true; // Kommentar: Lesen der Zeile abbrechen
						for ( var type : SVMLexer.TokenPart.values() ) {
							LOGGER.trace("      Test type='{}' ('{}'); lastTokenType={}", type, nextToken, lastTokenType);
							if ( matcher.group(type.toString()) != null ) {
								LOGGER.trace("        Gefunden: '{}'; parse...", type);
								lastTokenType = type.getTokenPartLexer().scanTokenPart(symbols, nextToken, lastTokenType);
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
		if ( lastTokenType != null )
			throw new SVMException("Angefangenes Token nicht beendet: "+lastTokenType);
		return false; // Kein Kommentar erkannt: nicht abbrechen, mit der nächsten TokenGroup der Zeile weitermachen
	};

}
