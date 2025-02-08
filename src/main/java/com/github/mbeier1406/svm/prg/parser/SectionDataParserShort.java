package com.github.mbeier1406.svm.prg.parser;

import static com.github.mbeier1406.svm.prg.lexer.SVMLexer.SYM_TOKEN_DATA;
import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;

/**
 * Standardimplementierung für das Parsen einer Datensektion eines SVM-Programms.
 * Die im SVM-Programm (externe Darstellung) enthaltenen Zeichen der Datensektion
 * werden in den Datentyp {@linkplain Short} umgewandelt.
 * @see Die Externe Darstellung: <code>/SVM/src/test/resources/com/github/mbeier1406/svm/prg/example.svm</code>
 */
public class SectionDataParserShort implements SectionDataParser<Short> {

	public static final Logger LOGGER = LogManager.getLogger(SectionDataParserShort.class);

	/** Angabe, an welchem Index der Liste {@linkplain LineInfo} gerade geparsed wird */
	private static final String INDEX = "Index %d: ";

	/** Fehlermeldung wenn das SVM-programm bereits Daten enthält */
	private static final String ERR_PRG_DATA_SECTION_NOT_EMPTY = "SVM-Programm enthält bereits Daten!";

	/** Fehlermeldung wenn nicht mit der Datensektion begonnen wird */
	private static final String ERR_DATA_SECTION_EXPECTED = INDEX+"Es wird die Datensektion erwartet: '"+SYM_TOKEN_DATA+"': %s";

	/** Fehlermeldung wenn nach Label das Programm endet */
	private static final String ERR_DATA_EXPECTED1 = INDEX+"Nach einer Labeldefinition darf das Programm nicht enden (%s)!";

	/** Fehlermeldung wenn nicht mit der Datensektion begonnen wird */
	private static final String ERR_DATA_EXPECTED2 = INDEX+"Nach einer Labeldefinition wird eine Datendefinition erwartet (%s): : %s";


	/** {@inheritDoc} */
	@Override
	public int parse(final SVMProgram<Short> svmProgram, final List<LineInfo> lineInfoList) throws SVMException {
		int index = 0; // Die Datensektion wird als erstes erwartet, wir starten an Index 0
		if ( requireNonNull(svmProgram, "svmProgram").getDataList().size() > 0 )
			throw new SVMException(ERR_PRG_DATA_SECTION_NOT_EMPTY);
		var symbols = requireNonNull(lineInfoList, "lineInfoList").get(index).symbols();
		if ( symbols.size() != 1 || !symbols.get(index).equals(SYM_TOKEN_DATA) )
			throw new SVMException(format(ERR_DATA_SECTION_EXPECTED, index, lineInfoList.get(index).line()));
		index++; // nächste Zeile
		Symbol label = null;
		for ( var lineInfo : lineInfoList.subList(index, lineInfoList.size()) ) {
			LOGGER.trace("lineInfo={}", lineInfo);
			if ( label != null ) {
				// Wir haben zuvor einen Label gelesen, jetzt wird die zugehörige Datendefinition erwartet
				if ( lineInfo.symbols().size() != 1 || lineInfo.symbols().get(0).token() != SVMLexer.Token.DATA )
					throw new SVMException(format(ERR_DATA_EXPECTED2, index, label, lineInfoList.get(index).line()));
				// Konstante in das SVMProgramm eintragen
				var data = lineInfo.symbols().get(0);
				LOGGER.trace("Data={}", data);
				svmProgram.addData(new Data<Short>(new Label(LabelType.DATA, label.value()), getSvmData(data)));
				label = null; // nächste Datendefinition
				index++; // nächste Zeile
			}
			else if ( lineInfo.symbols().size() == 1 && lineInfo.symbols().get(0).token() == SVMLexer.Token.LABEL ) {
				// Label-/Datendefinition gefunden
				label = lineInfo.symbols().get(0);
				LOGGER.trace("label={}", label);
				index++;
			}
			else
				break; // Keine Datendefinition, mit der Codesektion weitermachen...
		}
		if ( label != null )
			throw new SVMException(format(ERR_DATA_EXPECTED1, index, label));
		return index;
	}


	/** {@inheritDoc} */
	@Override
	public Short[] getSvmData(final Symbol data) {
		if ( requireNonNull(data, "data").token() != Token.DATA )
			throw new IllegalArgumentException("Symbol vom Typ 'DATA' erwartet: "+data);
		final Short[] daten = new Short[data.getStringValue().get().length()];
		for ( int i=0; i < daten.length; i++ )
			daten[i] = (short) data.getStringValue().get().charAt(i);
		return daten;
	}

}
