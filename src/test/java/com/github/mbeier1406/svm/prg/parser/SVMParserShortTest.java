package com.github.mbeier1406.svm.prg.parser;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;

/**
 * Tests für die Klasse {@linkplain SVMParserShort};
 */
public class SVMParserShortTest {

	/** Das zu testende Objekt */
	public SVMParser<Short>  svmParser = new SVMParserShort();

	@Disabled
	@Test
	public void testeProgrammEinlesen() throws SVMException {
		SVMProgram<Short> svmProgram = svmParser.parse("src/test/resources/com/github/mbeier1406/svm/prg/example.svm");
		svmProgram.validate();
	}
	
}
