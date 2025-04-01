package com.github.mbeier1406.svm.prg.parser;

import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TOKEN_CODE;

import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;
import com.github.mbeier1406.svm.prg.parser.instructions.InstructionParser;
import com.github.mbeier1406.svm.prg.parser.instructions.InstructionParserBase;
import com.github.mbeier1406.svm.prg.parser.instructions.Int;
import com.github.mbeier1406.svm.prg.parser.instructions.Nop;

/**
 * Standardimplementierung für das Parsen einer Codesektion eines SVM-Programms.
 * Die im SVM-Programm (externe Darstellung) enthaltenen Parameter der Instruktionen der Codesektion
 * werden in den Datentyp {@linkplain Short} umgewandelt.
 * @see Die Externe Darstellung: <code>/SVM/src/test/resources/com/github/mbeier1406/svm/prg/example.svm</code>
 */
public class SectionCodeParserShort implements SectionCodeParser<Short> {

	public static final Logger LOGGER = LogManager.getLogger(SectionDataParserShort.class);

	/** Angabe, an welchem Index der Liste {@linkplain LineInfo} gerade geparsed wird */
	private static final String INDEX = "Index %d: ";

	/** Fehlermeldung wenn eine Zeile nach dem Code-Symbol nicht mit einem Label oder einer Instruktion beginnt */
	private static final String ERR_CODE_OR_LABEL_EXPECTED = INDEX+"Labeldefinition oder Codezeile erwartet: : %s";

	/** Fehlermeldung wenn eine Zeile nach dem Code-Symbol nicht mit einem Label oder einer Instruktion beginnt */
	private static final String ERR_DOUBLE_LABEL_DEFINITION = INDEX+"Labeldefinition gefunden während Label '%s' gesetzt ist: %s";

	/** Fehlermeldung wenn bei Erzeugung des Codes einer Instruktion Fehler auftraten */
	private static final String ERR_CODE_GENERATION = INDEX+"Fehler bei Erzeugung des Codes für '%s' (lineInfo=%s, label=%s)";

	/**
	 * Liefert die Implementierung, die aus einer SVM-Programmzeile als Liste von {@linkplain Symbol}en die
	 * Implemntierung liefert, die zu der Instruktion gehört und die Codezeile in eine {@linkplain VirtualInstruction}
	 * überführt. <p>Beispiel</p>Programmzeile:
	 * <pre><code>
	 * int $1
	 * </code></pre>
	 * Liefert eine {@linkplain LineInfo} mit der Symbolliste {@linkplain SVMLexer#SYM_INT} und einem {@linkplain Symbol}
	 * mit {@linkplain Token#CONSTANT} mit Wert 1. Um diese Symbole in SVM-Instruktionen zu übersetzen, wird ein Objekt des
	 * Typs {@linkplain Int} erzeugt, und über {@linkplain Int#getVirtualInstruction(Symbol, LineInfo, SVMProgram)} der
	 * entsprechende Code generiert. Dieser kann dann mittels {@linkplain SVMProgram#addInstruction(VirtualInstruction)}
	 * dem ausführbaren Programm hinzugefügt werden.
	 * @see Nop
	 */
	private InstructionParser<Short> ipImplService = new InstructionParserBase<Short>();

	/** Definiert, ob Debugginginfos in die {@linkplain VirtualInstruction}-Items geschrieben wird */
	private boolean debugging = false;


	/** {@inheritDoc} */
	@Override
	public void setDebugging(boolean debugging) {
		this.debugging = debugging;
	}

	/** {@inheritDoc} */
	@Override
	public int parse(final SVMProgram<Short> svmProgram, final List<LineInfo> lineInfoList, int startIndex) throws SVMException {
		int index = SVMParser.checkSection(lineInfoList, startIndex, SYM_TOKEN_CODE, svmProgram::getInstructionList);
		LOGGER.trace("startIndex={}; index={}; Anzahl lineInfoList={}", startIndex, index, lineInfoList.size());
		Symbol label = null; // Der Label dient als Ziel für eine Sprunganweisung
		for ( var lineInfo : lineInfoList.subList(index, lineInfoList.size()) ) {
			LOGGER.trace("lineInfo={}", lineInfo);
			Symbol codeSymbol = Objects.requireNonNull(lineInfo.symbols().get(0), "lineInfo.symbols().get(0)");
			LOGGER.trace("codeSymbol={}", codeSymbol);
			if ( lineInfo.symbols().size() == 1 && codeSymbol.token() == SVMLexer.Token.LABEL ) {
				// Labeldefinition als Sprungadresse gefunden
				if ( label != null )
					throw new SVMException(String.format(ERR_DOUBLE_LABEL_DEFINITION, index, label, lineInfo));
				label = codeSymbol;
				LOGGER.trace("label={}", label);
			}
			else if ( codeSymbol.token() == SVMLexer.Token.CODE ) {
				try {
					var instructionParser = ipImplService.getInstructionParser(codeSymbol);
					LOGGER.trace("instructionParser={}", instructionParser);
					var virtualInstruction = instructionParser.getVirtualInstruction(label, lineInfo, svmProgram, this.debugging);
					LOGGER.trace("label={}; svmProgram={}; virtualInstruction={}", label, svmProgram, virtualInstruction);
					svmProgram.addInstruction(virtualInstruction);
					label = null;
				}
				catch ( Exception e ) {
					throw new SVMException(String.format(ERR_CODE_GENERATION, index, codeSymbol, lineInfo, label), e);
				}
			}
			else
				throw new SVMException(String.format(ERR_CODE_OR_LABEL_EXPECTED, index, lineInfo));
			index++; // Nächste Instruktion
		}
		return index;
	}

}
