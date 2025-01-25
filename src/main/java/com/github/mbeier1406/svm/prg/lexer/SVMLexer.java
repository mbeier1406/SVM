package com.github.mbeier1406.svm.prg.lexer;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Dieses Interface definiert alle Methoden, Datenstrukturen und Funktionsdefinitionen
 * für die lexikalische Analyse eine {@linkplain SVM}-Programms.
 * Das Ergebnis der lexiaklischen Analyse ist eine Liste von {@linkplain SVMLexer.Symbol Symbolen}.
 */
public interface SVMLexer {

	/**
	 * Alle bekannten lexikalische Einheiten (Token-Teile) eines {@linkplain SVM}-Programms.
	 * Ein {@linkplain Token} kann aus mehreren Teilen bestehen, wie zum Beispiel das
	 * Token {@linkplain SVMLexer#SYM_TOKEN_CODE}, das aus den Teilen {@linkplain TokenPart#AMPERSAND}
	 * und {@linkplain TokenPart#STRING} besteht. Es wird jeweils der reguläre Ausdruck zur Erkennung
	 * des Tokenteils sowie die Methode, wie er zu bearbeiten ist, definiert.
	 */
	public static enum TokenPart {
		DOT("\\.", DotLexer.TOKEN_SCANNER),				// Definiert einen Label
		TAB("	", TabLexer.TOKEN_SCANNER),				// Zu Beginn der Zeile leitet es eine Instruktion oder eine Programmkonfiguration ein
		HASH("#", null),								// Definiert eine Programmkonfiguration
		SPACE(" ", SpaceLexer.TOKEN_SCANNER),			// Leerzeichen zur Trennung von Token
		COMMA(",", CommaLexer.TOKEN_SCANNER),			// Trennt Parameter von Instruktionen
		DOLLAR("\\$", DollarLexer.TOKEN_SCANNER),		// Markiert eine Zahl
		PERCENT("%", PercentLexer.TOKEN_SCANNER),		// Definiert ein Register
		AMPERSAND("&", AmpersandLexer.TOKEN_SCANNER),	// Leerzeichen zur Trennung von Token
		NUMBER("\\d+", NumberLexer.TOKEN_SCANNER),		// Definiert eine Zahl
		STRING("[A-Za-z][A-Za-z0-9]*", StringLexer.TOKEN_SCANNER);	// Definiert eine Bezeichner (zum Beispiel einen Label)
		private String regEx;
		private TokenPartLexer tokenPartLexer;
		private TokenPart(String text, TokenPartLexer tokenTypeParser) {
			this.regEx = text;
			this.tokenPartLexer = tokenTypeParser;
		}
		/** Der reuläre Ausdruck zur Erkennung dieses {@linkplain TokenPart} */
		public String getRegEx() {
			return regEx;
		}
		/** Die Methode, die zur Verarbeitung des {@linkplain TokenPart} aufgerufen wird */
		public TokenPartLexer getTokenPartLexer() {
			return tokenPartLexer;
		}
	};

	/**
	 * Definiert die Signatur der Methode zur Bearbeitung/zum Scannen eines {@linkplain SVMLexer.TokenPart}.
	 * @see {@linkplain SVMLexer.TokenPart#getTokenPartLexer()}.
	 */
	@FunctionalInterface
	public static interface TokenPartLexer {
		/**
		 * Verarbeitet einen Teil eines {@linkplain SVMLexer.Token}.
		 * @param symbols Die Liste der bisher gelesenen Symbole wird ggf. (bei einem finalen {@linkplain SVMLexer.TokenPart}) erweitert
		 * @param currentTokenValue Der gerade gelesene {@linkplain SVMLexer.TokenPart} als Text
		 * @param lastTokenType Das zuletzt gelesene {@linkplain SVMLexer.TokenPart}, kann <b>null</b> sein, bei zusammengesetzten {@linkplain SVMLexer.Token}
		 * @return den gerade lesenen {@linkplain SVMLexer.TokenPart} für zusammengesetzte Token bzw. <b>null</b> bei finalen Tokenteilen
		 * @throws SVMException bei ungültiger lexikalischer Struktur (z. B. ungültiger vorangegangener Tokenteil)
		 */
		public TokenPart scanTokenType(final List<Symbol> symbols, String currentTokenValue, TokenPart lastTokenType) throws SVMException;
	}

	/**
	 * Definiert die Signatur zum Scannen einer Gruppe von {@linkplain SVMLexer.TokenPart}, die durch den Trenner {@linkplain Token#SPACE}
	 * getrennt sind (z. B. {@code &code}.
	 */
	@FunctionalInterface
	public static interface TokenGroupLexer {
		/**
		 * Verarbeitet eine Gruppe von Tokenteilen.
		 * @param symbols Die Liste der bisher gelesenen Symbole wird ggf. (bei einem finalen {@linkplain SVMLexer.TokenPart}) erweitert
		 * @param tokenGroup Die Gruppe der Tokenteil getrennt durch {@linkplain SVMLexer.TokenPart}
		 * @return <b>true</b>, wenn das Lesen der Zeile abgebrochen werden soll (Kommentar), sonst <b>false</b>
		 * @throws SVMException bei ungültiger lexikalischer Struktur (z. B. ungültiger vorangegangener Tokenteil)
		 * @see {@link TokenPartLexer}
		 */
		public boolean scanTokenType(final List<Symbol> symbols, String tokenGroup) throws SVMException;
	}

	@FunctionalInterface
	public static interface LineLexer {
		public void scanLine(final List<Symbol> symbols, String line) throws SVMException;
	}

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

	public static enum Token { SPACE, TAB, TOKEN_DATA, DATA, TOKEN_CODE, LABEL, CODE, CONSTANT, REGISTER, COMMA }

	public static record Symbol(Token token, String value) {
		public Symbol {
			Objects.requireNonNull(token, "token");
		}
		public Optional<String> getStringValue() {
			return Optional.ofNullable(value);
		}
		public Optional<Integer> getIntValue() {
			return Optional.ofNullable(Integer.parseInt(value));
		}
	}

	public static final Symbol SYM_SPACE = new Symbol(Token.SPACE, null);
	public static final Symbol SYM_TAB = new Symbol(Token.TAB, null);
	public static final Symbol SYM_TOKEN_DATA = new Symbol(Token.TOKEN_DATA, null);
	public static final Symbol SYM_TOKEN_CODE = new Symbol(Token.TOKEN_CODE, null);
	public static final Symbol SYM_COMMA = new Symbol(Token.COMMA, null);

	public List<List<Symbol>> scan(String file, Charset encoding) throws SVMException;

	public default List<List<Symbol>> scan(String file) throws SVMException {
		return scan(file, Charset.defaultCharset());
	}

	public List<List<Symbol>> scan(char[] text) throws SVMException;

}
