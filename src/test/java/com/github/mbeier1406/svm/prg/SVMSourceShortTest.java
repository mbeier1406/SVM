package com.github.mbeier1406.svm.prg;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain SVMSourceShort}.
 */
public class SVMSourceShortTest {

	public static final Logger LOGGER = LogManager.getLogger(SVMSourceShortTest.class);

	/** Der Pfad, untern dem das Programm {@linkplain SVMLoaderShortTest#getKorrektesProgramm()} gespeichert wird ist {@value} */
	public static final String PRG_DATEI = "program.svm";

	/** Das zu testende Objekt */
	public SVMSource<Short> svmSource = new SVMSourceShort();


	/** Speichert ein vorgegebenes programm, liest es wieder ein und prüft es */
	@Test
	public void testeSchreibenUndLesen() throws Exception {
		var korrektesProgramm = SVMLoaderShortTest.getKorrektesProgramm();
		LOGGER.trace("Speichere {}...", PRG_DATEI);
		svmSource.save(korrektesProgramm, PRG_DATEI);
		var svmProgram = svmSource.load(PRG_DATEI);
		svmProgram.validate();
		assertThat(korrektesProgramm.getDataList(), equalTo(svmProgram.getDataList()));
		assertThat(korrektesProgramm.getInstructionList(), equalTo(svmProgram.getInstructionList()));
	}

}
