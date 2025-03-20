package com.github.mbeier1406.svm.instructions;

import static com.github.mbeier1406.svm.impl.RuntimeShort.WORTLAENGE_IN_BYTES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests für die Klasse {@linkplain InstructionReaderInterface}.
 */
@ExtendWith(MockitoExtension.class)
public class InstructionReaderInterfaceTest {

	/** Die Instruktion, deren Länge berechnet werden soll */
	@Mock
	public InstructionInterface<Short> instruction;

	/** Das zu testende Objekt */
	public InstructionReaderInterface<Short> instructionReader = new InstructionReaderShort();

	/** Stellt sicher, dass zu jeder Instruktion die korrekte Anzahl Speicherwörter (also deren Länge) berechnet wird */
	@ParameterizedTest
	@MethodSource("getTestParameter")
	public void teste(int anzahlParameter, int erwarteteWortLaenge) {
		when(instruction.getAnzahlParameter()).thenReturn(anzahlParameter);
		int instrLenInWords = instructionReader.getInstrLenInWords(instruction, WORTLAENGE_IN_BYTES);
		assertThat(instrLenInWords, equalTo(erwarteteWortLaenge));
	}

	/** Liefert die Testfälle */
	public static Stream<Arguments> getTestParameter() {
		return Stream.of(
				Arguments.of(4, 3), // vier Parameter = Wortlänge 3 (Short)
				Arguments.of(3, 2),
				Arguments.of(2, 2), // zwei Parameter = Wortlänge 2 (Short)
				Arguments.of(1, 1), // ein Parameter = Wortlänge 1 (Short)
				Arguments.of(0, 1) 	// kein Parameter = Wortlänge 1 (Short)
			);
	}

}
