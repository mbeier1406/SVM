package com.github.mbeier1406.svm.cmd;

import java.util.Scanner;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM.Instruction;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;

/**
 * Führt Operationen auf die {@linkplain ALU} aus, wie das Auslesen von Registern usw.
 */
@Help(
	shortHelp="Führt Operationen auf die ALU und das MEM aus",
	longHelp=Alu.USAGE
)
@Command(command="alu", aliases={})
public class Alu extends CommandBase implements CommandInterface {

	/** Parameter für die Initialisierung der {@linkplain ALU} ist {@value} */
	public static final String CMD_INIT = "init";

	/** Parameter für den Start der {@linkplain ALU} ist {@value} */
	public static final String CMD_START = "start";

	/** Parameter für den Stopp der {@linkplain ALU} ist {@value} */
	public static final String CMD_SET_STOP_FLAG = "set_stop_flag";

	/** Parameter für das Setzen eines Registers ist {@value} */
	public static final String CMD_SET_REG = "set_reg";

	/** Parameter für das Lesen eines Registers ist {@value} */
	public static final String CMD_READ_REG = "read_reg";

	/** Parameter für das Setzen eines Speicherwortes ist {@value} */
	public static final String CMD_SET_MEM = "set_mem";

	/** Parameter für das Lesen eines Speicherwortes ist {@value} */
	public static final String CMD_READ_MEM = "read_mem";

	/** Fehlermeldung falsche Nutzung {@linkplain #CMD_SET_REG} */
	public static final String USAGE_SET_REG = CMD_SET_REG+" <nr> <wert>";

	/** Fehlermeldung falsche Nutzung {@linkplain #CMD_READ_REG} */
	public static final String USAGE_READ_REG = CMD_READ_REG+" <nr>";

	/** Fehlermeldung falsche Nutzung {@linkplain #CMD_SET_MEM} */
	public static final String USAGE_SET_MEM = CMD_SET_MEM+" <addr> <wert>";

	/** Fehlermeldung falsche Nutzung {@linkplain #CMD_READ_MEM} */
	public static final String USAGE_READ_MEM = CMD_READ_MEM+" <addr>";


	/** Hilfe zur Nutzung des Kommandos */
	public static final String USAGE = "alu ["
			+ CMD_INIT + "|" + CMD_START + "|" + CMD_SET_STOP_FLAG + "|"
			+ USAGE_SET_REG + "|" + USAGE_READ_REG + "|"
			+ USAGE_SET_MEM + "|" + USAGE_READ_MEM
			+ "]";


	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public <T> String exec(final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		String option = scanner.hasNext() ? scanner.next() : "";
		return switch ( option ) {
			case "" -> USAGE;
			case CMD_INIT -> { alu.init(); yield "OK"; }
			case CMD_START -> {
				try {
					alu.start();
					yield "OK";
				} catch (SVMException e) {
					yield "Fehler: "+e.getLocalizedMessage();
				}
			}
			case CMD_SET_STOP_FLAG -> { ((ALU.Instruction<T>) alu).setStopFlag(); yield "OK"; }
			case CMD_SET_REG -> {
				try {
					if ( !scanner.hasNext() ) throw new Exception("Register erwartet: "+USAGE_SET_REG);
					int reg = Integer.parseInt(scanner.next());
					if ( !scanner.hasNext() ) throw new Exception("Wert erwartet: "+USAGE_SET_REG);
					int wert = Integer.parseInt(scanner.next());
					alu.getInstructionInterface().setRegisterValue(reg, (T) Short.valueOf((short) wert));
					yield "OK";
				}
				catch ( NumberFormatException e ) {
					yield "Ungültige Zahl: "+e.getLocalizedMessage();
				}
				catch ( Exception e ) {
					yield e.getLocalizedMessage();
				}
			}
			case CMD_READ_REG -> {
				try {
					if ( !scanner.hasNext() ) throw new Exception("Register erwartet: "+USAGE_READ_REG);
					int reg = Integer.parseInt(scanner.next());
					T wert = alu.getInstructionInterface().getRegisterValue(reg);
					yield "OK: "+wert;
				}
				catch ( NumberFormatException e ) {
					yield "Ungültige Zahl: "+e.getLocalizedMessage();
				}
				catch ( Exception e ) {
					yield e.getLocalizedMessage();
				}
			}
			case CMD_SET_MEM -> {
				try {
					if ( !scanner.hasNext() ) throw new Exception("Speicheradresse erwartet: "+USAGE_SET_MEM);
					int addr = Integer.parseInt(scanner.next());
					if ( !scanner.hasNext() ) throw new Exception("Wert erwartet: "+USAGE_SET_MEM);
					int wert = Integer.parseInt(scanner.next());
					((Instruction<Short>) alu.getMEM().getInstructionInterface()).write(addr, (short) wert);
					yield "OK";
				}
				catch ( NumberFormatException e ) {
					yield "Ungültige Zahl: "+e.getLocalizedMessage();
				}
				catch ( Exception e ) {
					yield e.getLocalizedMessage();
				}
			}
			case CMD_READ_MEM -> {
				try {
					if ( !scanner.hasNext() ) throw new Exception("Speicheradresse erwartet: "+USAGE_READ_MEM);
					int addr = Integer.parseInt(scanner.next());
					T wert = ((Instruction<T>) alu.getMEM().getInstructionInterface()).read(addr);
					yield "OK: "+wert;
				}
				catch ( NumberFormatException e ) {
					yield "Ungültige Zahl: "+e.getLocalizedMessage();
				}
				catch ( Exception e ) {
					yield e.getLocalizedMessage();
				}
			}
			default -> "Unbekannt: "+option;
		};
	}

}
