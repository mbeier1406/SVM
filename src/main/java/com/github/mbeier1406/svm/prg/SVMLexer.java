package com.github.mbeier1406.svm.prg;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.lexer.SpaceLexer;

public interface SVMLexer {

	public static enum TokenType {
		DOT("\\.", null),				// Definiert einen Label
		TAB("	", null),				// Zu Beginn der Zeile leitet es eine Instruktion oder eine Programmkonfiguration ein
		HASH("#", null),				// Definiert eine Programmkonfiguration
		SPACE(" ", SpaceLexer.TOKEN_PORCESSOR),	// Leerzeichen zur Trennung von Token
		COMMA(",", null),				// Trennt Parameter von Instruktionen
		DOLLAR("\\$", null),			// Markiert eine Zahl
		PERCENT("%", null),				// Definiert ein Register
		AMPERSAND("&", null),			// Leerzeichen zur Trennung von Token
		NUMBER("\\d+", null),			// Definiert eine Zahl
		STRING("[A-Za-z][A-Za-z0-9]*", null);	// Definiert eine Bezeichner (zum Beispiel einen Label)
		private String text;
		private TokenTypeParser tokenTypeParser;
		private TokenType(String text, TokenTypeParser tokenTypeParser) {
			this.text = text;
			this.tokenTypeParser = tokenTypeParser;
		}
		public String getText() {
			return text;
		}
		public TokenTypeParser getTokenTypeParser() {
			return tokenTypeParser;
		}
	};

	@FunctionalInterface
	public static interface TokenTypeParser {
		public TokenType parseTokenType(final List<Symbol> symbols, String currentTokenValue, TokenType lastTokenType) throws SVMException;
	}


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
