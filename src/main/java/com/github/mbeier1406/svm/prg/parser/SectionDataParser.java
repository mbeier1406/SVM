package com.github.mbeier1406.svm.prg.parser;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

public class SectionDataParser {

	public static final Logger LOGGER = LogManager.getLogger(ProgramParser.class);

	public void parse(final SVMProgram<Short> svmProgram, final List<List<Symbol>> fileSymbols) throws SVMException {
		boolean isInDataSection = false;
		if ( svmProgram.getDataList() != null )
			throw new SVMException("Programm enthält bereits Daten!");
		for ( var lineSymbols : fileSymbols ) {
			LOGGER.trace("");
		}
	}

}
