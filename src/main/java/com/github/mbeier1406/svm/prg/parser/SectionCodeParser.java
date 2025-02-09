package com.github.mbeier1406.svm.prg.parser;

import java.util.List;

import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;

/**
 * Definiert die Methode zum parsen der Codesektion eines SVM-Programms (externe Darstelllung).
 * Das zu erstellende {@linkplain SVMProgram} wird entsprechend mit den {@linkplain InstructionDefinition Instruktionen} befüllt.
 * @param <T> Die Wortlänge der {@linkplain SVM}
 * @see Ein Beispiel SVM-Programm (externe Repräsentation) /SVM/src/test/resources/com/github/mbeier1406/svm/prg/example.svm
 */
public interface SectionCodeParser<T> {

	/**
	 * Übernimmt das Ergebnis der lexikalischen Analyse eines {@linkplain SVMLexer} und
	 * überträgt die Codesektion in das zu erstellende {@linkplain SVMProgram} (interne Darstellung). Es wird erwartet,
	 * dass die erste Zeile die Codesektion kennzeichnet ({@linkplain SVMLexer#SYM_TOKEN_CODE}).
	 * Es wird bis zum Ende der {@linkplain LineInfo}-Liste gelesen.
	 * @param svmProgram Das zu erstellende Progamm (interne Darstellung)
	 * @param lineInfoList Das Ergebnis der lexikalischen Analyse {@linkplain SVMLexer#scan(String)}
	 * @param startIndex Index (nach {@linkplain SectionDataParser#parse(SVMProgram, List, int)} in der <u>lineInfoList</u>, ab der geparsed wird
	 * @return Den Index (Letzter Eintrag) in der Liste {@linkplain SVMLexer.LineInfo}
	 * @throws SVMException Bei Parsingfehlern oder wenn das {@linkplain SVMProgram} bereits Code enthält
	 */
	public int parse(final SVMProgram<Short> svmProgram, final List<LineInfo> lineInfoList, int startIndex) throws SVMException;

	/** @see #parse(SVMProgram, List, int) */
	public default int parse(final SVMProgram<Short> svmProgram, final List<LineInfo> lineInfoList) throws SVMException {
		return parse(svmProgram, lineInfoList, 0);
	}

}
