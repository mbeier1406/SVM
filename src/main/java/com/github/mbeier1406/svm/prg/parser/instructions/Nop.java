package com.github.mbeier1406.svm.prg.parser.instructions;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.instructions.InstructionFactory;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

/**
 * Erzeugt aus einer vom {@linkplain SVMLexer} eingelesenen Codezeile
 * mit einer Instruktion {@linkplain com.github.mbeier1406.svm.instructions.Nop}.
 * die zugehörige virtuelle Instruktion. Beispiel:
 * <pre><code>
 * .label1	# Optional
 * 	nop
 * </code></pre>
 * @see {@linkplain InstructionFactory#INSTRUCTIONS}
 */
public class Nop implements InstructionParser<Short> {

	/** {@inheritDoc} */
	@Override
	public VirtualInstruction<Short> getVirtualInstruction(final Symbol label, final LineInfo lineInfo) throws SVMException {
		if ( lineInfo.symbols().size() != 1 )
			throw new SVMException("NOP erwartet keine Parameter: "+lineInfo.symbols());
		return new VirtualInstruction<>(
				Helper.getLabel(label),
				new InstructionDefinition<>(new com.github.mbeier1406.svm.instructions.Nop(), new byte[0], null),
				new Label[]{} /* Keine Parameter */);
	}

}
