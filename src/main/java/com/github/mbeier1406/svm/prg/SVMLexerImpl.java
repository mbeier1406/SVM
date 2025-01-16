package com.github.mbeier1406.svm.prg;

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
	public List<List<Token>> scan(String file, Charset encoding) throws SVMException {
		try ( @SuppressWarnings("unused") CloseableThreadContext.Instance ctx = put("file", file).put("encoding", encoding.toString()) ) {
			return scan(new String (Files.readAllBytes(Paths.get(file)), encoding).toCharArray());
		} catch (IOException e) {
			throw new SVMException();
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<List<Token>> scan(char[] text) throws SVMException {
		try {
			LOGGER.info("Start Scan...");
			var tokens = new ArrayList<List<Token>>();
			try ( var lineScanner = new Scanner(new String(text)) ) {
				lineScanner.useDelimiter("\n");
				while ( lineScanner.hasNext() ) {
					String nextLine = lineScanner.next();
					LOGGER.trace("nextLine={}", nextLine);
					if ( nextLine.length() == 0 ) continue;
					try ( var tokenScanner = new Scanner(new String(nextLine)) ) {
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

}
