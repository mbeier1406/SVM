package com.github.mbeier1406.svm.prg.parser;

import java.util.List;

import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

/**
 * Definiert die Methode zum parsen der Datensektion eines SVM-Programms (externe Darstelllung).
 * Das zu erstellende {@linkplain SVMProgram} wird entsprechend
 * mit den Daten befüllt.
 * @param <T> Die Wortlänge der {@linkplain SVM}
 * @see Ein Beispiel SVM-Programm (externe Repräsentation) /SVM/src/test/resources/com/github/mbeier1406/svm/prg/example.svm
 */
public interface SectionDataParser<T> {

	/**
	 * Legt fest, ob in die {@linkplain Data}-Items Debugging-Informationen geschrieben wird.
	 * Diese besteht aus Zeilennummer und Zeile aus {@linkplain LineInfo}. Standardmäßig ausgeschaltet.
	 * @param debugging wenn <b>true</b>, wird die Information im {@linkplain SVMProgram} gespeichert, sonst nicht
	 */
	public void setDebugging(boolean debugging);

	/**
	 * Übernimmt das Ergebnis der lexikalischen Analyse eines {@linkplain SVMLexer} und
	 * überträgt die Datensektion in das zu erstellende {@linkplain SVMProgram} (interne Darstellung). Es wird erwartet,
	 * dass die erste Zeile die Datensektion kennzeichnet ({@linkplain SVMLexer#SYM_TOKEN_DATA}.
	 * Es wird gelesen, bis die Codesektion erreicht wird (das Symbol {@linkplain SVMLexer#SYM_TOKEN_CODE}).
	 * @param svmProgram Das zu erstellende Progamm (interne Darstellung)
	 * @param lineInfoList Das Ergebnis der lexikalischen Analyse {@linkplain SVMLexer#scan(String)}
	 * @param startIndex (standardmäßig beginnend ab 0) Index in der <u>lineInfoList</u>, ab der geparsed wird
	 * @return Den Index (beginnend ab 0 wenn keine Daten) in der Liste {@linkplain SVMLexer.LineInfo}, an der mit der nächsten Sektion weitergemacht werden muss
	 * @throws SVMException Bei Parsingfehlern oder wenn das {@linkplain SVMProgram} bereits Daten enthält
	 */
	public int parse(final SVMProgram<Short> svmProgram, final List<LineInfo> lineInfoList, int startIndex) throws SVMException;

	/** @see #parse(SVMProgram, List, int) */
	public default int parse(final SVMProgram<Short> svmProgram, final List<LineInfo> lineInfoList) throws SVMException{
		return parse(svmProgram, lineInfoList, 0);
	}

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
