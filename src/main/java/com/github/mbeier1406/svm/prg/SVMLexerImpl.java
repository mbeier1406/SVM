package com.github.mbeier1406.svm.prg;

import static org.apache.logging.log4j.CloseableThreadContext.put;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;

public class SVMLexerImpl implements SVMLexer {

	public static final Logger LOGGER = LogManager.getLogger(SVMLexerImpl.class);

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
				boolean endeDatei = false;
				while ( !endeDatei ) {
					try {
						String nextLine = lineScanner.next();
						LOGGER.trace("nextLine={}", nextLine);
						if ( nextLine == null ) endeDatei = true;
						if ( nextLine.length() == 0 ) continue;
						try ( var tokenScanner = new Scanner(new String(nextLine)) ) {
							boolean endeZeile = false;
							while ( !endeZeile ) {
								try {
									String nextToken = tokenScanner.next();
									LOGGER.trace("  nextToken={}", nextToken);
									if ( TokenType.getToken(nextToken.charAt(0)).equals(TokenType.HASH) )
										continue; // Kommentar
								}
								catch ( NoSuchElementException e ) {
									endeZeile = true;
								}
							}
						}
					}
					catch ( NoSuchElementException e ) {
						endeDatei = true;
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
