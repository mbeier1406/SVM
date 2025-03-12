package com.github.mbeier1406.svm.prg.lexer;

import static java.util.Objects.requireNonNull;
import static org.apache.logging.log4j.CloseableThreadContext.put;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;

public class SVMLexerImpl implements SVMLexer {

	public static final Logger LOGGER = LogManager.getLogger(SVMLexerImpl.class);

	public SVMLexerImpl() {
	}

	/** {@inheritDoc} */
	@Override
	public List<LineInfo> scan(final File file, Charset encoding) throws SVMException {
		try ( @SuppressWarnings("unused") CloseableThreadContext.Instance ctx = put("file", requireNonNull(file.getAbsolutePath(), "file")).put("encoding", encoding.toString()) ) {
			return scan(new String (Files.readAllBytes(Paths.get(file.getAbsolutePath())), requireNonNull(encoding,"encoding")));
		} catch (IOException e) {
			LOGGER.warn("file={}", file, e);
			throw new SVMException("File "+file+"; Encoding "+encoding, e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<LineInfo> scan(String text) throws SVMException {
		int zeile = 1;
		try {
			LOGGER.info("Start Scan...");
			var lineInfo = new ArrayList<LineInfo>();
			var lines = text.split("\n", -1);
			for ( String nextLine : lines ) {
				LOGGER.trace("nextLine={}", nextLine);
				if ( nextLine.length() > 0 ) {
					var symbolsInLine = com.github.mbeier1406.svm.prg.lexer.LineLexer.LINE_SCANNER.scanLine(nextLine);
					if ( symbolsInLine.size() > 0 )
						lineInfo.add(new LineInfo(zeile, nextLine, symbolsInLine));
				}
				zeile++;
			}
			LOGGER.info("Ende Scan.");
			return lineInfo;
		}
		catch ( Exception e ) {
			throw new SVMException("zeile="+zeile, e);
		}
	}

}
