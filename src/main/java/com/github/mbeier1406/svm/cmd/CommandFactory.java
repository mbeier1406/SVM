package com.github.mbeier1406.svm.cmd;

import java.util.Map;

import com.github.mbeier1406.svm.GenericFactory;

public class CommandFactory {

	/** Das Java-Package, in dem sich alle {@linkplain Command}s befinden ist {@value} */
	public static final String PACKAGE = CommandFactory.class.getPackageName();

	/**
	 * Diese Map enthält alle {@linkplain CommandInterface SVM-Kommands} mit deren Eingabe als key.
	 */
	public static final Map<String, CommandInterface> COMMANDS;

	/** Lädt die definierten {@linkplain Command}s */
	static {
		COMMANDS = getCommands();
	}

	/** Lädt alle Systemaufrufe für {@linkplain com.github.mbeier1406.Int.instructions.Syscall} */
	public static Map<String, CommandInterface> getCommands() {
		return new GenericFactory<String, CommandInterface>().getItems(CommandFactory.PACKAGE, Command.class, "command");
	}

}
