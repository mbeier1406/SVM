package com.github.mbeier1406.svm.cmd;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMLoaderShort;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.prg.SVMSourceShort;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexerImpl;
import com.github.mbeier1406.svm.prg.parser.SVMParser;
import com.github.mbeier1406.svm.prg.parser.SVMParserShort;

/**
 * Alle programmbezogenen Funktionen wie lexen, parsen, einlesen, speichern, validieren und ausführen von Programmen.
 * Beispiele für die Darstellung eines {@linkplain SVMProgram}s
 * <ul>
 * <li>Extern: {@code /SVM/src/test/resources/com/github/mbeier1406/svm/prg/example.svm}</li>
 * <li>Intern: {@code /SVM/src/test/resources/com/github/mbeier1406/svm/prg/example.prg} (nach {@linkplain SVMParser#parse(File)})</li>
 * </ul>
 */
@Help(shortHelp="Lädt, validiert, führt aus, ... ein SVM-Programm", longHelp=Programm.HILFE)
@Command(command="programm", aliases={"prog", "prg"})
public class Programm extends CommandBase implements CommandInterface {

	/** Wenn keine Option zum Kommando angegeben wurde */
	public static final String KEINE_OPTION_ANGEGEBEN = "Keine Option angegeben!";

	/** Wenn keine Programm angegeben wurde */
	public static final String KEIN_PROGRAMM_ANGEGEBEN = "Kein Programm angegeben!";

	/** Option zur lexikalischen Analyse eines SVM-Programms */
	public static final String CMD_LEXER = "lexer";

	/** Option zum Laden eines SVM-Programms (externe Darstellung) in die internen Strukturen {@linkplain SVMProgram} */
	public static final String CMD_PARSE = "parse";

	/** Option zum Laden eines PRG-Programms (interne Darstellung) in die internen Strukturen {@linkplain SVMProgram} */
	public static final String CMD_LADE_INTERN = "lade-intern";

	/** Option zum Speichern eines Programms in interner Struktur (zuvor geladen mit {@linkplain #CMD_LADE_INTERN} oder {@linkplain #CMD_PARSE} als Datei */
	public static final String CMD_SPEICHER_EXTERN = "speicher-extern";

	/** Option zum Validieren eines Programms in interner Struktur (zuvor geladen mit {@linkplain #CMD_LADE_INTERN} oder {@linkplain #CMD_PARSE} */
	public static final String CMD_VALIDIEREN = "validieren";

	/** Option zum Löschen eines zuvor mit {@value #CMD_PARSE} oder {@value #CMD_LADE_INTERN} geladenen Programms */
	public static final String CMD_LOESCHEN = "loeschen";

	/** Option zum Laden eines zuvor mit {@value #CMD_PARSE} oder {@value #CMD_LADE_INTERN} geladenen Programms zur Ausführung in den Speicher {@linkplain MEM} */
	public static final String CMD_LADE_SPEICHER = "lade-speicher";

	/** Option zum Ausführen eines zuvor mit {@value #CMD_LADE_SPEICHER} geladenen Programms */
	public static final String CMD_STARTEN = "starten";

	/** Option zum Ausführen eines Programmes über {@value #CMD_PARSE}, {@linkplain #CMD_VALIDIEREN}, {@linkplain #CMD_LADE_SPEICHER} und {@linkplain #CMD_STARTEN} */
	public static final String CMD_AUSFUEHREN = "ausfuehren";

