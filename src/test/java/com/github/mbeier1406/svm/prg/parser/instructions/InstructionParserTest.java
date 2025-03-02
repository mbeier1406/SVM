package com.github.mbeier1406.svm.prg.parser.instructions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;

/**
 * Tests für das Interface {@linkplain InstructionParser}.
 */
public class InstructionParserTest {

	/** Die Klasse für die zu testende Methode {@linkplain InstructionParser#getInstructionParser(Symbol)} */
	public InstructionParser<Short> testee = new InstructionParser<>() {
		@Override
		public VirtualInstruction<Short> getVirtualInstruction(Symbol label, LineInfo lineInfo, SVMProgram<Short> svmProgram) throws SVMException {
			return null;
		}
	};

	/** Stellt sicher, dass zu einem {@linkplain Symbol} mit einer Code */
	@ParameterizedTest
	@MethodSource("getTestParameter")
	public void testeGetInstructionParser(Symbol symbol, final InstructionParser<Short> instructionParserExpected) throws SVMException {
		var instructionParser = testee.getInstructionParser(symbol);
		assertThat(instructionParserExpected.getClass(), equalTo(instructionParser.getClass()));
	}

	/** Liefert die Testfälle */
	public static Stream<Arguments> getTestParameter() {
		return Stream.of(
				Arguments.of(new Symbol(Token.CODE, "mov"), new com.github.mbeier1406.svm.prg.parser.instructions.Mov()),
				Arguments.of(new Symbol(Token.CODE, "int"), new com.github.mbeier1406.svm.prg.parser.instructions.Int()),
				Arguments.of(new Symbol(Token.CODE, "nop"), new com.github.mbeier1406.svm.prg.parser.instructions.Nop())
			);
	}

}
