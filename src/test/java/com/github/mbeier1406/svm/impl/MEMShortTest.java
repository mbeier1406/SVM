package com.github.mbeier1406.svm.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;

/**
 * Tests für die Klasse {@linkplain MEMShort}.
 */
public class MEMShortTest {

	public static final Logger LOGGER = LogManager.getLogger(MEMShortTest.class);

	/** Das zu testende Objekt */
	public MEMShort mem = new MEMShort();

	/** Beschreibt den Speicher mit zufälligen Werten und prüft, ob sie korrekt wieder eingelesen werden können */
	@Test
	public void testeLesenUndSchreiben() throws SVMException {
		var random = new Random();
		var werte = new short[mem.getHighAddr()+1]; // +1 da die Adressen von 0..len-1 gehen
		for ( int i=0; i < werte.length; i++ ) {
			werte[i] = (short) random.nextInt(Short.MAX_VALUE);
			LOGGER.trace("{}: {}", i, werte[i]);
			mem.write(i, werte[i]);
		}
		for ( int i=0; i < werte.length; i++ )
			assertThat(mem.read(i), equalTo(werte[i]));
	}

}
