package com.github.mbeier1406.svm.prg.parser;

import static org.apache.logging.log4j.CloseableThreadContext.put;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgramShort;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexerImpl;

/**
 * Standardimplementierung zur Erzeugung eines SVM-Programms in
 * interner Darstellung aus einer Textdatei.
 * @see SVMProgram
 */
public class SVMParserShort implements SVMParser<Short> {

	public static final Logger LOGGER = LogManager.getLogger(SVMParserShort.class);

	/** Das aus der SVM-textdatei erstelle Programm (interne Darstellung) */
	private final SVMProgram<Short> svmProgram = new SVMProgramShort();

	/** Das Programm zur lexikalischen Analyse, dessen Ergebnis zum Parsen verwendet wird */
	private final SVMLexer svmLexer = new SVMLexerImpl();

	/** Liest die Datensektion ein und schreibt die Werte in {@linkplain #svmProgram} */
	private SectionDataParser<Short> sectionDataParser = new SectionDataParserShort();

	private SectionCodeParser<Short> sectionCodeParser = new SectionCodeParserShort();


	public SVMParserShort() {
	}
	
	/** {@inheritDoc} */
	@Override
	public SVMProgram<Short> parse(File file, Charset encoding) throws SVMException {
		try ( @SuppressWarnings("unused") CloseableThreadContext.Instance ctx = put("file", file.toString()) ) {
			LOGGER.trace("Start parsen...");
			var lineInfoList = svmLexer.scan(file, encoding);
			LOGGER.debug("Abzahl Lines: {}", lineInfoList.size());
			parse(lineInfoList);
			LOGGER.trace("Ende parsen.");
			return svmProgram;
		}
	}

	/** {@inheritDoc} */
	@Override
	public SVMProgram<Short> parse(final List<LineInfo> lineInfoList) throws SVMException {
		LOGGER.trace("Parse die Datensektion...");
		int indexNextSection = sectionDataParser.parse(svmProgram, lineInfoList);
		LOGGER.trace("Ende parsen Datensektion (indexNextSection={}).\nParse die Codesektion...", indexNextSection);
		indexNextSection = sectionCodeParser.parse(svmProgram, lineInfoList.subList(indexNextSection, lineInfoList.size()));
		LOGGER.trace("Ende parsen Codesektion (indexNextSection={}).", indexNextSection);
		return svmProgram;
	}

}
