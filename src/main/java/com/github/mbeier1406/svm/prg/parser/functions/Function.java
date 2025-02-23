package com.github.mbeier1406.svm.prg.parser.functions;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

@FunctionalInterface
public interface Function {

	public int apply(final Symbol param, final SVMProgram<Short> svmProgram) throws SVMException;

}
