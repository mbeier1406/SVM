package com.github.mbeier1406.svm.prg;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import com.github.mbeier1406.svm.SVMException;

public interface SVMLexer {

	public static enum TokenType {
		DOT("."),			// Definiert einen Label
		TAB("\t"),			// Zu Beginn der Zeile leitet es eine Instruktion oder eine Programmkonfiguration ein
		HASH("#"),			// Definiert eine Programmkonfiguration
		SPACE(" "),			// Leerzeichen zur Trennung von Token
		COMMA(","),			// Trennt Parameter von Instruktionen
		PERCENT("%"),		// Definiert ein Register
		AMPERSAND("&"),		// Leerzeichen zur Trennung von Token
		NUMBER("0-9"),		// Definiert eine Zahl
		STRING("A-Za-z\n");	// Definiert eine Bezeichner (zum Beispiel einen Label)
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

	public static record Token(TokenType token, String value) {
		public Token {
			Objects.requireNonNull(token, "token");
		}
		public Optional<String> getValue() {
			return Optional.ofNullable(value);
		}
	}

	public List<List<Token>> scan(String file, Charset encoding) throws SVMException;

	public default List<List<Token>> scan(String file) throws SVMException {
		return scan(file, Charset.defaultCharset());
	}

	public List<List<Token>> scan(char[] text) throws SVMException;

}
