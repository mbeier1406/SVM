package com.github.mbeier1406.svm.instructions;

import static com.github.mbeier1406.svm.SVM.BD_BYTE;
import static com.github.mbeier1406.svm.SVM.BD_SHORT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests für die Klasse {@linkplain InstructionBase}.
 */
public class InstructionBaseTest {

	public static final Logger LOGGER = LogManager.getLogger(InstructionBaseTest.class);

	/** Stellt sicher, dass die Umwandlung von zwei Byte-Parametern zu einem Short funktioniert */
	@ParameterizedTest
	@MethodSource("getTestParameter")
	public void testeBytes2Short(byte links, byte rechts, short erwartetesErgebnis) {
		LOGGER.info("Links: {}; rechts: {}; erwartet: {}",
				BD_BYTE.getBinaerDarstellung(links),
				BD_BYTE.getBinaerDarstellung(rechts),
				BD_SHORT.getBinaerDarstellung(erwartetesErgebnis));
		short bytes2Short = InstructionBase.bytes2Short(links, rechts);
		LOGGER.info("bytes2Short: {}", BD_SHORT.getBinaerDarstellung(bytes2Short));
		assertThat(bytes2Short, equalTo(erwartetesErgebnis));
	}

	/** Liefert die Testfälle */
	public static Stream<Arguments> getTestParameter() {
		return Stream.of(
				Arguments.of(Byte.MAX_VALUE, Byte.MAX_VALUE, (short) 32639),
				Arguments.of((byte) -1, (byte) -1, (short) -1),
				Arguments.of((byte) 0, (byte) 0, (short) 0)
			);
	}

}
