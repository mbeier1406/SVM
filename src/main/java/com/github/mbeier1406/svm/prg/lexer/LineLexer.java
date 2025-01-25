package com.github.mbeier1406.svm.prg.lexer;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Scanner;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Definiert die Funktion zur lexikalischen Analyse einer Zeile {@linkplain SVM}-Code.
 * Hierbei handelt sich es um eine durch Leerzeichen ({@linkplain SVMLexer.TokenPart#SPACE})
 * getrennte Serie von {@linkplain TokenGroupLexer Tokengruppe}.
 */
public class LineLexer {

	public static final Logger LOGGER = LogManager.getLogger(LineLexer.class);

	/**
	 * Liefert zu einer Programmzeile {@linkplain SVM}-Code, die aus einer oder mehreren
	 * {@linkplain TokenGroupLexer TokenType-Gruppen} besteht, die Liste der zugehörigen
	 * {@linkplain SVMLexer.Symbol Symbole}.
	 */
	public static final SVMLexer.LineLexer LINE_SCANNER = ( symbols, line ) -> {
		try ( @SuppressWarnings("unused") var ctx = CloseableThreadContext.put("line", Objects.requireNonNull(line, "line"));
			  var lineScanner = new Scanner(requireNonNull(line, "line")) ) {
			lineScanner.useDelimiter(SVMLexer.TokenPart.SPACE.getRegEx());
			while ( lineScanner.hasNext() ) {
				boolean abbruch = com.github.mbeier1406.svm.prg.lexer.TokenGroupLexer.TOKEN_GROUP_LEXER.scanTokenType(symbols, lineScanner.next());
				LOGGER.debug("abbruch={}; symbols={}", abbruch, symbols);
				if ( abbruch ) break; // Ein Kommentar wurde gelesen, alles danch in der Zeile ignorieren
			}
		}
		catch ( Exception e ) {
			throw new SVMException("Zeile '"+line+"': "+e.getLocalizedMessage(), e);
		}
	};

}
