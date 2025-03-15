package com.github.mbeier1406.svm.cmd;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgramShort;

/**
 * Tests für die Klasse {@linkplain Programm}.
 */
public class ProgrammTest {

	/** externe Repräsentation des Programms, mit dem getestet wird */
	public static final String SVM = "src/test/resources/com/github/mbeier1406/svm/prg/example.svm";

	/** Interne Repräsentation des Programms, mit dem getestet wird */
	public static final String PRG = "src/test/resources/com/github/mbeier1406/svm/prg/example.prg";

	/** Das zu testende Objekt */
	public Programm programm = new Programm();

	/** Die interne Repräsentation des Programms, mit dem gearbeitet wird */
	public SVMProgram<Short> svmProgramm = new SVMProgramShort();

	/** Rückmeldung, falls keine Option zum Kommando angegeben wurde */
	@Test
	public void testeKeineOptionAngegeben() {
		assertThat(programm.exec(new Scanner(""), null), equalTo(Programm.KEINE_OPTION_ANGEGEBEN));
	}

	/** Rückmeldung, falls eine unbekannte Option zum Kommando angegeben wurde */
	@Test
	public void testeUnbekannteOptionAngegeben() {
		assertThat(programm.exec(new Scanner("xyz"), null), equalTo("Unbekannte Option: xyz"));
	}

	/** Rückmeldung, wenn {@linkplain Programm#CMD_LADE_INTERN} ohne Parameter aufgerufen wurde */
	@Test
	public void testePrgLadenOhneParameter() {
		assertThat(programm.exec(new Scanner(Programm.CMD_LADE_INTERN+" "), null), equalTo(Programm.KEIN_PROGRAMM_ANGEGEBEN));
	}

	/** Rückmeldung, wenn {@value #PRG} geladen wurde */
	@Test
	public void testePrgLaden() {
		assertThat(programm.exec(new Scanner(Programm.CMD_LADE_INTERN+" "+PRG), svmProgramm), equalTo("Ok "+PRG));
		assertThat(svmProgramm.getDataList().size(), equalTo(2));
		assertThat(svmProgramm.getInstructionList().size(), equalTo(9));
	}

	/** Rückmeldung, wenn {@value #SVM} geladen wurde */
	@Test
	public void testeSvmLaden() {
		assertThat(programm.exec(new Scanner(Programm.CMD_PARSE+" "+SVM), svmProgramm), equalTo("Ok "+SVM));
		assertThat(svmProgramm.getDataList().size(), equalTo(2));
		assertThat(svmProgramm.getInstructionList().size(), equalTo(10));
	}

	/** Rückmeldung, wenn validiert wird und kein {@value #PRG} geladen wurde */
	@Test
	public void testeValidierenMitFehler() {
		assertThat(programm.exec(new Scanner(Programm.CMD_VALIDIEREN), svmProgramm), equalTo("Fehler: [Instr] Leeres Programm: mindestens eine Instruktion wird erwartet!"));
	}

	/** Rückmeldung, wenn validiert wird und {@value #PRG} geladen wurde */
	@Test
	public void testeValidierenOhneFehler() {
		programm.exec(new Scanner(Programm.CMD_LADE_INTERN+" "+PRG), svmProgramm);
		assertThat(programm.exec(new Scanner(Programm.CMD_VALIDIEREN), svmProgramm), equalTo("OK"));
	}

}
