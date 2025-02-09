package com.github.mbeier1406.svm.prg.parser;

import java.util.List;

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

	/** {@inheritDoc} */
	@Override
	public int parse(final SVMProgram<Short> svmProgram, final List<LineInfo> lineInfoList, int startIndex) throws SVMException {
		return 0;
	}

}
