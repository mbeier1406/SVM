package com.github.mbeier1406.svm.instructions;

import static com.github.mbeier1406.svm.impl.RuntimeShort.WORTLAENGE_IN_BYTES;
import static com.github.mbeier1406.svm.instructions.InstructionFactory.INSTRUCTIONS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.impl.MEMShort;

/**
 * Tests für die Klasse {@linkplain InstructionReaderShort}.
 */
@ExtendWith(MockitoExtension.class)
public class InstructionReaderShortTest {

	public static final Logger LOGGER = LogManager.getLogger(InstructionReaderShortTest.class);

	/** Der Speicher, aus dem die Instruktion gelesen werden soll */
	public MEMShort mem = new MEMShort();

	/** Das zu testende Objekt */
	public static final InstructionReaderInterface<Short> INSTRUCTION_READER = new InstructionReaderShort();

	/** Speicher vor jedem Test löschen */
	@BeforeEach
	public void init() {
		mem.clear();
	}

	/** Prüft, ob die {@linkplain Nop}-Instruktion korrekt eingelesen wird */
	@Test
	public void testeNop() throws SVMException {
		mem.write(mem.getHighAddr(), (short) (Nop.CODE<<8));
		var instructionDefinition = INSTRUCTION_READER.getInstruction(mem, mem.getHighAddr());
		LOGGER.info("instructionDefinition={}",instructionDefinition);
		assertThat(instructionDefinition.instr().getClass(), equalTo(Nop.class));
		assertThat(instructionDefinition.args().length, equalTo(0));
		assertThat(instructionDefinition.len(), equalTo(1));
	}

	/** Prüft, ob die {@linkplain Int}-Instruktion mitParametr korrekt eingelesen wird */
	@Test
	public void testeInt() throws SVMException {
		mem.write(mem.getHighAddr(), (short) ((Int.class.getAnnotation(Instruction.class).code()<<8)+0x27));
		var instructionDefinition = INSTRUCTION_READER.getInstruction(mem, mem.getHighAddr());
		LOGGER.info("instructionDefinition={}",instructionDefinition);
		assertThat(instructionDefinition.instr().getClass(), equalTo(Int.class));
		assertThat(instructionDefinition.args().length, equalTo(1));
		assertThat(instructionDefinition.args()[0], equalTo((byte) 0x27));
		assertThat(instructionDefinition.len(), equalTo(1));
	}

	/**
	 * Testet das Einlesen von Instruktionen: im Speicher wird deren Code abgelegt (Parameter bleiben leer),
	 * danach wird geprüft, ob die korrekte {@linkplain InstructionInterface Instruktion} mit der erwarteten
	 * Länge eingelesen wurde.
	 * @param code der {@linkplain Instruction#code() Code} der gerade getesteten {@linkplain InstructionInterface Instruktion}
	 * @param instr die dazu erwartete Instruktion
	 * @param len die erwartete Länge der Instruktion
	 * @throws SVMException bei technischen Fehlern
	 */
	@ParameterizedTest
	@MethodSource("getTestParameter")
	public void testeInstruktionEinlesen(final InstructionInterface.Codes code, InstructionInterface<Short> instr, int len) throws SVMException {
		mem.write(mem.getHighAddr(), (short) (instr.getClass().getAnnotation(Instruction.class).code()<<8));
		var instructionDefinition = INSTRUCTION_READER.getInstruction(mem, mem.getHighAddr());
		LOGGER.info("instructionDefinition={}",instructionDefinition);
		assertThat(instructionDefinition.instr().getClass(), equalTo(instr.getClass()));
		assertThat(instructionDefinition.args().length, equalTo(instructionDefinition.instr().getAnzahlParameter()));
		assertThat(instructionDefinition.len(), equalTo(len));
	}

	/** Liefert die Testfälle (alle Instruktionen): 1. den Code der Instruktion, die erwartete Instruktion und die erwartete Länge der Instruktion */
	@SuppressWarnings("unchecked")
	public static Stream<Arguments> getTestParameter() {
		return Arrays
				.stream(InstructionInterface.Codes.values())
				.map(code -> new Object[] {code, INSTRUCTIONS.get(code.getCode())})
				.map(codeAndInstr -> new Object[] {
						codeAndInstr[0],
						codeAndInstr[1],
						INSTRUCTION_READER.getInstrLenInWords((InstructionInterface<Short>) codeAndInstr[1], WORTLAENGE_IN_BYTES)})
				.map(Arguments::of);
	}

}
