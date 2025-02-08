package com.github.mbeier1406.svm.prg.parser;

import java.util.List;

import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

/**
 * Definiert die Methode zum parsen eine Datensektion.
 * Das zu erstellende {@linkplain SVMProgram} wird entsprechend
 * mit den Daten befüllt.
 * @param <T> Die Wortlänge der {@linkplain SVM}
 */
public interface SectionDataParser<T> {

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

	/**
	 * Überführt eine Datendefinition aus der externen SVM-Programm Darstellung
	 * in die interne Repräsentation der {@linkplain SVM}. Beispiel SVM vom Typ {@linkplain Short}
	 * (externe Repräsentation):
	 * <pre><code>
	 * 	&data
	 * .label1
	 * 	abc
	 * </code></pre>
	 * liefert ein Short-Array {@code [ 'a', 'b', 'c' ]}
	 * @param data die Datendefinition (extern)
	 * @return die Datendefinition (intern)
	 */
	public T[] getSvmData(final Symbol data);

}
