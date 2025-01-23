package com.github.mbeier1406.svm.prg.lexer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;

/**
 * Tests für die Klasse {@linkplain SVMLexerImpl}.
 */
public class FileLexerTest {

	public static final Logger LOGGER = LogManager.getLogger(FileLexerTest.class);

	/** Die zu scannende Testdatei ist {@value} */
	public static final String PRG = "src/test/resources/com/github/mbeier1406/svm/prg/example.prg";

	/** Das zu testende Objekt */
	public SVMLexer svmLexer = new SVMLexerImpl();

	@Test
	public void testeDateiScannen() throws SVMException {
//		svmLexer.scan(PRG);
	}
	
}
