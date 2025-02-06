package com.github.mbeier1406.svm.prg.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;
import com.github.mbeier1406.svm.prg.SVMProgramShort;

/**
 * Tests für die Klasse {@linkplain SectionDataParserImpl}.
 */
public class SectionDataParserImplTest {

	public static final Logger LOGGER = LogManager.getLogger(SectionDataParserImplTest.class);

	/** Das zu testende Objekt */
	public final SectionDataParser sectionDataParser = new SectionDataParserImpl();

	/** Das zu erstellende Programm */
	public final SVMProgram<Short> svmProgramm = new SVMProgramShort();


	/** Stellt sicher, dass ein fehlende {@linkplain SVMProgram} einen definierten Fehler liefert */
	@Test
	public void testeSVMProgramNullValue() {
		var ex = assertThrows(NullPointerException.class, () -> sectionDataParser.parse(null, new ArrayList<>()));
		assertThat(ex.getLocalizedMessage(), containsString("svmProgram"));
	}

	/** Stellt sicher, dass eine fehlende Symbolliste einen definierten Fehler liefert */
	@Test
	public void testeLineInfoListNullValue() {
		var ex = assertThrows(NullPointerException.class, () -> sectionDataParser.parse(svmProgramm, null));
		assertThat(ex.getLocalizedMessage(), containsString("lineInfoList"));
	}

	/** Stellt sicher, dass ein definierten Fehler erzeugt wird, wenn das Programm bereits Daten enthält */
	@Test
	public void testeSVMProgramDataSectionNotEmpty() {
		svmProgramm.addData(new SVMProgram.Data<Short>(new SVMProgram.Label(LabelType.DATA, "xyz"), new Short[] {1}));
		var ex = assertThrows(SVMException.class, () -> sectionDataParser.parse(svmProgramm, new ArrayList<>()));
		assertThat(ex.getLocalizedMessage(), containsString("enthält bereits Daten"));
	}

}
