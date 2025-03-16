package com.github.mbeier1406.svm.cmd;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain Ende}.
 */
public class EndeTest {

	public static final Logger LOGGER = LogManager.getLogger(EndeTest.class);

	/** Das zu testende Objekt */
	public Ende ende = new Ende();

	/** Prüft, ob das Ende-Signal bei Ausführung geliefert wird */
	@Test
	public void testeExec() {
		String erg = ende.exec(new Scanner(""), null, null);
		LOGGER.info("erg={}", erg);
		assertTrue(erg.equals(Ende.ENDE));
	}

}
