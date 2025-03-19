package com.github.mbeier1406.svm.cmd;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain Information}.
 */
public class InformationTest {

	/** Das zu testende Objekt */
	public Information info = new Information();

	/** Stellt sicher, dass eine Hilfe bei Benutzung ohne Parameter ausgegeben wird */
	@Test
	public void testeExecOhneParameter() {
		assertTrue(info.exec(new Scanner(""), null, null).equals(Information.USAGE));

	}

}
