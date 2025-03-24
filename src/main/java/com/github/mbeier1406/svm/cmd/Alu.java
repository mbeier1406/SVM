package com.github.mbeier1406.svm.cmd;

import java.util.Scanner;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM.Instruction;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;

/**
 * Führt Operationen auf die {@linkplain ALU} aus, wie das Auslesen von registern usw.
 */
@Help(
	shortHelp="Führt Operationen auf die ALU und das MEM aus",
	longHelp=Alu.USAGE
)
@Command(command="alu", aliases={})
public class Alu extends CommandBase implements CommandInterface {

	public static final String USAGE = "alu [init|start|set_stop_flag|set_reg <nr> <wert>|read_reg <nr>|set_mem <addr> <wert>|read_mem <addr>]";

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public <T> String exec(final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		String usageSetReg = "set_reg <nr> <wert>";
		String usageReadReg = "read_reg <nr>";
		String usageSetMem = "set_mem <addr> <wert>";
		String usageReadMem = "read_mem <addr>";
		String option = scanner.hasNext() ? scanner.next() : "";
		return switch ( option ) {
			case "" -> USAGE;
			case "init" -> { alu.init(); yield "OK"; }
			case "start" -> {
				try {
					alu.start();
					yield "OK";
				} catch (SVMException e) {
					yield "Fehler: "+e.getLocalizedMessage();
				}
			}
			case "set_stop_flag" -> { ((ALU.Instruction<T>) alu).setStopFlag(); yield "OK"; }
			case "set_reg" -> {
				try {
					if ( !scanner.hasNext() ) throw new Exception("Register erwartet: "+usageSetReg);
					int reg = Integer.parseInt(scanner.next());
					if ( !scanner.hasNext() ) throw new Exception("Wert erwartet: "+usageSetReg);
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
			case "get_reg" -> {
				try {
					if ( !scanner.hasNext() ) throw new Exception("Register erwartet: "+usageReadReg);
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
			case "set_mem" -> {
				try {
					if ( !scanner.hasNext() ) throw new Exception("Speicheradresse erwartet: "+usageSetMem);
					int addr = Integer.parseInt(scanner.next());
					if ( !scanner.hasNext() ) throw new Exception("Wert erwartet: "+usageSetMem);
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
			case "read_mem" -> {
				try {
					if ( !scanner.hasNext() ) throw new Exception("Register erwartet: "+usageReadMem);
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