	/** Hilfe zur Benutzung des Kommandos */
	public static final String HILFE = "programm ("
				+ CMD_LEXER + " <SVM-Programm>|"
				+ CMD_PARSE + " <SVM-Programm>|"
				+ CMD_LADE_INTERN + " <PRG-Programm>|"
				+ CMD_SPEICHER_EXTERN + " <PRG-Programm>|"
				+ CMD_VALIDIEREN + "|"
				+ CMD_LOESCHEN + "|"
				+ CMD_LADE_SPEICHER + "|"
				+ CMD_STARTEN + "|"
				+ CMD_AUSFUEHREN + " <SVM-Programm>"
				+ ")\n"
			+ "\t"+CMD_LEXER+" - führt die lexikalische Analyse des angegebenen Programms in externer Darstellung (SVM) durch\n"
			+ "\t"+CMD_PARSE+" - lädt das angegebene Programm in externer Darstellung (SVM) in die internen Strukturen\n"
			+ "\t"+CMD_LADE_INTERN+" - lädt das angegebene Programm in interner Darstellung (PRG) in die internen Strukturen\n"
			+ "\t"+CMD_SPEICHER_EXTERN+" - speichert das geladene Programm in interner Darstellung als Datei (PRG)\n"
			+ "\t"+CMD_VALIDIEREN+" - prüft die internen Programm-Strukturen nach dem Laden\n"
			+ "\t"+CMD_LOESCHEN+" - löscht die internen Programm-Strukturen nach dem Laden\n"
			+ "\t"+CMD_LADE_SPEICHER+" - lädt die internen Strukturen in den Speicher der SVM\n"
			+ "\t"+CMD_STARTEN+" - startet das in den Speicher der SVM geladene Programm\n"
			+ "\t"+CMD_AUSFUEHREN+" - parst, validiert, lädt und startet das angegebene SVM-Programm\n\n"
			+ "\tBeispiel SVM-Programm: src/test/resources/com/github/mbeier1406/svm/prg/example.svm\n"
			+ "\t         PRG-Programm: src/test/resources/com/github/mbeier1406/svm/prg/example.prg\n";

	/** Alle Optionen zu diesem Kommando müssen dieses Interface implementieren */
	@FunctionalInterface
	public static interface Ausfuehrung {
		public <T> String ausfuehren(String option, final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram);
	}

	/** Diese Map pflegt die Zuordnung einer Option zum Kommando zur Ausführungslogik */
	private final Map<String, Ausfuehrung> PRG_MAP = new HashMap<String, Ausfuehrung>();

	/** Initialisierung {@linkplain #PRG_MAP} */
	public Programm() {
		PRG_MAP.put(CMD_LEXER, this::lexer);
		PRG_MAP.put(CMD_PARSE, this::parse);
		PRG_MAP.put(CMD_LADE_INTERN, this::ladeIntern);
		PRG_MAP.put(CMD_SPEICHER_EXTERN, this::speicherExtern);
		PRG_MAP.put(CMD_VALIDIEREN, this::validieren);
		PRG_MAP.put(CMD_LOESCHEN, this::loeschen);
		PRG_MAP.put(CMD_LADE_SPEICHER, this::ladeSpeicher);
		PRG_MAP.put(CMD_STARTEN, this::starten);
		PRG_MAP.put(CMD_AUSFUEHREN, this::ausfuehren);
	}

	/** Option {@linkplain #CMD_LEXER}: lexikalische Analyse für ein SVM-Programm */
	private <T> String lexer(String option, final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		String svmprg = null;
		try {
			if ( !scanner.hasNext() ) return KEIN_PROGRAMM_ANGEGEBEN;
			svmprg  = scanner.next();
			List<LineInfo> scan = new SVMLexerImpl().scan(new File(svmprg));
			return "Ok " + svmprg + ": "+scan.size() + " Symbole.\n" + scan.toString().replaceAll("LineInfo", "\n\tLineInfo").replaceAll("]$", "\n]");
		} catch (SVMException e) {
			return "Fehler (svmprg="+svmprg+"): " + e.getLocalizedMessage();
		}
	}

	/** Option {@linkplain #CMD_PARSE}: ein SVM-Programm in die interne Datenstruktur laden */
	@SuppressWarnings("unchecked")
	private <T> String parse(String option, final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		String svmprg = null;
		try {
			if ( !scanner.hasNext() ) return KEIN_PROGRAMM_ANGEGEBEN;
			svmprg  = scanner.next();
			boolean debugging =  scanner.hasNext() && scanner.next().equals("debug");
			var prg = new SVMParserShort().setDebugging(debugging).parse(svmprg);
			for ( var data : prg.getDataList() )
				svmProgram.addData((Data<T>) data);
			for ( var instr : prg.getInstructionList() )
				svmProgram.addInstruction((VirtualInstruction<T>) instr);
			return "Ok " + svmprg + (debugging?" (debugging)":"");
		} catch (SVMException e) {
			return "Fehler (svmprg="+svmprg+"): " + e.getLocalizedMessage();
		}
	}

