package com.github.mbeier1406.svm.prg.parser;

import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TOKEN_CODE;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;

/**
 * Standardimplementierung für das Parsen einer Codesektion eines SVM-Programms.
 * Die im SVM-Programm (externe Darstellung) enthaltenen Parameter der Instruktionen der Codesektion
 * werden in den Datentyp {@linkplain Short} umgewandelt.
 * @see Die Externe Darstellung: <code>/SVM/src/test/resources/com/github/mbeier1406/svm/prg/example.svm</code>
 */
public class SectionCodeParserShort implements SectionCodeParser<Short> {

	public static final Logger LOGGER = LogManager.getLogger(SectionDataParserShort.class);

	/** {@inheritDoc} */
	@Override
	public int parse(final SVMProgram<Short> svmProgram, final List<LineInfo> lineInfoList, int startIndex) throws SVMException {
		int index = SVMParser.checkSection(svmProgram, lineInfoList, startIndex, SYM_TOKEN_CODE);
		LOGGER.trace("startIndex={}; index={}; Anzahl lineInfoList={}", startIndex, index, lineInfoList.size());
		return 0;
	}

}
