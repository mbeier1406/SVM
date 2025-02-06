package com.github.mbeier1406.svm.prg.parser;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;

public class ProgramParser {

	public static final Logger LOGGER = LogManager.getLogger(ProgramParser.class);

	private SectionDataParserImpl sectionDataParser = new SectionDataParserImpl();

	private SectionCodeParser sectionCodeParser = new SectionCodeParser();

	public void parse(final SVMProgram<Short> svmProgram, final List<List<Symbol>> symbols) throws SVMException {
		
	}

}
