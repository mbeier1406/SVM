package com.github.mbeier1406.svm.prg.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.cmd.ProgrammTest;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;

/**
 * Tests für die Klasse {@linkplain SVMParserShort};
 */
public class SVMParserShortTest {

	/** Das zu testende Objekt */
	public SVMParser<Short>  svmParser = new SVMParserShort();

	/** Liest ein SVM Beispiel-Programm ein (Lexer/Parser) und validiert es */
	@Test
	public void testeProgrammEinlesen() throws SVMException {
		SVMProgram<Short> svmProgram = svmParser.parse(ProgrammTest.SVM);
		svmProgram.validate();
	}
	
	/** Liest ein SVM Beispiel-Programm im Debug-Modus ein und prüft die Infos */
	@Test
	public void testeProgrammMitDebuggingEinlesen() throws SVMException {
		SVMProgram<Short> svmProgram = svmParser.setDebugging(true).parse(ProgrammTest.SVM);
		assertTrue(svmProgram.getDataList().stream().map(Data::lineInfo).filter(Objects::nonNull).count() == 2L);
		assertTrue(svmProgram.getInstructionList().stream().map(VirtualInstruction::lineInfo).filter(Objects::nonNull).count() == 10L);
	}
	
}
