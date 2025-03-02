package com.github.mbeier1406.svm.prg.parser.instructions;

import org.apache.commons.lang.NotImplementedException;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

/**
 * Diese Klasse beinhaltet gemeinsame Methoden und Daten der
 * einzelnen {@linkplain InstructionParser}-Implementierungen.
 * Außerdem kann mittels {@linkplain InstructionParserBase#getInstructionParser(Symbol)}
 * eine Implementierung für ein Code-Symbol ({@code nop}, ...) geladen werden.
 * @param <T> Die Wortlänge der {@linkplain SVM}
 * @see Nop
 */
public class InstructionParserBase<T> implements InstructionParser<T> {

	/**
	 * {@inheritDoc}
	 * <p/>
	 * Diese Methode ist nicht zur Ausführung gedacht, da sie in den einzelnen
	 * Instruktionsimplemntierungen verwendet werdne muss (zum Beispiel
	 * {@linkplain Nop#getVirtualInstruction(Symbol, LineInfo, SVMProgram)}).
	 */
	@Override
	public VirtualInstruction<T> getVirtualInstruction(Symbol label, LineInfo lineInfo, SVMProgram<T> svmProgram) throws SVMException {
		throw new NotImplementedException("Bitte eine der Implementierungen Nop, Int, Mov usw. verwenden!");
	}

}
