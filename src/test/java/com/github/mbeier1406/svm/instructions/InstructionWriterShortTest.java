package com.github.mbeier1406.svm.instructions;

import static com.github.mbeier1406.svm.impl.RuntimeShort.WORTLAENGE_IN_BYTES;
import static com.github.mbeier1406.svm.instructions.InstructionFactory.INSTRUCTIONS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.impl.MEMShort;

/**
 * Tests für die Klasse {@linkplain InstructionWriterShort}.
 */
public class InstructionWriterShortTest {

	public static final Logger LOGGER = LogManager.getLogger(InstructionWriterShortTest.class);

	/** Das zu testende Objekt */
	public InstructionWriterInterface<Short> instructionWriter = new InstructionWriterShort();

	/** Stellt sicher, dass Null-Werte korrekte Fehler produzieren */
	@Test
	public void teste2ArrayNullParameter() {
		NullPointerException ex = assertThrows(NullPointerException.class, () -> instructionWriter.instruction2Array(null));
		assertThat(ex.getMessage(), equalTo("instr"));
	}

	/** Stellt sicher, dass bei unpassender Parameterliste ein entsprechender Fehöer erzeugt wird */
	@Test
	public void teste2ArrayFalscheAnzahlParameter() {
		SVMException ex = assertThrows(SVMException.class, () -> instructionWriter.instruction2Array(new InstructionDefinition<>(new Nop(), new byte[1], WORTLAENGE_IN_BYTES)));
		assertThat(ex.getMessage(), containsString("erwartete Parameter: 0; erhalteneAnzahlParameter: 1"));
	}

	/** Stellt sicher, dass aus einer Instruktion mit Parametern die korrekte Wortfolge für den Speicher wird */
	@ParameterizedTest
	@MethodSource("getTestParameter")
	public void testeInstruction2Array(final InstructionInterface<Short> instr, byte[] params, Short[] speicherErwartet) throws SVMException {
		LOGGER.trace("instr={}", instr);
		Short[] instruction2Array = instructionWriter.instruction2Array(new InstructionDefinition<>(instr, params, WORTLAENGE_IN_BYTES));
		for ( int i=0; i < speicherErwartet.length; i++ )
			LOGGER.info("speicherErwartet[{}]={}\t({})", i, speicherErwartet[i], SVM.BD_SHORT.getBinaerDarstellung(speicherErwartet[i]));
		for ( int i=0; i < instruction2Array.length; i++ )
			LOGGER.info("instruction2Array[{}]={}\t({})", i, instruction2Array[i], SVM.BD_SHORT.getBinaerDarstellung(instruction2Array[i]));
		assertThat(instruction2Array, equalTo(speicherErwartet));
	}

	/** Analog {@linkplain #testeInstruction2Array(InstructionInterface, byte[], Short[])} prüfen, ob der Speicher korrekt geschrieben wird */
	@ParameterizedTest
	@MethodSource("getTestParameter")
	public void testeWriteInstruction(final InstructionInterface<Short> instr, byte[] params, Short[] speicherErwartet) throws SVMException {
		LOGGER.trace("instr={}", instr);
		var mem = new MEMShort().clear();
		var instructionInterface = mem.getInstructionInterface();
		var instructionDefinition = new InstructionDefinition<>(instr, params, WORTLAENGE_IN_BYTES);
		var instruction2Array = instructionWriter.instruction2Array(new InstructionDefinition<>(instr, params, WORTLAENGE_IN_BYTES));
		instructionWriter.writeInstruction(instructionInterface, mem.getLowAddr(), instructionDefinition);
		for ( int i=0; i < instruction2Array.length; i++ )
			assertThat(instructionInterface.read(mem.getLowAddr()+i), equalTo(instruction2Array[i]));
	}

	/** Liefert die Testfälle: 1. die Instruktion 2. die Parametern 3. die erwartete Bytefolge */
	public static Stream<Arguments> getTestParameter() {
		return Stream.of(
				Arguments.of(INSTRUCTIONS.get(Mov.CODE),
						new byte[]{ (byte) 0x13, (byte) 0x0, (byte) 0x02, (byte) 0x04, (byte) 0x6b },
						new Short[] {(short) 787, (short) 0x2, (short) 0x046b }), // MOV REG(2)->ADDR(1131)
				Arguments.of(INSTRUCTIONS.get(Mov.CODE),
						new byte[]{ (byte) 0x21, (byte) 0x0, (byte) 0x33, (byte) 0x0, (byte) 0x1 },
						new Short[] {(short) 801, (short) 0x33, (short) 0x1 }), // MOV CONST(51)->REG(1)
				Arguments.of(INSTRUCTIONS.get(Int.CODE), new byte[]{ (byte) 0x1 }, new Short[] {(short) 513}), // INT 1 => Syscall
				Arguments.of(INSTRUCTIONS.get(Nop.CODE), new byte[]{}, new Short[] {(short) 256}));
	}

}
