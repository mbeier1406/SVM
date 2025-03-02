package com.github.mbeier1406.svm.prg.parser.instructions;

import java.util.Objects;
import java.util.Optional;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.instructions.InstructionFactory;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;
import com.github.mbeier1406.svm.prg.parser.Helper;

/**
 * Erzeugt aus einer vom {@linkplain SVMLexer} eingelesenen Codezeile
 * mit einer Instruktion {@linkplain com.github.mbeier1406.svm.instructions.Int}.
 * die zugehörige virtuelle Instruktion. Beispiel:
 * <pre><code>
 * .label1	# Optional
 * 	int $1
 * </code></pre><p/>
 * Syntax: {@code int <Number>}
 * @see {@linkplain InstructionFactory#INSTRUCTIONS}
 * @see com.github.mbeier1406.svm.instructions.Int
 */
public class Int extends InstructionParserBase<Short> implements InstructionParser<Short> {

	/** {@inheritDoc} */
	@Override
	public VirtualInstruction<Short> getVirtualInstruction(final Symbol label, final LineInfo lineInfo, final SVMProgram<Short> svmProgram) throws SVMException {
		if ( Objects.requireNonNull(lineInfo, "lineInfo").symbols().size() != 2 ) // Syntax "int <Number>"
			throw new SVMException("INT erwartet einen Parameter: "+lineInfo.symbols());
		if ( !lineInfo.symbols().get(0).equals(SVMLexer.SYM_INT) )
			throw new SVMException("INT erwartet Symbol "+SVMLexer.SYM_INT);
		if ( !lineInfo.symbols().get(1).token().equals(Token.CONSTANT) )
			throw new SVMException("INT erwartet Number-Parameter "+lineInfo.symbols().get(1));
		if ( lineInfo.symbols().get(1).getIntValue().equals(Optional.empty()) )
			throw new SVMException("INT Number-Parameter fehlt: "+lineInfo.symbols().get(1));
		return new VirtualInstruction<>(
				Helper.getLabel(label),
				new InstructionDefinition<>(InstructionFactory.INT, new byte[] {lineInfo.symbols().get(1).getIntValue().get().byteValue()}, null),
				new Label[]{ null } /* Ein Parameter, keine Referenz, muss eine Konstante sein */);
	}

}
