package com.github.mbeier1406.svm.prg.lexer;

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
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

public class SVMLexerImpl implements SVMLexer {

	public static final Logger LOGGER = LogManager.getLogger(SVMLexerImpl.class);

	public SVMLexerImpl() {
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
			var symbols = new ArrayList<List<Symbol>>();
			try ( var lineScanner = new Scanner(new String(text)) ) {
				lineScanner.useDelimiter("\n");
				List<Symbol> symbolsInLine = new ArrayList<>();
				while ( lineScanner.hasNext() ) {
					String nextLine = lineScanner.next();
					LOGGER.trace("nextLine={}", nextLine);
					if ( nextLine.length() == 0 ) continue;
					com.github.mbeier1406.svm.prg.lexer.LineLexer.LINE_SCANNER.scanLine(symbolsInLine, nextLine);
				}
				symbols.add(symbolsInLine);
			}
			LOGGER.info("Ende Scan.");
			return symbols;
		}
		catch ( Exception e ) {
			throw new SVMException(e);
		}
	}

}
