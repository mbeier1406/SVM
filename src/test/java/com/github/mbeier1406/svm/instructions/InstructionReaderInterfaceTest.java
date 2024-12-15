package com.github.mbeier1406.svm.instructions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
//import static org.mockito.Mockito.when;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
// import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.mbeier1406.svm.ALU.Instruction;
import com.github.mbeier1406.svm.SVMException;

/**
 * tests für die Klasse {@linkplain InstructionReaderInterface}.
 */
@ExtendWith(MockitoExtension.class)
public class InstructionReaderInterfaceTest {

	/**
	 * Die Instruktion, deren Länge berecnet werdne soll. Da Mockito nicht funktioniert über Hilfsklassen
	 * @see https://github.com/mockito/mockito/issues/3547
	 */
	// @Mock
	// public InstructionInterface<Short> instruction

	/** Instruktion ohne Parameter */
	public static class InstructionMock implements InstructionInterface<Short> {
		@Override
		public int getAnzahlParameter() {
			return 0;
		}
		@Override
		public int execute(byte[] params) throws SVMException {
			return 0;
		}
		@Override
		public void setAlu(Instruction<Short> alu) {
		}
		@Override
		public void setMemory(com.github.mbeier1406.svm.MEM.Instruction<Short> mem) {
		}
	};

	/** Instruktion mit einem Parameter */
	public static class InstructionMock1 extends InstructionMock {
		@Override
		public int getAnzahlParameter() {
			return 1;
		}
	}

	/** Instruktion mit zwei Parametern */
	public static class InstructionMock2 extends InstructionMock {
		@Override
		public int getAnzahlParameter() {
			return 2;
		}
	}

	/** Instruktion mit drei Parametern */
	public static class InstructionMock3 extends InstructionMock {
		@Override
		public int getAnzahlParameter() {
			return 3;
		}
	}

	/** Instruktion mit vier Parametern */
	public static class InstructionMock4 extends InstructionMock {
		@Override
		public int getAnzahlParameter() {
			return 4;
		}
	}

	/** Das zu testende Objekt */
	public InstructionReaderInterface<Short> instructionReader = new InstructionReaderShort();

	/** Wortläng der {@linkplain SVM} ist zwei Bytes (Short) */
	public final int wortLaengeInBytes = 2;

	/** Stellt sicher, dass zu jeder Instruktion die korrekte Anzahl Speicherwörter deren Länge berechnet wird */
	@ParameterizedTest
	@MethodSource("getTestParameter")
	public void teste(InstructionInterface<Short> instruction /*, int anzahlParameter */, int erwarteteWortLaenge) {
//		when(instruction.getAnzahlParameter()).thenReturn(anzahlParameter);
		int instrLenInWords = instructionReader.getInstrLenInWords(instruction, wortLaengeInBytes);
		assertThat(instrLenInWords, equalTo(erwarteteWortLaenge));
	}

	/** Liefert die Testfälle */
	public static Stream<Arguments> getTestParameter() {
		return Stream.of(
				Arguments.of(new InstructionMock4()/*, 4*/, 3), // vier Parameter = Wortlänge 3 (Short)
				Arguments.of(new InstructionMock3()/*, 3*/, 2),
				Arguments.of(new InstructionMock2()/*, 2*/, 2), // zwei Parameter = Wortlänge 2 (Short)
				Arguments.of(new InstructionMock1()/*, 1*/, 1), // ein Parameter = Wortlänge 1 (Short)
				Arguments.of(new InstructionMock()/*, 0*/, 1) 	// kein Parameter = Wortlänge 1 (Short)
			);
	}

}
