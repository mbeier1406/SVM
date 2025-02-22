package com.github.mbeier1406.svm.prg.parser.instructions;

import java.util.Objects;

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
 * </code></pre><p/>
 * Syntax: {@code nop}
 * @see {@linkplain InstructionFactory#INSTRUCTIONS}
 */
public class Nop implements InstructionParser<Short> {

	/** {@inheritDoc} */
	@Override
	public VirtualInstruction<Short> getVirtualInstruction(final Symbol label, final LineInfo lineInfo) throws SVMException {
		if ( Objects.requireNonNull(lineInfo, "lineInfo").symbols().size() != 1 ) // Symbol an Index  0 ist "nop"
			throw new SVMException("NOP erwartet keine Parameter: "+lineInfo.symbols());
		if ( !lineInfo.symbols().get(0).equals(SVMLexer.SYM_NOP) )
			throw new SVMException("NOP erwartet Symbol "+SVMLexer.SYM_NOP);
		return new VirtualInstruction<>(
				Helper.getLabel(label),
				new InstructionDefinition<>(InstructionFactory.NOP, new byte[0], null),
				new Label[]{} /* Keine Parameter */);
	}

}
