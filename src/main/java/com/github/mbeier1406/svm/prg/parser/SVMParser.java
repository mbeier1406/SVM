package com.github.mbeier1406.svm.prg.parser;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMLoader;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

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

	/** Fehlermeldung wenn der Startindex außerhalb des Bereichs der Infoliste liegt */
	public static final String ERR_INVALID_START_INDEX = "Der Startindex muss zwischen 0 und %s liegen: %d";

	/** Fehlermeldung wenn das SVM-Programm bereits Daten enthält */
	public static final String ERR_PRG_DATA_SECTION_NOT_EMPTY = "SVM-Programm enthält bereits Daten!";

	/** Fehlermeldung wenn nicht mit der Datensektion begonnen wird */
	public static final String ERR_DATA_SECTION_EXPECTED = "Index %d: Es wird die Datensektion erwartet: '%s': %s";


	/**
	 * Diese Methode prüft die Parameter {@linkplain SVMProgram}, Liste {@linkplain LineInfo} und den Index, ab dem
	 * geparsed werden soll (die {@linkplain SVMLexer#SYM_TOKEN_DATA Daten-} bzw. {@linkplain SVMLexer#SYM_TOKEN_CODE Codesektion})
	 * auf Korrektheit und liefert den Index, ab dem weiter geparsed werden muss (normalerweise der übergebene Index plus 1
	 * für die Sektionsdefinition). Die Sektion selbst wird nicht geparsed, nur die Definition.
	 * @param svmProgram das aus der Liste {@linkplain LineInfo} zu erstellende {@linkplain SVMProgram} (interne Darstellung)
	 * @param lineInfoList Ergebnis deer {@linkplain SVMLexer lexikalischen Analyse} des SVM-Programms (externe Darstellung)
	 * @param startIndex Der Index in der Liste {@linkplain LineInfo}, ab dem geparsed wird
	 * @param symbol Das am Index erwartete Symbol, {@linkplain SVMLexer#SYM_TOKEN_DATA} für die Daten-, {@linkplain SVMLexer#SYM_TOKEN_CODE} für die Codesektion
	 * @return Der Index ab dem nach erfolgreicher Prüfung weiter geparsed wird
	 * @throws SVMException
	 */
	public static int checkSection(final SVMProgram<Short> svmProgram, final List<LineInfo> lineInfoList, int startIndex, final Symbol symbol) throws SVMException {
		int anzahlLines = requireNonNull(lineInfoList, "lineInfoList").size();
		if ( anzahlLines == 0 ) return 0; // keine Daten
		if ( startIndex < 0 || startIndex >= anzahlLines )
			throw new SVMException(format(ERR_INVALID_START_INDEX, lineInfoList==null?"?":lineInfoList.size()-1, startIndex));
		if ( requireNonNull(svmProgram, "svmProgram").getDataList().size() > 0 )
			throw new SVMException(ERR_PRG_DATA_SECTION_NOT_EMPTY);
		var symbols = lineInfoList.get(startIndex).symbols();
		if ( symbols.size() != 1 || !symbols.get(startIndex).equals(symbol) )
			throw new SVMException(format(ERR_DATA_SECTION_EXPECTED, startIndex, symbol, lineInfoList.get(startIndex).line()));
		return startIndex+1;
	}

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