	/** Option {@linkplain #CMD_LADE_INTERN}: ein PRG-Programm in die interne Datenstruktur laden */
	@SuppressWarnings("unchecked")
	private <T> String ladeIntern(String option, final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		String prgprg = null;
		try {
			if ( !scanner.hasNext() ) return KEIN_PROGRAMM_ANGEGEBEN;
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

	/** Option {@linkplain #CMD_SPEICHER_EXTERN}: ein PRG-Programm aus der interne Datenstruktur (geladen mit {@value #CMD_PARSE} oder {@value #CMD_LADE_INTERN}l in eine Datei speichern */
	@SuppressWarnings("unchecked")
	private <T> String speicherExtern(String option, final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		String prgprg = null;
		try {
			if ( !scanner.hasNext() ) return KEIN_PROGRAMM_ANGEGEBEN;
			prgprg  = scanner.next();
			new SVMSourceShort().save((SVMProgram<Short>) svmProgram, prgprg);
			return "Ok " + prgprg;
		} catch (Exception e) {
			return "Fehler (prg="+prgprg+"): " + e.getLocalizedMessage();
		}
	}

	/** Option {@linkplain #CMD_VALIDIEREN}: ein geladenes Programm (interne Darstellung) validieren */
	private <T> String validieren(String option, final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		try {
			svmProgram.validate();
			return "OK";
		} catch (SVMException e) {
			return "Fehler: " + e.getLocalizedMessage();
		}
	}

	/** Option {@linkplain #CMD_LOESCHEN}: ein geladenes Programm (interne Darstellung) wieder löschen */
	private <T> String loeschen(String option, final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		svmProgram.reset();
		return "OK";
	}

	/** Option {@linkplain #CMD_LADE_SPEICHER}: ein geladenes Programm (interne Darstellung) in den Hauptspeicher laden */
	@SuppressWarnings("unchecked")
	private <T> String ladeSpeicher(String option, final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		try {
			new SVMLoaderShort().load((MEM<Short>) alu.getMEM(), (SVMProgram<Short>) svmProgram);
			return "OK";
		} catch (SVMException e) {
			return "Fehler: " + e.getLocalizedMessage();
		}
	}

	/** Option {@linkplain #CMD_STARTEN}: ein geladenes Programm ({@linkplain #CMD_LADE_INTERN}) ausführen */
	private <T> String starten(String option, final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		try {
			int code = alu.start();
			return "OK: "+code;
		} catch (SVMException e) {
			return "Fehler: " + e.getLocalizedMessage();
		}
	}

	/** Option {@linkplain #CMD_AUSFUEHREN}: parst, validiert, lädt und startet ein SVM-Programm */
	private <T> String ausfuehren(String option, final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		try {
			loeschen(option, scanner, alu, svmProgram);
			parse(option, scanner, alu, svmProgram);
			validieren(option, scanner, alu, svmProgram);
			ladeSpeicher(option, scanner, alu, svmProgram);
			int code = alu.start();
			return "OK: "+code;
		} catch (SVMException e) {
			return "Fehler: " + e.getLocalizedMessage();
		}
	}

	/** Falls eine unbekannte Option als Parameter zu 'programm' eingegben wird */
	private <T> String unbekannteOption(String option, final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		return "Unbekannte Option: "+option;
	}

	/** {@inheritDoc} */
	@Override
	public <T> String exec(final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		if ( !scanner.hasNext() ) return KEINE_OPTION_ANGEGEBEN;
		String option = scanner.next();
		return Optional
				.ofNullable(PRG_MAP.get(option))
				.orElse(this::unbekannteOption)
				.ausfuehren(option, scanner, alu, svmProgram);
	}

}
