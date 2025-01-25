package com.github.mbeier1406.svm.prg.lexer;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;

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

	/**
	 * Definiert die Signatur zum Scannen einer einer Zeile eines {@linkplain SVM}-Programms, die aus durch den Trenner {@linkplain Token#SPACE}
	 * getrennte {@linkplain SVMLexer.TokenGroupLexer Tokengruppen} besteht.
	 */
	@FunctionalInterface
	public static interface LineLexer {
		/**
		 * Verarbeitet eine Zeile eines {@linkplain SVM}-Programms.
		 * @param line die zu scannende Zeile
		 * @return symbols Die Liste der bisher gelesenen Symbole wird ggf. (bei einem finalen {@linkplain SVMLexer.TokenPart}) erweitert
		 * @throws SVMException bei ungültiger lexikalischer Struktur (z. B. ungültiger vorangegangener Tokenteil)
		 */
		public List<Symbol> scanLine(String line) throws SVMException;
	}

	/**
	 * Definiert alle bekannten lexikalischen Typen, aus denen {@linkplain SVM}-programm besteht.
	 * Jedes dieser Token besteht aus einem oder mehreren {@linkplain SVMLexer.TokenPart Tokenteilen}.
	 * <ul>
	 * <li>{@linkplain Token#SPACE}: Das Leerzeichen als Token-Trenner ({@linkplain TokenPart#SPACE})</li>
	 * <li>{@linkplain Token#TAB}: Leitet eine Sektion oder {@linkplain InstructionDefinition Instruktion ein} ({@linkplain TokenPart#TAB})</li>
	 * <li>{@linkplain Token#TOKEN_DATA}: Start der Datensektion ({@linkplain TokenPart#AMPERSAND} und {@linkplain TokenPart#STRING} {@code data})</li>
	 * <li>{@linkplain Token#TOKEN_CODE}: Start der Codesektion ({@linkplain TokenPart#AMPERSAND} und {@linkplain TokenPart#STRING} {@code code})</li>
	 * <li>{@linkplain Token#LABEL}: Definiert einen Bezeichner ({@linkplain TokenPart#DOT} und {@linkplain TokenPart#STRING} Name des Labels)</li>
	 * <li>{@linkplain Token#DATA}: Definiert Zahlen oder Strings in der Datensektion</li>
	 * <li>{@linkplain Token#CODE}: Gibt eine {@linkplain InstructionDefinition Instruktion} an</li>
	 * <li>{@linkplain Token#CONSTANT}: Gibt eine {@linkplain TokenPart#NUMBER} Zahl als Parameter einer Instruktion an</li>
	 * <li>{@linkplain Token#REGISTER}: Gibt ein Register der {@linkplain ALU.Instruction} an</li>
	 * <li>{@linkplain Token#COMMA}: Trennt zwei Parameter einer {@linkplain InstructionDefinition Instruktion}</li>
	 * </ul>
	 */
	public static enum Token { SPACE, TAB, TOKEN_DATA, DATA, TOKEN_CODE, LABEL, CODE, CONSTANT, REGISTER, COMMA }

	/** Definiert alle bekannten lexikalischen Einheiten ({@linkplain Token} ggf. mit Wert) aus denen {@linkplain SVM}-Programm besteht */
	public static record Symbol(Token token, String value) {
		public Symbol {
			Objects.requireNonNull(token, "token");
		}
		/** Liefert den Wert für String-Token wie {@linkplain Token#LABEL} */
		public Optional<String> getStringValue() {
			return Optional.ofNullable(value);
		}
		/** Liefert den Wert für Zahl-Token wie {@linkplain Token#REGISTER} (hier: die Nummer des Registers) */
		public Optional<Integer> getIntValue() {
			return Optional.ofNullable(Integer.parseInt(value));
		}
	}

	/** Definiert das statische Symbol für ein Leerzeichen */
	public static final Symbol SYM_SPACE = new Symbol(Token.SPACE, null);

	/** Definiert das statische Symbol für ein Tabulator */
	public static final Symbol SYM_TAB = new Symbol(Token.TAB, null);

	/** Definiert das statische Symbol für die Datensektion */
	public static final Symbol SYM_TOKEN_DATA = new Symbol(Token.TOKEN_DATA, null);

	/** Definiert das statische Symbol für die Codesektion */
	public static final Symbol SYM_TOKEN_CODE = new Symbol(Token.TOKEN_CODE, null);

	/** Definiert das statische Symbol für ein Komma */
	public static final Symbol SYM_COMMA = new Symbol(Token.COMMA, null);

	/**
	 * Methode zur lexikalischen Analyse eines {@linkplain SVM}-Programms.
	 * @param file das zu scannende Programm (Pfad zur Datei)
	 * @param encoding Kodierung der Datei
	 * @return Liste von Listen (jeweils eine Programmzeile) von Symbolen
	 * @throws SVMException bei lexikalischen Fehlern
	 */
	public List<List<Symbol>> scan(File file, Charset encoding) throws SVMException;

	/**
	 * Methode zur lexikalischen Analyse eines {@linkplain SVM}-Programms mit Standardkodierung.
	 * @see #scan(String, Charset)
	 */
	public default List<List<Symbol>> scan(File file) throws SVMException {
		return scan(file, Charset.defaultCharset());
	}

	/**
	 * Methode zur lexikalischen Analyse eines {@linkplain SVM}-Programms.
	 * @see #scan(String, Charset)
	 */
	public List<List<Symbol>> scan(String text) throws SVMException;

}
