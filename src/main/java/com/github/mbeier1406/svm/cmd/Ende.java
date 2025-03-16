package com.github.mbeier1406.svm.cmd;

import java.util.Scanner;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.prg.SVMProgram;

/**
 * Beendet die {@linkplain SVMCliImpl}.
 */
@Help(shortHelp="Beendet die SVM-Cli", longHelp="")
@Command(command="ende", aliases={"e"})
public class Ende extends CommandBase implements CommandInterface {

	/** Signalisiert der {@linkplain SVMCli} das Ende der Eingaben */
	public static final String ENDE = "Ende";

	/** {@inheritDoc} */
	@Override
	public <T> String exec(final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		return ENDE;
	}

}
