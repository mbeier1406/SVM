package com.github.mbeier1406.svm.instructions;

import static com.github.mbeier1406.svm.impl.RuntimeShort.WORTLAENGE_IN_BYTES;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests für die Klasse {@linkplain InstructionDefinition}.
 */
public class InstructionDefinitionTest {

	/** Als Parameter für die Tests */
	public InstructionInterface<Short> instruction = InstructionFactory.INSTRUCTIONS.get(Nop.CODE);


	/** Instruction ist <b>null</b> */
	@Test
	public void testeNullInstructionInterface() {
		assertThrows(NullPointerException.class, () -> new InstructionDefinition<Short>(null, new byte[]{}, WORTLAENGE_IN_BYTES));
	}

	/** Parameterliste ist <b>null</b> */
	@Test
	public void testeNullParams() {
		assertThrows(NullPointerException.class, () -> new InstructionDefinition<Short>(instruction, null, WORTLAENGE_IN_BYTES));
	}

}
