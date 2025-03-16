package com.github.mbeier1406.svm.cmd;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain Hilfe}.
 */
public class HilfeTest {

	public static final Logger LOGGER = LogManager.getLogger(HilfeTest.class);

	/** Das zu testende Objekt */
	public Hilfe hilfe = new Hilfe();

	/** Test die Standardausgabe des Hilfe-Kommandos */
	@Test
	public void testeShortHelp() {
		String usage = hilfe.exec(new Scanner(""), null, null);
		LOGGER.info("usage={}", usage);
		assertTrue(usage.length() > 0);
	}

	/** Test die Ausgabe des Hilfe-Kommandos für verschiedene Kommandos */
	@Test
	public void testeHilfe() {
		CommandFactory.getCommands().keySet().stream().forEach(cmd -> {
			String usage = hilfe.exec(new Scanner(" "+cmd), null, null);
			LOGGER.info("cmd={}; usage={}", cmd, usage);
			assertTrue(usage.length() > 0);
		});
	}

}
