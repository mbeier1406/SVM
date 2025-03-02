package com.github.mbeier1406.svm.prg.parser.instructions;

import static com.github.mbeier1406.svm.prg.parser.Helper.checkParameterToken;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.github.mbeier1406.svm.prg.parser.functions.FunctionFactory;

/**
 * Erzeugt aus einer vom {@linkplain SVMLexer} eingelesenen Codezeile
 * mit einer Instruktion {@linkplain com.github.mbeier1406.svm.instructions.Mov}
 * die zugehörige virtuelle Instruktion.<p/>
 * Beispiel Syntax 1 (ohne Funktion):
 * <pre><code>
 * .label1	# Optional
 * 	mov $1, %1
 * </code></pre><p/>
 * Beispiel Syntax 2 (mit Funktion):
 * <pre><code>
 * 	&data
 * .label1
 * 	abc
 * 	&code
 * 	mov len(label1), %1
 * </code></pre><p/>
 * Syntax: {@code mov ((<Number>|<Register>)|(<Function>\(<Label-Reference>\)) , (<Register>|<Register-Reference>)}
 * @see {@linkplain InstructionFactory#INSTRUCTIONS}
 * @see com.github.mbeier1406.svm.instructions.Mov
 */
public class Mov implements InstructionParser<Short> {

	/** Liste der <u>drei</u> Token/Typen, die als jeweiliger Parameter zulässig sind bei Syntax 1 (ohne Funktion) */
	public static final Token[][] PARAMS4 = new Token[][] {
		new Token[] { Token.CONSTANT, Token.REGISTER },
		new Token[] { Token.COMMA },
		new Token[] { Token.REGISTER } // später auch Registerreferenz
	};

	/** Liste der <u>sechs</u> Token/Typen, die als jeweiliger Parameter zulässig sind bei Syntax 2 (mit Funktion) */
	public static final Token[][] PARAMS7 = new Token[][] {
		new Token[] { Token.FUNCTION },
		new Token[] { Token.LEFTPAR },
		new Token[] { Token.LABEL_REF },
		new Token[] { Token.RIGHTPAR },
		new Token[] { Token.COMMA },
		new Token[] { Token.REGISTER } // später auch Registerreferenz
	};

	/**
	 * Der <u>erste</u> Parameter zur MOV-Instruktion ist ein Byte, dass angibt, von wo
	 * nach wo verschoben werden soll. Beispiel <code>21</code> gibt an, dass eine
	 * Konstante in ein Register verschoben werden soll. Als SVM-Programm Beispiel:
	 * {@code mov $2 %1}
	 * @see com.github.mbeier1406.svm.instructions.Mov
	 */
	@SuppressWarnings({ "unused", "serial" })
	private static final Map<Token, Byte> PARAM1_MAP = new HashMap<Token, Byte>() {{
		put(Token.REGISTER, (byte) 1);
		put(Token.FUNCTION, (byte) 2); // Funktionen müssen eine Konstante Zahl als Ergebnis liefern
		put(Token.CONSTANT, (byte) 2);
		put(Token.LABEL_REF, (byte) 3);
	}};

	/** {@inheritDoc} */
	@Override
	public VirtualInstruction<Short> getVirtualInstruction(final Symbol label, final LineInfo lineInfo, final SVMProgram<Short> svmProgram) throws SVMException {
		int numSymbols = requireNonNull(lineInfo, "lineInfo").symbols().size();
		if ( numSymbols != 4 && numSymbols != 7 ) // Syntax siehe oben
			throw new SVMException("MOV erwartet drei bzw. sechs Parameter: "+lineInfo.symbols());
		if ( !lineInfo.symbols().get(0).equals(SVMLexer.SYM_MOV) )
			throw new SVMException("MOV erwartet Symbol "+SVMLexer.SYM_MOV);
		boolean syntax1 = numSymbols == 4; // Syntax 1 mov x , y ohne Funktion
		Token[][] PARAMS = syntax1 ? PARAMS4 : PARAMS7;
		for ( int i = 0; i < PARAMS.length; i++ )
			checkParameterToken(i+1, lineInfo.symbols().get(i+1).token(), PARAMS[i]);

		final List<Symbol> sym = new ArrayList<>(); // Bei Funktionen muss die zugehörige Konstante (Zahl) berechnet werden
		if ( !syntax1 ) {
			// Es wird eine Funktion benutzt, befindet sich an Index 1 (mov len(label)...), die Labelreferenz an Index 3
			int wert = FunctionFactory.getFunction(lineInfo.symbols().get(1).value()).apply(lineInfo.symbols().get(3), requireNonNull(svmProgram, "svmProgram"));
			sym.add(new Symbol(Token.CONSTANT, String.valueOf(wert)));
		}

		/* Nachdem die Funktion aufgelöst wurde, Symbolliste vereinheitlichen */
		@SuppressWarnings("serial")
		List<Symbol> symbols = syntax1 ? // Beispiel mov $1, %1
				lineInfo.symbols() :     // Beispiel mov len(label1), %1
					new ArrayList<Symbol>() {{
						add(lineInfo.symbols().get(0)); // mov
						add(sym.get(0)); // die oben berechnete Konstante
						add(lineInfo.symbols().get(5)); // ,
						add(lineInfo.symbols().get(6)); // Ziel
					}};

		/*
		 * Die Parameterliste für die MOV-Instruktion.
		 * In der vereinheitlichten Symbolliste "symbols" bezeichnen Index 1 und 3 Quelle und Ziel von mov.
		 */
		byte[] params = new byte[5]; // Die fünf Parameter zur Instruktion MOV
		params[0] = (byte) (requireNonNull(PARAM1_MAP.get(symbols.get(1).token()), "Kein Wert param1") * 16 +
				requireNonNull(PARAM1_MAP.get(symbols.get(3).token()), "Kein Wert param2")); // 1. von wo nach wo
		int i=0; // Index in der Paramterliste des Maschinebefehls Mov
		for ( Symbol s : new Symbol[] {symbols.get(1), symbols.get(3)} ) { // Index 1 = Quelle, 3 = Ziel
			short wert = switch ( s.token() ) {
				case Token.REGISTER, Token.CONSTANT -> s.getIntValue().get().shortValue();
				default -> throw new SVMException("Param ungültig Index "+i+": "+s);
			};
			params[i+1] = (byte) ((wert >> 8) & 0xff);
			params[i+2] = (byte) (wert & 0xff);
			i += 2;
		}

		return new VirtualInstruction<>(
				Helper.getLabel(label),
				new InstructionDefinition<>(InstructionFactory.MOV, params, null),
				new Label[]{ null, null, null, null, null } /* Fünf Parameter, keine Referenzen, müssen Register oder Konstanten sein */);
	}

}
