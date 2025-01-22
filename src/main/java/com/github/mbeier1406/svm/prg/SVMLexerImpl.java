package com.github.mbeier1406.svm.prg;

import static java.util.Objects.requireNonNull;
import static org.apache.logging.log4j.CloseableThreadContext.put;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;

public class SVMLexerImpl implements SVMLexer {

	public static final Logger LOGGER = LogManager.getLogger(SVMLexerImpl.class);

	public Pattern tokenTypePattern;

	public SVMLexerImpl() {
		tokenTypePattern = Pattern.compile(SVMLexer.getTokenTypePattern());
	}

	/** {@inheritDoc} */
	@Override
	public List<List<Symbol>> scan(String file, Charset encoding) throws SVMException {
		try ( @SuppressWarnings("unused") CloseableThreadContext.Instance ctx = put("file", requireNonNull(file, "file")).put("encoding", encoding.toString()) ) {
			return scan(new String (Files.readAllBytes(Paths.get(file)), requireNonNull(encoding,"encoding")).toCharArray());
		} catch (IOException e) {
			throw new SVMException();
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<List<Symbol>> scan(char[] text) throws SVMException {
		try {
			LOGGER.info("Start Scan...");
			var tokens = new ArrayList<List<Symbol>>();
			try ( var lineScanner = new Scanner(new String(text)) ) {
				lineScanner.useDelimiter("\n");
				while ( lineScanner.hasNext() ) {
					String nextLine = lineScanner.next();
					LOGGER.trace("nextLine={}", nextLine);
					if ( nextLine.length() == 0 ) continue;
					try ( var tokenScanner = new Scanner(nextLine) ) {
						while ( tokenScanner.hasNext() ) {
							String nextToken = tokenScanner.findInLine(tokenTypePattern);
							LOGGER.trace("  nextToken={}", nextToken);
							if ( nextToken != null ) {
								Matcher matcher = tokenTypePattern.matcher(nextToken);
								if (matcher.matches()) {
									for ( var type : SVMLexer.TokenType.values() )
										if (matcher.group(type.toString()) != null) {
											LOGGER.trace("Gefunden: " + matcher.group(type.toString()) + " " + nextToken);
									}
										// else if (matcher.group("WORD") != null) {
//										LOGGER.trace("Gefundenes Wort: " + matcher.group("WORD"));
//									} else if (matcher.group("AMPERSAND") != null) {
//										LOGGER.trace("Gefunden &");
//									}
								}
//									String nextToken = tokenScanner.next();
//									if ( TokenType.getToken(nextToken.charAt(0)).equals(TokenType.HASH) )
//										continue; // Kommentar
							}
							else
								LOGGER.trace("NICHT {}", tokenScanner.next());
						}
					}
				}
			}
			LOGGER.info("Ende Scan.");
			return tokens;
		}
//		catch ( SVMException e ) {
//			throw e;
//		}
		catch ( Exception e ) {
			throw new SVMException(e);
		}
	}

	public List<Symbol> scanLine(String line) throws SVMException {
		try ( CloseableThreadContext.Instance ctx = CloseableThreadContext.put("line", line);
			  var lineScanner = new Scanner(requireNonNull(line, "line")) ) {
			lineScanner.useDelimiter(SVMLexer.TokenType.SPACE.getText());
			List<Symbol> symbols = new ArrayList<>();
			TokenType lastTokenType = null; // hier merken, ob wie gerade in Token lesen, dass aus mehren TokenTypen besteht
			while ( lineScanner.hasNext() ) {
				String nextTokenGroup = lineScanner.next();
				LOGGER.trace("  nextTokenGroup='{}'", nextTokenGroup);
				while ( nextTokenGroup.length() > 0 ) {
					try ( var tokenScanner = new Scanner(nextTokenGroup) ) {
						var nextToken = tokenScanner.findInLine(tokenTypePattern);
						LOGGER.trace("    nextToken='{}'", nextToken);
						if ( nextToken != null ) {
							Matcher matcher = tokenTypePattern.matcher(nextToken);
							if ( matcher.matches() ) {
								if ( nextToken.startsWith(TokenType.HASH.getText()) )
									return symbols; // Kommentare überlesen
								for ( var type : SVMLexer.TokenType.values() ) {
									LOGGER.trace("      Test type='{}' ('{}'); lastTokenType={}", type, nextToken, lastTokenType);
									if ( matcher.group(type.toString()) != null ) {
										LOGGER.trace("        Gefunden: '{}'; parse...", type);
										lastTokenType = type.getTokenTypeParser().parseTokenType(symbols, nextToken, lastTokenType);
										LOGGER.trace("        lastTokenType'{}'; symbols={}", lastTokenType, symbols);
										break; // nächstes Token lesen
									}
								}
							}
							nextTokenGroup = nextTokenGroup.substring(nextToken.length());
						}
						else
							throw new SVMException("Zeile '"+line+"': Ungültige(s) Token: '"+nextTokenGroup+"'!");
					}
				}
			}
			return symbols;
		}
	}

}
