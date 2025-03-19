package com.github.mbeier1406.svm.cmd;

import java.util.Scanner;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.prg.SVMProgram;

/**
 * Gibt Informationen zum Zustander der {@linkplain SVM}, der {@linkplain ALU} und des {@linkplain MEM Speichers} aus.
 */
@Help(
	shortHelp="Zustand von SVM, ALU und MEM ausgeben",
	longHelp=Information.USAGE
)
@Command(command="information", aliases={"i", "info"})
public class Information extends CommandBase implements CommandInterface {

	public static final String USAGE = "information [alu|mem|addr <addr> <len>] gibt Info zu ALU, Speicher bzw. einem Speicherbereich";

	/** {@inheritDoc} */
	@Override
	public <T> String exec(final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		String usageAddr = "addr <Adresse> <Länge>";
		String option = scanner.hasNext() ? scanner.next() : "";
		return switch ( option ) {
			case "" -> USAGE;
			case "alu" -> alu.toString();
			case "mem" -> alu.getMEM().toString();
			case "addr" -> {
				try {
					if ( !scanner.hasNext() ) throw new Exception("Adresse erwartet: "+usageAddr);
					int addr = Integer.parseInt(scanner.next());
					if ( !scanner.hasNext() ) throw new Exception("Länge erwartet: "+usageAddr);
					int len = Integer.parseInt(scanner.next());
					yield alu.getMEM().getBinaryContentStringAt(addr, len);
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
