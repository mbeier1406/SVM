package com.github.mbeier1406.svm.cmd;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.github.mbeier1406.svm.GenericFactory;

public class CommandFactory {

	/** Das Java-Package, in dem sich alle {@linkplain Command}s befinden ist {@value} */
	public static final String PACKAGE = CommandFactory.class.getPackageName();

	/**
	 * Diese Map enthält alle {@linkplain CommandInterface SVM-Kommands} mit deren Eingabe als Key.
	 */
	private static final Map<String, CommandInterface> COMMANDS;

	/**
	 * Diese Map enthält alle {@linkplain CommandInterface SVM-Kommands} mit deren Aliase als Eingabe als Key.
	 */
	private static final Map<String, CommandInterface> ALIASE;

	/** Lädt die definierten {@linkplain Command}s */
	static {
		COMMANDS = getCommandList();
		ALIASE = getAliaseList();
	}

	/** Lädt alle Systemaufrufe über das Kommando für {@linkplain CommandInterface} */
	public static Map<String, CommandInterface> getCommandList() {
		return new GenericFactory<String, CommandInterface>().getItems(CommandFactory.PACKAGE, Command.class, "command");
	}

	/** Lädt alle Systemaufrufe über den Alias für {@linkplain CommandInterface} */
	public static Map<String, CommandInterface> getAliaseList() {
		Map<String, CommandInterface> a = new HashMap<>();
		for ( Map.Entry<String, CommandInterface> item : COMMANDS.entrySet() )
			for ( String alias : item.getValue().getClass().getAnnotation(Command.class).aliases() )
				a.put(alias, item.getValue());
		return a;
	}

	/** Liefert eine Kopie der Map Kommando -> {@linkplain CommandInterface} */
	public static Map<String, CommandInterface> getCommands() {
		return Collections.unmodifiableMap(CommandFactory.COMMANDS);
	}

	/** Liefert eine Kopie der Map Alias -> {@linkplain CommandInterface} */
	public static Map<String, CommandInterface> getAliase() {
		return Collections.unmodifiableMap(CommandFactory.ALIASE);
	}

}
