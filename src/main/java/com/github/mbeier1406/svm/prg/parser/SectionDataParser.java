package com.github.mbeier1406.svm.prg.parser;

import java.util.List;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;

/**
 * Definiert die Methode zum parsen eine Datensektion.
 * Das zu erstellende {@linkplain SVMProgram} wird entsprechend
 * mit den Daten befüllt.
 */
public interface SectionDataParser {

	/**
	 * Übernimmt das Ergebnis der lexikalischen Analyse eines {@linkplain SVMLexer} und
	 * überträgt die Datensektion in das zu erstellende SVM-Programm. Es wird erwartet,
	 * dass die erste Zeile die Datensektion kennzeichnet ({@linkplain SVMLexer#SYM_TOKEN_DATA}.
	 * Es wird gelesen, bis die Codesektion erreicht wird (das Symbol {@linkplain SVMLexer#SYM_TOKEN_CODE}).
	 * @param svmProgram Das zu erstellende Progamm (interne Darstellung)
	 * @param lineInfoList Das Ergebnis der lexikalischen Analyse {@linkplain SVMLexer#scan(String)}
	 * @return Den Index (beginnend ab 0) in der Liste {@linkplain SVMLexer.LineInfo}, an der mit der nächsten Sektion weitergemacht werden muss
	 * @throws SVMException Bei Parsingfehlern oder wenn das {@linkplain SVMProgram} bereits Daten enthält
	 */
	public int parse(final SVMProgram<Short> svmProgram, final List<LineInfo> lineInfoList) throws SVMException;

}
