package com.github.mbeier1406.svm.cmd;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Gibt eine Hilfe zu allen verfügbaren {@linkplain Command SVM-Kommandos} aus.
 */
@Help(
	shortHelp="Bietet Hilfe zur Nutzung der SVM-Cli Kommandos an",
	longHelp="hilfe [Kommando] gibt Hilfestellung für ein spezielles Kommando"
)
@Command(command="hilfe")
public class Hilfe extends CommandBase implements CommandInterface {

	/** {@inheritDoc} */
	@Override
	public String exec(final Scanner scanner) {
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
		CommandFactory.COMMANDS.entrySet().stream().forEach(cmd -> {
			if ( (!commando.isBlank() && commando.equals(cmd.getKey())) || commando.isBlank() ) {
				s.append(cmd.getKey());
				s.append(" - ");
				s.append(cmd.getValue().getClass().getAnnotation(Help.class).shortHelp());
				s.append("\n");
				if ( !commando.isBlank() ) {
					s.append("\t");
					s.append(cmd.getValue().getClass().getAnnotation(Help.class).longHelp());
				}
			}
		});
		return s.toString();
	}

}
