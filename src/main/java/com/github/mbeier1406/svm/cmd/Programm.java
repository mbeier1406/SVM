package com.github.mbeier1406.svm.cmd;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.prg.SVMSourceShort;
import com.github.mbeier1406.svm.prg.parser.SVMParserShort;

/**
 * Lädt ein SVM-Programm in externer in die interne
 * Darstellung ({@linkplain SVMProgram}. Beispiel:
 * {@code /SVM/src/test/resources/com/github/mbeier1406/svm/prg/example.svm}.
 */
@Help(shortHelp="Lädt, validiert, führt aus, ... ein SVM-Programm", longHelp=Programm.HILFE)
@Command(command="programm", aliases={"prog", "prg"})
public class Programm extends CommandBase implements CommandInterface {

	/** Option zum Laden eines PRG-Programms (interne Darstellung) in die internen Strukturen {@linkplain SVMProgram} */
	public static final String CMD_LADE_INTERN = "lade-intern";

	/** Option zum Laden eines SVM-Programms (externe Darstellung) in die internen Strukturen {@linkplain SVMProgram} */
	public static final String CMD_PARSE = "parse";

	/** Option zum Validieren eines Programms in interner Struktur (zuvor geladen mit {@linkplain #CMD_LADE_INTERN} oder {@linkplain #CMD_PARSE} */
	public static final String CMD_VALIDIEREN = "validieren";

	/** Hilfe zur Benutzung des Kommandos */
	public static final String HILFE = "programm ("+CMD_PARSE+" <SVM-Programm>"+CMD_LADE_INTERN+" <PRG-Programm>|"+CMD_VALIDIEREN+"|lade-speicher|loeschen|starten)\n"
			+ "\t"+CMD_LADE_INTERN+" - lädt das angegebene Programm in interner Darstellung (PRG) in die internen Strukturen\n"
			+ "\t"+CMD_PARSE+" - lädt das angegebene Programm in externer Darstellung (SVM) in die internen Strukturen\n"
			+ "\t"+CMD_VALIDIEREN+" - prüft die internen Strukturen nach dem Laden\n"
			+ "\tlade-speicher - lädt die internen Strukturen in den Speicher der SVM\n"
			+ "\tstarten - startet das in den Speicher der SVM geladene Programm";

	/** Alle Optionen zu diesem Kommando müssen dieses Interface implementieren */
	@FunctionalInterface
	public static interface Ausfuehrung {
		public <T> String ausfuehren(String option, final Scanner scanner, final SVMProgram<T> svmProgram);
	}

	/** Diese Map pflegt die Zuordnung einer Option zum Kommando zur Ausführungslogik */
	private final Map<String, Ausfuehrung> PRG_MAP = new HashMap<String, Ausfuehrung>();

	/** Initialisierung {@linkplain #PRG_MAP} */
	public Programm() {
		PRG_MAP.put(CMD_LADE_INTERN, this::ladeIntern);
		PRG_MAP.put(CMD_PARSE, this::parse);
		PRG_MAP.put(CMD_VALIDIEREN, this::validieren);
	}

	/** Option {@linkplain #CMD_LADE_INTERN}: ein PRG-Programm in die interne Datenstruktur laden */
	@SuppressWarnings("unchecked")
	private <T> String ladeIntern(String option, final Scanner scanner, final SVMProgram<T> svmProgram) {
		String prgprg = null;
		try {
			prgprg  = scanner.next();
			var prg = new SVMSourceShort().load(prgprg);
			for ( var data : prg.getDataList() )
				svmProgram.addData((Data<T>) data);
			for ( var instr : prg.getInstructionList() )
				svmProgram.addInstruction((VirtualInstruction<T>) instr);
			return "Ok " + prgprg;
		} catch (SVMException e) {
			return "Fehler (prg="+prgprg+"): " + e.getLocalizedMessage();
		}
	}

	/** Option {@linkplain #CMD_PARSE}: ein SVM-Programm in die interne Datenstruktur laden */
	@SuppressWarnings("unchecked")
	private <T> String parse(String option, final Scanner scanner, final SVMProgram<T> svmProgram) {
		String svmprg = null;
		try {
			svmprg  = scanner.next();
			var prg = new SVMParserShort().parse(svmprg);
			for ( var data : prg.getDataList() )
				svmProgram.addData((Data<T>) data);
			for ( var instr : prg.getInstructionList() )
				svmProgram.addInstruction((VirtualInstruction<T>) instr);
			return "Ok " + svmprg;
		} catch (SVMException e) {
			return "Fehler (svmprg="+svmprg+"): " + e.getLocalizedMessage();
		}
	}

	/** Option {@linkplain #CMD_VALIDIEREN}: ein geladenes Programm (interne Darstellung) validieren */
	private <T> String validieren(String option, final Scanner scanner, final SVMProgram<T> svmProgram) {
		try {
			svmProgram.validate();
			return "OK";
		} catch (SVMException e) {
			return "Fehler: " + e.getLocalizedMessage();
		}
	}

	/** Falls eine unbekannte Option als Parameter zu 'programm' eingegben wird */
	private <T> String unbekannteOption(String option, final Scanner scanner, final SVMProgram<T> svmProgram) {
		return "Unbekannte Option: "+option;
	}

	/** {@inheritDoc} */
	@Override
	public <T> String exec(final Scanner scanner, final SVMProgram<T> svmProgram) {
		String option = scanner.next();
		if ( option == null ) return "Keine Option angegeben!";
		return Optional
				.ofNullable(PRG_MAP.get(option))
				.orElse(this::unbekannteOption)
				.ausfuehren(option, scanner, svmProgram);
	}

}
