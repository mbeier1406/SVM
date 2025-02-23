package com.github.mbeier1406.svm.prg.parser.instructions;

import static com.github.mbeier1406.svm.prg.parser.Helper.checkParameterToken;
import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;

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
 * mit einer Instruktion {@linkplain com.github.mbeier1406.svm.instructions.Mov}
 * die zugehörige virtuelle Instruktion. Beispiel:
 * <pre><code>
 * .label1	# Optional
 * 	mov $1, %1
 * </code></pre><p/>
 * Syntax: {@code mov (<Number>|<Register>|<Label-Reference>|<Function><Label-Reference>) , (<Register>|<Register-Reference>)}
 * @see {@linkplain InstructionFactory#INSTRUCTIONS}
 * @see com.github.mbeier1406.svm.instructions.Mov
 */
public class Mov implements InstructionParser<Short> {

	/** Liste der Token/Typen, die als jeweiliger Parameter zulässig sind */
	public static final Token[][] PARAMS = new Token[][] {
		new Token[] { Token.CONSTANT, Token.FUNCTION, Token.LABEL_REF, Token.REGISTER },
		new Token[] { Token.COMMA },
		new Token[] { Token.REGISTER }
	};

	/**
	 * Der <u>erste</u> Parameter zur MOV-Instruktion ist ein Byte, dass angibt, von wo
	 * nach wo verschoben werden soll. Beispiel <code>21</code> gibt an, dass eine
	 * Konstante in ein register verschoben werdne soll. Als SVM-Programm Beispiel:
	 * {@code mov $2 %1}
	 * @see com.github.mbeier1406.svm.instructions.Mov
	 */
	@SuppressWarnings({ "unused", "serial" })
	private static final  Map<Token, Byte> PARAM1_MAP = new HashMap<Token, Byte>() {{
		put(Token.REGISTER, (byte) 1);
		put(Token.FUNCTION, (byte) 2); // Funktionen müssen eine Konstante Zahl als Ergebnis liefern
		put(Token.CONSTANT, (byte) 2);
		put(Token.LABEL_REF, (byte) 3);
	}};

	/** {@inheritDoc} */
	@Override
	public VirtualInstruction<Short> getVirtualInstruction(final Symbol label, final LineInfo lineInfo, final SVMProgram<Short> svmProgram) throws SVMException {
		if ( requireNonNull(lineInfo, "lineInfo").symbols().size() != 4 ) // Syntax siehe oben
			throw new SVMException("MOV erwartet drei Parameter: "+lineInfo.symbols());
		if ( !lineInfo.symbols().get(0).equals(SVMLexer.SYM_MOV) )
			throw new SVMException("MOV erwartet Symbol "+SVMLexer.SYM_MOV);
		for ( int i = 0; i < PARAMS.length; i++ )
			checkParameterToken(i+1, lineInfo.symbols().get(i+1).token(), PARAMS[i]);

		/* Die Parameterliste für die MOV-Instruktion */
		Symbol param1 = lineInfo.symbols().get(1); // Beispiel MOV $1, %2 wäre hier das zweite Symbol $1 (erste Symbol ist MOV)
		Symbol param2 = lineInfo.symbols().get(3); // Beispiel MOV $1, %2 wäre hier das vierte Symbol %2 (drittes Symbol ist ,)
		byte[] params = new byte[5]; // Die fünf Parameter zur Instruktion MOV
		params[0] = (byte) (requireNonNull(PARAM1_MAP.get(param1.token()), "Kein Wert param1") * 16 +
				requireNonNull(PARAM1_MAP.get(param2.token()), "Kein Wert param2")); // 1. von wo nach wo
		short wert = switch ( param1.token() ) {
			case Token.REGISTER, Token.CONSTANT -> param1.getIntValue().get().byteValue();
			default -> 0;
		};
		params[1] = (byte) (wert >> 8);

		return new VirtualInstruction<>(
				Helper.getLabel(label),
				new InstructionDefinition<>(InstructionFactory.MOV, new byte[] {lineInfo.symbols().get(1).getIntValue().get().byteValue()}, null),
				new Label[]{ null } /* Eine Parameter, keine Referenz, muss eine Konstante sein */);
	}

}
