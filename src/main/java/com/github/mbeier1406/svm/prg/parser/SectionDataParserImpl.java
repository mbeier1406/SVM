package com.github.mbeier1406.svm.prg.parser;

import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TOKEN_DATA;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;

/**
 * Standardimplementierung für das Parsen einer Datensektion eines SVM-Programms.
 */
public class SectionDataParserImpl implements SectionDataParser {

	public static final Logger LOGGER = LogManager.getLogger(SectionDataParserImpl.class);

	/** Angabe, an welchem Index der Liste {@linkplain LineInfo} gerade geparsed wird */
	private static final String INDEX = "Index %d: ";

	/** Fehlermeldung wenn das SVM-programm bereits Daten enthält */
	private static final String ERR_PRG_DATA_SECTION_NOT_EMPTY = "SVM-Programm enthält bereits Daten!";

	/** Fehlermeldung wenn nicht mit der Datensektion begonnen wird */
	private static final String ERR_DATA_SECTION_EXPECTED = INDEX+"Es wird die Datensektion '"+SYM_TOKEN_DATA+"' erwartet!";


	/** {@inheritDoc} */
	@Override
	public int parse(final SVMProgram<Short> svmProgram, final List<LineInfo> lineInfoList) throws SVMException {
		int index = 0; // Die Datensektion wird als erstes erwartet, wir starten an Index 0
		if ( requireNonNull(svmProgram, "svmProgram").getDataList().size() > 0 )
			throw new SVMException(ERR_PRG_DATA_SECTION_NOT_EMPTY);
		var symbols = requireNonNull(lineInfoList, "lineInfoList").get(index).symbols();
		if ( symbols.size() != 1 || !symbols.get(index).equals(SYM_TOKEN_DATA) )
			throw new SVMException(format(ERR_DATA_SECTION_EXPECTED, index));
		for ( var lineInfo : lineInfoList ) {
			LOGGER.trace("lineInfo={}", lineInfo);
		}
		return index;
	}

}
