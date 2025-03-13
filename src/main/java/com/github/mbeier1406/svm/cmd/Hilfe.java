package com.github.mbeier1406.svm.cmd;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.github.mbeier1406.svm.prg.SVMProgram;

/**
 * Gibt eine Hilfe zu allen verfügbaren {@linkplain Command SVM-Kommandos} aus.
 */
@Help(
	shortHelp="Bietet Hilfe zur Nutzung der SVM-Cli Kommandos an",
	longHelp="hilfe [Kommando] gibt Hilfestellung für ein spezielles Kommando"
)
@Command(command="hilfe", aliases={"h"})
public class Hilfe extends CommandBase implements CommandInterface {

	/** {@inheritDoc} */
	@Override
	public <T> String exec(final Scanner scanner, final SVMProgram<T> svmProgram) {
		final StringBuffer s = new StringBuffer("");
		if ( !scanner.hasNext() )
			s.append("Folgende Kommandos sind verfügbar:\n");
		final StringBuffer sb = new StringBuffer("");
		try {
			sb.append(scanner.next());
		}
		catch ( NoSuchElementException e) {
		}
		final String commando = sb.toString();
		CommandFactory.getCommands().entrySet().stream().forEach(cmd -> {
			if ( (!commando.isBlank() && commando.equals(cmd.getKey())) || commando.isBlank() ) {
				if ( !s.isEmpty() ) s.append("\n");
				s.append(cmd.getKey());
				s.append(" - ");
				s.append(cmd.getValue().getClass().getAnnotation(Help.class).shortHelp());
				if ( !commando.isBlank() ) {
					s.append("\n\t");
					s.append("Aliase: "+Arrays.toString(cmd.getValue().getClass().getAnnotation(Command.class).aliases()));
					s.append("\n\t");
					s.append(cmd.getValue().getClass().getAnnotation(Help.class).longHelp());
				}
			}
		});
		if ( s.isEmpty() ) s.append("Unbekannt: "+commando);
		return s.toString();
	}

}
