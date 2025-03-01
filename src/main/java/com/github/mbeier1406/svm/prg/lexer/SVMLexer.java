package com.github.mbeier1406.svm.prg.lexer;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.instructions.Int;
import com.github.mbeier1406.svm.instructions.Mov;
import com.github.mbeier1406.svm.instructions.Nop;

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
		HASH("#", null),								// Definiert den Beginn eines Programmkommentars
		SPACE(" ", SpaceLexer.TOKEN_SCANNER),			// Leerzeichen zur Trennung von Token
		COMMA(",", CommaLexer.TOKEN_SCANNER),			// Trennt Parameter von Instruktionen
		LEFTPAR("\\(", LeftparLexer.TOKEN_SCANNER),		// Öffnende Klammer nach einem Funktionsaufruf
		RIGHTPAR("\\)", RightparLexer.TOKEN_SCANNER),		// Schließende Klammer nach einem Funktionsaufruf
		DOLLAR("\\$", DollarLexer.TOKEN_SCANNER),		// Markiert eine Zahl
		PERCENT("%", PercentLexer.TOKEN_SCANNER),		// Definiert ein Register
		AMPERSAND("&", AmpersandLexer.TOKEN_SCANNER),	// Leerzeichen zur Trennung von Token
		NUMBER("\\d+", NumberLexer.TOKEN_SCANNER),		// Definiert eine Zahl
		STRING("[A-Za-z][A-Za-z0-9\\\\]*", StringLexer.TOKEN_SCANNER);	// Definiert eine Bezeichner (zum Beispiel einen Label)
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
		public TokenPart scanTokenPart(final List<Symbol> symbols, String currentTokenValue, TokenPart lastTokenType) throws SVMException;
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
		public boolean scanTokenGroup(final List<Symbol> symbols, String tokenGroup) throws SVMException;
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
	 * Der Parameter gibt an, ob, wenn das Token in einem {@linkplain Symbol} verwendet wird, es einen
	 * Parameter benötigt.
	 * <ul>
	 * <li>{@linkplain Token#SPACE}: Das Leerzeichen als Token-Trenner ({@linkplain TokenPart#SPACE})</li>
	 * <li>{@linkplain Token#LEFTPAR}: Die öffnende Klammer ({@linkplain TokenPart#LEFTPAR})</li>
	 * <li>{@linkplain Token#RIGHTPAR}: Die schließende Klammer ({@linkplain TokenPart#RIGHTPAR})</li>
	 * <li>{@linkplain Token#TAB}: Leitet eine Sektion oder {@linkplain InstructionDefinition Instruktion ein} ({@linkplain TokenPart#TAB})</li>
	 * <li>{@linkplain Token#TOKEN_DATA}: Start der Datensektion ({@linkplain TokenPart#AMPERSAND} und {@linkplain TokenPart#STRING} {@code data})</li>
	 * <li>{@linkplain Token#TOKEN_CODE}: Start der Codesektion ({@linkplain TokenPart#AMPERSAND} und {@linkplain TokenPart#STRING} {@code code})</li>
	 * <li>{@linkplain Token#LABEL}: Definiert einen Bezeichner ({@linkplain TokenPart#DOT} und {@linkplain TokenPart#STRING} Name des Labels)</li>
	 * <li>{@linkplain Token#LABEL_REF}:Verwendung/Referenz eines Bezeichner ({@linkplain Token#LABEL})</li>
	 * <li>{@linkplain Token#DATA}: Definiert Zahlen oder Strings in der Datensektion</li>
	 * <li>{@linkplain Token#CODE}: Gibt eine {@linkplain InstructionDefinition Instruktion} an</li>
	 * <li>{@linkplain Token#CONSTANT}: Gibt eine {@linkplain TokenPart#NUMBER} Zahl als Parameter einer Instruktion an</li>
	 * <li>{@linkplain Token#REGISTER}: Gibt ein Register der {@linkplain ALU.Instruction} an</li>
	 * <li>{@linkplain Token#COMMA}: Trennt zwei Parameter einer {@linkplain InstructionDefinition Instruktion}</li>
	 * <li>{@linkplain Token#FUNCTION}: Parserfunktion wie {@code len}, die die Länge eines Strings ermittelt etc.</li>
	 * </ul>
	 */
	public static enum Token {
		SPACE(false), TAB(false), COMMA(false), LEFTPAR(false), RIGHTPAR(false),
		TOKEN_DATA(false), DATA(true),
		TOKEN_CODE(false), CODE(true),
		LABEL(true), LABEL_REF(true),
		CONSTANT(true), REGISTER(true),
		FUNCTION(true);
		private boolean needsParameter;
		private Token(boolean needsParameter) {
			this.needsParameter = needsParameter;
		}
		public boolean needsParameter() {
			return this.needsParameter;
		}
	}

	/** Definiert alle bekannten lexikalischen Einheiten ({@linkplain Token} ggf. mit Wert) aus denen {@linkplain SVM}-Programm besteht */
	public static record Symbol(Token token, String value) {
		public Symbol {
			requireNonNull(token, "token");
			if ( token.needsParameter )
				requireNonNull(value, "value");
			else if ( value != null )
				throw new IllegalArgumentException("Token '"+token+"' erwartet keinen Parameter!");
		}
		/** Liefert den Wert für String-Token wie {@linkplain Token#LABEL} */
		public Optional<String> getStringValue() {
			return Optional.ofNullable(value);
		}
		/** Liefert den Wert für Zahl-Token wie {@linkplain Token#REGISTER} (hier: die Nummer des Registers) */
		public Optional<Integer> getIntValue() {
			return Optional.ofNullable(value == null ? null : Integer.parseInt(value));
		}
	}

	/** Definiert das statische Symbol für ein Leerzeichen */
	public static final Symbol SYM_SPACE = new Symbol(Token.SPACE, null);

	/** Definiert das statische Symbol für eine öffnende Klammer */
	public static final Symbol SYM_LEFTPAR = new Symbol(Token.LEFTPAR, null);

	/** Definiert das statische Symbol für eine schließende Klammer */
	public static final Symbol SYM_RIGHTPAR = new Symbol(Token.RIGHTPAR, null);

	/** Definiert das statische Symbol für ein Tabulator */
	public static final Symbol SYM_TAB = new Symbol(Token.TAB, null);

	/** Definiert das statische Symbol für die Datensektion */
	public static final Symbol SYM_TOKEN_DATA = new Symbol(Token.TOKEN_DATA, null);

	/** Definiert das statische Symbol für die Codesektion */
	public static final Symbol SYM_TOKEN_CODE = new Symbol(Token.TOKEN_CODE, null);

	/** Definiert das statische Symbol für ein Komma */
	public static final Symbol SYM_COMMA = new Symbol(Token.COMMA, null);

	/** Definiert das statische Symbol für die Instruktion {@linkplain Nop} */
	public static final Symbol SYM_NOP = new Symbol(Token.CODE, "nop");

	/** Definiert das statische Symbol für die Instruktion {@linkplain Mov} */
	public static final Symbol SYM_MOV = new Symbol(Token.CODE, "mov");

	/** Definiert das statische Symbol für die Instruktion {@linkplain Int} */
	public static final Symbol SYM_INT = new Symbol(Token.CODE, "int");

	/** Definiert das Symbol für die Funktion {@code len()}, zB für die Instruktion {@linkplain Mov} */
	public static final Symbol SYM_FUNCTION_LEN = new Symbol(Token.FUNCTION, "len");

	/**
	 * Speichert die Ergebnisse der lexikalischen Analyse einer Programmzeile:
	 * <ul>
	 * <li>Die Nummer der Zeile (als Debugging Information)</li>
	 * <li>Die Zeile selbst (als Debugging Information)</li>
	 * <li>Die Liste der {@linkplain Symbol Symbole} als Ergebnis von {@linkplain SVMLexer#scan(File)}</li>
	 * </ul>
	 */
	public static record LineInfo(int lineNumber, String line, List<Symbol> symbols) {
		public LineInfo {
			requireNonNull(symbols, "symbols");
			if ( symbols.size() == 0 ) throw new IllegalArgumentException("Leere Symbolliste!");
			requireNonNull(line, "line");
			if ( lineNumber <= 0 ) throw new IllegalArgumentException("Ungültige Zeilennummer: "+lineNumber);
		}
	}


	/**
	 * Methode zur lexikalischen Analyse eines {@linkplain SVM}-Programms.
	 * @param file das zu scannende Programm (Pfad zur Datei)
	 * @param encoding Kodierung der Datei
	 * @return Liste von Listen (jeweils eine Programmzeile) von Symbolen
	 * @throws SVMException bei lexikalischen Fehlern
	 */
	public List<LineInfo> scan(File file, Charset encoding) throws SVMException;

	/**
	 * Methode zur lexikalischen Analyse eines {@linkplain SVM}-Programms mit Standardkodierung.
	 * @see #scan(File, Charset)
	 */
	public default List<LineInfo> scan(File file) throws SVMException {
		return scan(file, Charset.defaultCharset());
	}

	/**
	 * Methode zur lexikalischen Analyse eines {@linkplain SVM}-Programms.
	 * @see #scan(File, Charset)
	 */
	public List<LineInfo> scan(String text) throws SVMException;

}
