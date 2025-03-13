package com.github.mbeier1406.svm.cmd;

import java.util.Scanner;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.prg.parser.SVMParserShort;

/**
 * Lädt ein SVM-Programm in externer in die interne
 * Darstellung ({@linkplain SVMProgram}. Beispiel:
 * {@code /SVM/src/test/resources/com/github/mbeier1406/svm/prg/example.svm}.
 */
@Help(shortHelp="Lädt ein SVM-Programm in die internen Strukturen", longHelp="lade <SVM-Programm>\n\tmit 'validiere' prüfen")
@Command(command="programm", aliases={"prog", "prg"})
public class Programm extends CommandBase implements CommandInterface {

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public <T> String exec(final Scanner scanner, final SVMProgram<T> svmProgram) {
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

}
