package com.github.mbeier1406.svm.prg;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import com.github.mbeier1406.svm.SVMException;

public interface SVMLexer {

	public static enum TokenType {
		DOT("\\."),				// Definiert einen Label
		TAB("	"),				// Zu Beginn der Zeile leitet es eine Instruktion oder eine Programmkonfiguration ein
		HASH("#"),				// Definiert eine Programmkonfiguration
		SPACE(" "),				// Leerzeichen zur Trennung von Token
		COMMA(","),				// Trennt Parameter von Instruktionen
		DOLLAR("\\$"),			// Markiert eine Zahl
		PERCENT("%"),			// Definiert ein Register
		AMPERSAND("&"),			// Leerzeichen zur Trennung von Token
		NUMBER("\\d+"),			// Definiert eine Zahl
		STRING("[A-Za-z][A-Za-z0-9]*");	// Definiert eine Bezeichner (zum Beispiel einen Label)
		private String text;
		private static Map<TokenType, Pattern> regex = null;
		private TokenType(String text) {
			this.text = text;
		}
		public String getText() {
			return text;
		}
		public static TokenType getToken(char ch) {
			if ( regex == null ) {
				regex = new HashMap<>();
				for ( TokenType t : TokenType.values() )
					regex.put(t, Pattern.compile("["+t.getText().replace(".", "\\.")+"]"));
			}
			for ( TokenType t : TokenType.values() ) {
				if ( regex.get(t).matcher(String.valueOf(ch)).find() )
					return t;
			}
			throw new IllegalArgumentException("Kein Token: '"+ch+"'");
		}
	};

	public static String getTokenTypePattern() {
		final StringBuilder pattern = new StringBuilder("");
		Arrays.stream(TokenType.values()).forEach(t -> {
			if ( !pattern.isEmpty() ) pattern.append("|"); // EInzelne Token durch '|' trennen
			pattern.append("(?<"); // Neue Gruppe beginnen
			pattern.append(t.toString()); // Gruppennanme = TokenTyp
			pattern.append(">"); // Gruppenname schließen
			pattern.append(t.getText()); // Regulären Ausdruck zum Erkennen des Tokens
			pattern.append(")"); // Gruppefür den Tokentyp schließen
		});
		return pattern.toString();
	}

	public static enum Token { SPACE, TAB, TOKEN_DATA, DATA, TOKEN_CODE, LABEL, CODE, CONSTANT, REGISTER }

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

	public List<List<Symbol>> scan(String file, Charset encoding) throws SVMException;

	public default List<List<Symbol>> scan(String file) throws SVMException {
		return scan(file, Charset.defaultCharset());
	}

	public List<List<Symbol>> scan(char[] text) throws SVMException;

}
