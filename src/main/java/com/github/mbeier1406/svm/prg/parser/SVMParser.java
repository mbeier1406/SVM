package com.github.mbeier1406.svm.prg.parser;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMLoader;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;

/**
 * Dieses Interface definiert die Methoden und Datenstrukturen zur Erstellung
 * eines {@linkplain SVMProgram}s (interne Darstellung) aus einer Textdatei
 * (externe Darstellung eines SVM-Programms). Die Interne Darstellung kann in
 * eine {@linkplain SVM} zur Ausführung geladen werden.
 * @param <T> die Wortlänge der {@linkplain SVM}
 * @see SVMLexer
 * @see SVMLoader
 * @see Ein Beispiel SVM-Programm (externe Repräsentation) /SVM/src/test/resources/com/github/mbeier1406/svm/prg/example.svm
 */
public interface SVMParser<T> {

	/**
	 * Liest das SVM-Programm (externer Darstellung) aus der angegebenen Datei ein
	 * und erstellt daraus das SVM-Programm in interner Darstellung (Parser).
	 * @param file
	 * @param encoding
	 * @return
	 * @throws SVMException
	 */
	public SVMProgram<T> parse(File file, Charset encoding) throws SVMException;

	/**
	 * Methode zum Parsen eines {@linkplain SVM}-Programms (externe Darstellung) mit Standardkodierung.
	 * @see #parse(File, Charset)
	 */
	public default SVMProgram<T> parse(File file) throws SVMException {
		return parse(file, Charset.defaultCharset());
	}

	/**
	 * Methode zum Parsen eines {@linkplain SVM}-Programms (externe Darstellung) mit Standardkodierung.
	 * @see #parse(File, Charset)
	 */
	public default SVMProgram<T> parse(String file) throws SVMException {
		return parse(new File(file), Charset.defaultCharset());
	}

	/**
	 * Methode zum Parsen eines {@linkplain SVM}-Programms als Ergebnis der lexikalischen Analyse.
	 * @see #parse(File, Charset)
	 */
	public SVMProgram<T> parse(final List<LineInfo> symbols) throws SVMException;

}
