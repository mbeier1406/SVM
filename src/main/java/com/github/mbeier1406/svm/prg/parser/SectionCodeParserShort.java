package com.github.mbeier1406.svm.prg.parser;

import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TOKEN_CODE;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

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

	/** Fehlermeldung wenn eine Zeile nach dem Code-Symbol nicht mit einer Instruktion beginnt */
	private static final String ERR_CODE_EXPECTED = INDEX+"Eine Codezeile muss mit einer Instruktion beginnen: %s";


	/** {@inheritDoc} */
	@Override
	public int parse(final SVMProgram<Short> svmProgram, final List<LineInfo> lineInfoList, int startIndex) throws SVMException {
		int index = SVMParser.checkSection(svmProgram, lineInfoList, startIndex, SYM_TOKEN_CODE);
		LOGGER.trace("startIndex={}; index={}; Anzahl lineInfoList={}", startIndex, index, lineInfoList.size());
		Symbol label = null; // Wir erwarten zunächst eine Instruktion
		for ( var lineInfo : lineInfoList.subList(index, lineInfoList.size()) ) {
			LOGGER.trace("lineInfo={}", lineInfo);
			if ( lineInfo.symbols().size() == 1 && lineInfo.symbols().get(0).token() == SVMLexer.Token.LABEL ) {
				// Labeldefinition als Sprungadresse gefunden
				label = lineInfo.symbols().get(0);
				LOGGER.trace("label={}", label);
				index++;
			}
			else if ( lineInfo.symbols().get(0).token() != SVMLexer.Token.CODE ) {
//					throw new SVMException(String.format(ERR_CODE_EXPECTED, index, lineInfo));
				label = null;
			}
		}
		return index;
	}

}
