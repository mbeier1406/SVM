package com.github.mbeier1406.svm.cmd;

import java.util.Scanner;

/**
 * Beendet die {@linkplain SVMCliImpl}.
 */
@Help(shortHelp="Beendet die SVM-Cli", longHelp="")
@Command(command="ende")
public class Ende extends CommandBase implements CommandInterface {

	/** Signalisiert der {@linkplain SVMCli} das Ende der Eingaben */
	public static final String ENDE = "Ende";

	/** {@inheritDoc} */
	@Override
	public String exec(final Scanner scanner) {
		return ENDE;
	}

}
