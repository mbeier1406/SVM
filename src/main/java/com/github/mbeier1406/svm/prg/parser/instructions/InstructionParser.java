package com.github.mbeier1406.svm.prg.parser.instructions;

import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMLoader;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

/**
 * Dieses Interface definiert die Services die aus einer
 * durch den {@linkplain SVMLexer} eingelesenen Instruktion ({@linkplain LineInfo}
 * eine virtuelle Instruktion ({@linkplain VirtualInstruction} erzeugt, die in
 * eine {@linkplain SVM} zur Ausführung geladen werden kann.<p/>
 * <u>Hinweis</u>: die Implementierungen prüfen die Liste der übergebenen Symbole {@linkplain LineInfo}
 * auf syntaktische Korrektheit und erzeugen bei Fehlern eine {@linkplain SVMException}!
 * @see SVMLoader
 */
@FunctionalInterface
public interface InstructionParser<T> {

	/**
	 * Erzeugt aus einer duech den {@linkplain SVMLexer} eingelesenen Code-Zeile
	 * die virtuelle Instruktion, die in die SVM geladen werden kann. Beispiel:
	 * <pre><code>
	 * 	mov $1, %1
	 * </code></pre>
	 * Beispiel mit Label:
	 * <pre><code>
	 * .start1
	 * 	mov $1, %1
	 * </code></pre>
	 * <u>Hinweis</u>: das erste Symbol in {@linkplain LineInfo#symbols()} ist immer die Instruktion, für die
	 * die {@linkplain VirtualInstruction} gerade erzeugt werden soll.
	 * @param label Falls im Programmtext zuvor ein Label definiert wurde, muss er als Ziel für einen Sprungbefehl mitgegeben werden
	 * @param lineInfo die vom Lexer eingelesene Programmzeile als Liste von {@linkplain SVMLexer.Symbol Symbolen} mitgegeben wird
	 * @param svmProgram das bisher erzeugte Programm, wird für die Berechnung von Referenzen (Zugriff auf {@linkplain Label} benötigt
	 * @param debugging Ob Debugginginformationen in die {@linkplain VirtualInstruction} geschrieben werden soll
	 * @throws SVMException falls die Liste der Symbole in {@linkplain LineInfo} nicht zur Instruktion passt
	 * @return die virtuelle Instruktion
	 */
	public VirtualInstruction<T> getVirtualInstruction(final Symbol label, final LineInfo lineInfo, final SVMProgram<T> svmProgram, boolean debugging) throws SVMException;

	/** @see #getVirtualInstruction(Symbol, LineInfo, SVMProgram, boolean) */
	public default VirtualInstruction<T> getVirtualInstruction(final Symbol label, final LineInfo lineInfo, final SVMProgram<T> svmProgram) throws SVMException {
		return getVirtualInstruction(label, lineInfo, svmProgram, false);
	}

	public default InstructionParser<T> getInstructionParser(final Symbol symbol) throws SVMException {
		if ( symbol.token() != SVMLexer.Token.CODE )
			throw new SVMException("[getInstructionParser()] symbol="+symbol+": Token '"+SVMLexer.Token.CODE+"' erwartet!");
		try {
			final Class<?> instrParserClass = Class.forName(
					"com.github.mbeier1406.svm.prg.parser.instructions." +
					symbol.value().substring(0, 1).toUpperCase() +
					symbol.value().substring(1).toLowerCase());
			@SuppressWarnings("unchecked")
			final InstructionParser<T> instrParser = (InstructionParser<T>) instrParserClass.getDeclaredConstructor().newInstance();
			return instrParser;
		}
		catch ( Exception e ) {
			throw new SVMException("[getInstructionParser()] Keine SVM-Instruktion: symbol="+symbol, e);
		}
	}

}
