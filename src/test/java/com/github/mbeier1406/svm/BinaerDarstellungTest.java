package com.github.mbeier1406.svm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests für die Klasse {@linkplain BinaerDarstellung}.
 */
public class BinaerDarstellungTest {

	public static final Logger LOGGER = LogManager.getLogger(BinaerDarstellungTest.class);

	/**Das zu testende Objekt Typ {@linkplain Byte} */
	public static BinaerDarstellung<Byte> bdByte = new BinaerDarstellung<>();

	/**Das zu testende Objekt Typ {@linkplain Short} */
	public static BinaerDarstellung<Short> bdShort = new BinaerDarstellung<>();

	/** Stellt sicher, dass bei der binären Darstellung eines Wertes das korrekte Ergebnis je Typ ermittelt wird */
	@ParameterizedTest
	@MethodSource("getTestParameter")
	public <T extends Number> void testeBinaerdarstellung(final BinaerDarstellung<T> testee, T val, String ergebnis) {
		String binaerDarstellung = testee.getBinaerDarstellung(val);
		LOGGER.info("val={} -> {}", val, binaerDarstellung);
		assertThat(binaerDarstellung, equalTo(ergebnis));
	}

	/** Liefert die Testfälle */
	public static Stream<Arguments> getTestParameter() {
		return Stream.of(
				Arguments.of(bdShort, (short) 1 , "0000000000000001"), // Tests für den Typ Short
				Arguments.of(bdShort, (short) 257 , "0000000100000001"),
				Arguments.of(bdShort, (short) 769 , "0000001100000001"),
				Arguments.of(bdShort, (short) -31999 , "1000001100000001"),
				Arguments.of(bdByte, (byte) 1 , "00000001"), // Tests für den Typ Byte
				Arguments.of(bdByte, (byte) -1 , "11111111")
			);
	}

}
