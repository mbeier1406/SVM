package com.github.mbeier1406.svm.cmd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.impl.ALUShort;
import com.github.mbeier1406.svm.impl.MEMShort;
import com.github.mbeier1406.svm.instructions.InstructionFactory;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.prg.SVMProgramShort;
import com.github.mbeier1406.svm.syscalls.SyscallFactory;

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
		assertThat(programm.exec(new Scanner(""), null, null), equalTo(Programm.KEINE_OPTION_ANGEGEBEN));
	}

	/** Rückmeldung, falls eine unbekannte Option zum Kommando angegeben wurde */
	@Test
	public void testeUnbekannteOptionAngegeben() {
		assertThat(programm.exec(new Scanner("xyz"), null, null), equalTo("Unbekannte Option: xyz"));
	}

	/** Rückmeldung, wenn {@linkplain Programm#CMD_LEXER} ohne Parameter aufgerufen wurde */
	@Test
	public void testeSvmLexerOhneParameter() {
		assertThat(programm.exec(new Scanner(Programm.CMD_LEXER+" "), null, null), equalTo(Programm.KEIN_PROGRAMM_ANGEGEBEN));
	}

	/** Rückmeldung, wenn {@value #SVM} gescannt wurde */
	@Test
	public void testeLexerScannen() {
		assertThat(programm.exec(new Scanner(Programm.CMD_LEXER+" "+SVM), null, null), startsWith("Ok "+SVM+": 16 Symbole."));
	}

	/** Rückmeldung, wenn {@linkplain Programm#CMD_PARSE} ohne Parameter aufgerufen wurde */
	@Test
	public void testeSvmLadenOhneParameter() {
		assertThat(programm.exec(new Scanner(Programm.CMD_PARSE+" "), null, null), equalTo(Programm.KEIN_PROGRAMM_ANGEGEBEN));
	}

	/** Rückmeldung, wenn {@value #SVM} geladen wurde */
	@Test
	public void testeSvmLaden() {
		assertThat(programm.exec(new Scanner(Programm.CMD_PARSE+" "+SVM), null, svmProgramm), equalTo("Ok "+SVM));
		assertThat(svmProgramm.getDataList().size(), equalTo(2));
		assertThat(svmProgramm.getInstructionList().size(), equalTo(10));
	}

	/** Rückmeldung, wenn {@value #SVM} mit debugging Option geladen wurde */
	@Test
	public void testeSvmLadenMitDebug() {
		assertThat(programm.exec(new Scanner(Programm.CMD_PARSE+" "+SVM+" "+Programm.CMD_DEBUG), null, svmProgramm), equalTo("Ok "+SVM+ " (debugging)"));
		assertThat(svmProgramm.getDataList().stream().map(Data::lineInfo).count(), equalTo(2L));
		assertThat(svmProgramm.getInstructionList().stream().map(VirtualInstruction::lineInfo).count(), equalTo(10L));
	}

	/** Rückmeldung, wenn {@linkplain Programm#CMD_LADE_INTERN} ohne Parameter aufgerufen wurde */
	@Test
	public void testePrgLadenOhneParameter() {
		assertThat(programm.exec(new Scanner(Programm.CMD_LADE_INTERN+" "), null, null), equalTo(Programm.KEIN_PROGRAMM_ANGEGEBEN));
	}

	/** Rückmeldung, wenn {@value #PRG} geladen wurde */
	@Test
	public void testePrgLaden() {
		assertThat(programm.exec(new Scanner(Programm.CMD_LADE_INTERN+" "+PRG), null, svmProgramm), equalTo("Ok "+PRG));
		assertThat(svmProgramm.getDataList().size(), equalTo(2));
		assertThat(svmProgramm.getInstructionList().size(), equalTo(9));
	}

	/** Rückmeldung, wenn {@linkplain Programm#CMD_SPEICHER_EXTERN} ohne Parameter aufgerufen wurde */
	@Test
	public void testePrgSpeichernOhneParameter() {
		assertThat(programm.exec(new Scanner(Programm.CMD_SPEICHER_EXTERN+" "), null, null), equalTo(Programm.KEIN_PROGRAMM_ANGEGEBEN));
	}

	/** Rückmeldung, wenn {@linkplain Programm#CMD_SPEICHER_EXTERN} mit ungültigem Pfad aufgerufen wurde */
	@Test
	public void testePrgSpeichernUngueltigerPfad() {
		programm.exec(new Scanner(Programm.CMD_LADE_INTERN+" "+PRG), null, svmProgramm);
		assertThat(programm.exec(new Scanner(Programm.CMD_SPEICHER_EXTERN+" /a/b/c/x.prg"), null, svmProgramm), startsWith("Fehler (prg=/a/b/c/x.prg)"));
	}

	/** Rückmeldung, wenn mit {@linkplain Programm#CMD_SPEICHER_EXTERN} gespeichert wurde */
	@Test
	public void testePrgSpeichern() throws IOException {
		programm.exec(new Scanner(Programm.CMD_LADE_INTERN+" "+PRG), null, svmProgramm);
		assertThat(programm.exec(new Scanner(Programm.CMD_SPEICHER_EXTERN+" /tmp/x.prg"), null, svmProgramm), equalTo("Ok /tmp/x.prg"));
		assertThat(Files.readAllBytes(new File(PRG).toPath()), equalTo(Files.readAllBytes(new File("/tmp/x.prg").toPath())));
	}

	/** Rückmeldung, wenn validiert wird und kein {@value #PRG} geladen wurde */
	@Test
	public void testeValidierenMitFehler() {
		assertThat(programm.exec(new Scanner(Programm.CMD_VALIDIEREN), null, svmProgramm), equalTo("Fehler: [Instr] Leeres Programm: mindestens eine Instruktion wird erwartet!"));
	}

	/** Rückmeldung, wenn validiert wird und {@value #PRG} geladen wurde */
	@Test
	public void testeValidierenOhneFehler() {
		programm.exec(new Scanner(Programm.CMD_LADE_INTERN+" "+PRG), null, svmProgramm);
		assertThat(programm.exec(new Scanner(Programm.CMD_VALIDIEREN), null, svmProgramm), equalTo("OK"));
	}

	/** Rückmeldung, wenn {@value #PRG} geladen und danach gelöscht wurde */
	@Test
	public void testePrgLadenUndLoeschen() {
		assertThat(programm.exec(new Scanner(Programm.CMD_LADE_INTERN+" "+PRG), null, svmProgramm), equalTo("Ok "+PRG));
		assertThat(svmProgramm.getDataList().size(), equalTo(2));
		assertThat(svmProgramm.getInstructionList().size(), equalTo(9));
		assertThat(programm.exec(new Scanner(Programm.CMD_LOESCHEN), null, svmProgramm), equalTo("OK"));
		assertThat(svmProgramm.getDataList().size(), equalTo(0));
		assertThat(svmProgramm.getInstructionList().size(), equalTo(0));
	}

	/** Testet das Laden und Ausführen eines Programms */
	@Test
	public void testeStarten() {
		MEMShort mem = new MEMShort();
		ALUShort alu = new ALUShort(mem);
		alu.init();
		InstructionFactory.init(alu, mem);
		SyscallFactory.init(alu, mem);
		programm.exec(new Scanner(Programm.CMD_LADE_INTERN+" "+PRG), null, svmProgramm);
		programm.exec(new Scanner(Programm.CMD_VALIDIEREN), null, svmProgramm);
		programm.exec(new Scanner(Programm.CMD_LADE_SPEICHER), alu, svmProgramm);
		assertThat(programm.exec(new Scanner(Programm.CMD_STARTEN), alu, svmProgramm), equalTo("OK: 1"));
	}

	/** Testet das Parsen, Validieren, Laden und Ausführen eines Programms */
	@Test
	public void testeAusfuehren() {
		MEMShort mem = new MEMShort();
		ALUShort alu = new ALUShort(mem);
		alu.init();
		InstructionFactory.init(alu, mem);
		SyscallFactory.init(alu, mem);
		assertThat(programm.exec(new Scanner(Programm.CMD_AUSFUEHREN+" "+SVM), alu, svmProgramm), equalTo("OK: 55"));
	}

}
