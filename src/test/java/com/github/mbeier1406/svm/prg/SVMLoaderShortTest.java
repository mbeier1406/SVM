package com.github.mbeier1406.svm.prg;

import static org.apache.logging.log4j.CloseableThreadContext.put;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain SVMLoaderShort}.
 */
public class SVMLoaderShortTest {

	public static final Logger LOGGER = LogManager.getLogger(SVMLoaderShortTest.class);

	@Test
	public void test() {
		int i = 0;
		Integer j = 0;
		AtomicInteger k = new AtomicInteger(0); 
		try ( CloseableThreadContext.Instance ctx = put("i", String.valueOf(i)).put("j", String.valueOf(j)).put("k", k.toString()) ) {
			LOGGER.info("A: {} {}", i, j);
			i++; j++; k.addAndGet(1);
			LOGGER.info("B: {} {} {}", i, j, k.toString());
		}

	}

}
