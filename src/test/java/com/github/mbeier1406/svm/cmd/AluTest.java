package com.github.mbeier1406.svm.cmd;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain Alu}.
 */
public class AluTest {

	/** Das zu testende Objekt */
	public Alu alu = new Alu();

	/** Prüft, ob ohne Parameter die richtige Meldung geliefert wird */
	@Test
	public void testeExec() {
		String erg = alu.exec(new Scanner(""), null, null);
		assertTrue(erg.equals(Alu.USAGE));
	}

}
