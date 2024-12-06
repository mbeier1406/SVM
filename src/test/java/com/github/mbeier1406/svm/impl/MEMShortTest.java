package com.github.mbeier1406.svm.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.MEM;

/**
 * Tests für die Klasse {@linkplain MEMShort}.
 */
public class MEMShortTest {

	public static final Logger LOGGER = LogManager.getFormatterLogger(MEMShortTest.class);

	/** Das zu testende Objekt */
	public MEM<Short> mem = new MEMShort();

	@Test
	public void testeTBinaryContentStringAt() {
		
	}

}
