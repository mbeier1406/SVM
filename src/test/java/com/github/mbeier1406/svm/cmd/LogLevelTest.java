package com.github.mbeier1406.svm.cmd;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain LogLevel}.
 */
public class LogLevelTest {

	/** Das zu testende Objekt */
	public LogLevel logLevel = new LogLevel();

	/** Prüft, ob Ausführung ohne Parameter ein Fehler geliefert wird */
	@Test
	public void testeExecOhneParameter() {
		String erg = logLevel.exec(new Scanner(""), null);
		assertTrue(erg.equals("Kein Loglevel angegeben!"));
	}

	/** Prüft, ob Ausführung mit falschem Parameter ein Fehler geliefert wird */
	@Test
	public void testeExecMitFalschemParameter() {
		String erg = logLevel.exec(new Scanner(" xxx"), null);
		assertTrue(erg.equals("Unbekannter Loglevel: xxx"));
	}

	/** Prüft, ob Ausführung mit korrektem Parameter funktioniert */
	@Test
	public void testeExecMitKorrektemParameter() {
		String erg = logLevel.exec(new Scanner("info"), null);
		assertTrue(erg.equals("OK INFO"));
	}

}
