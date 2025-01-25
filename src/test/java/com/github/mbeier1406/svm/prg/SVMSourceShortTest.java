package com.github.mbeier1406.svm.prg;

import static com.github.mbeier1406.svm.instructions.InstructionFactory.INSTRUCTIONS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.Int;
import com.github.mbeier1406.svm.instructions.Mov;
import com.github.mbeier1406.svm.instructions.Nop;

/**
 * Tests für die Klasse {@linkplain SVMSourceShort}.
 */
public class SVMSourceShortTest {

	public static final Logger LOGGER = LogManager.getLogger(SVMSourceShortTest.class);

	/** Der Pfad, untern dem das Programm {@linkplain SVMLoaderShortTest#getKorrektesProgramm()} gespeichert wird ist {@value} */
	public static final String PRG_DATEI = "src/test/resources/com/github/mbeier1406/svm/prg/program.prg";

	/** Das zu testende Objekt */
	public SVMSource<Short> svmSource = new SVMSourceShort();


	/** Speichert ein vorgegebenes programm, liest es wieder ein und prüft es */
	@Test
	public void testeSchreibenUndLesen() throws SVMException {
		var korrektesProgramm = SVMLoaderShortTest.getKorrektesProgramm();
		LOGGER.trace("Speichere {}...", PRG_DATEI);
		svmSource.save(korrektesProgramm, PRG_DATEI);
		var svmProgram = svmSource.load(PRG_DATEI);
		svmProgram.validate();
		assertThat(korrektesProgramm.getDataList(), equalTo(svmProgram.getDataList()));
		assertThat(korrektesProgramm.getInstructionList(), equalTo(svmProgram.getInstructionList()));
	}

	/** Liest ein bekanntes Programm ein und prüft die Struktur */
	@Test
	public void testeLesen() throws SVMException {
		var svmProgram = svmSource.load("src/test/resources/com/github/mbeier1406/svm/prg/example.prg");
		svmProgram.validate();
		assertThat(svmProgram.getDataList().size(), equalTo(2));
		Stream.of("text1", "text2").forEach(label -> {
			assertThat(svmProgram.getDataList().stream().filter(data -> data.label().label().equals(label)).count(), equalTo(1L));
		});
		assertThat(svmProgram.getInstructionList().get(0).instruction().instruction(), equalTo(INSTRUCTIONS.get(Nop.CODE)));
		for ( int i=1; i < 5; i++ )
			assertThat(svmProgram.getInstructionList().get(i).instruction().instruction(), equalTo(INSTRUCTIONS.get(Mov.CODE)));
		assertThat(svmProgram.getInstructionList().get(5).instruction().instruction(), equalTo(INSTRUCTIONS.get(Int.CODE)));
		for ( int i=6; i < 8; i++ )
			assertThat(svmProgram.getInstructionList().get(i).instruction().instruction(), equalTo(INSTRUCTIONS.get(Mov.CODE)));
		assertThat(svmProgram.getInstructionList().get(8).instruction().instruction(), equalTo(INSTRUCTIONS.get(Int.CODE)));
	}

}
