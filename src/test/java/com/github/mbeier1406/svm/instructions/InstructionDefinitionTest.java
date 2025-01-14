package com.github.mbeier1406.svm.instructions;

import static com.github.mbeier1406.svm.instructions.InstructionFactory.INSTRUCTIONS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;

/**
 * Tests für die Klasse {@linkplain InstructionDefinition}.
 */
public class InstructionDefinitionTest {

	/** Als Parameter für die Tests */
	public InstructionInterface<Short> instruction = INSTRUCTIONS.get(Nop.CODE);


	/** Instruction ist <b>null</b> */
	@Test
	public void testeNullInstructionInterface() {
		assertThrows(NullPointerException.class, () -> new InstructionDefinition<Short>(null, new byte[]{}, null));
	}

	/** Parameterliste ist <b>null</b> */
	@Test
	public void testeNullParams() {
		assertThrows(NullPointerException.class, () -> new InstructionDefinition<Short>(instruction, null, null));
	}

	/** Wenn die Länge im Speicher nicht gesetzt ist, darf sie nicht abgefragt werden! */
	@Test
	public void testeLenInWordsNotSet() {
		assertThrows(SVMException.class, () -> new InstructionDefinition<Short>(instruction, new byte[]{}, null).getLenInMemoryInWords());
	}

	/** Wenn die Länge im Speicher gesetzt ist, muss sie korrekt zurückgegeben werden! */
	@Test
	public void testeLenInWordsSet() throws SVMException {
		assertThat(new InstructionDefinition<Short>(instruction, new byte[]{}, 3).getLenInMemoryInWords(), equalTo(3));
	}

	/** Stellt sicher, dass bei unpassender Parameterliste NOP ein entsprechender Fehöer erzeugt wird */
	@Test
	public void testeFalscheAnzahlParameterNop() {
		final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new InstructionDefinition<Short>(instruction, new byte[]{1}, null));
		assertThat(ex.getMessage(), containsString("erwartete Parameter: 0; erhalteneAnzahlParameter: 1"));
	}

	/** Stellt sicher, dass bei unpassender Parameterliste INT ein entsprechender Fehöer erzeugt wird */
	@Test
	public void testeFalscheAnzahlParameterInt() {
		final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new InstructionDefinition<Short>(INSTRUCTIONS.get(Int.CODE), new byte[]{1,2}, null));
		assertThat(ex.getMessage(), containsString("erwartete Parameter: 1; erhalteneAnzahlParameter: 2"));
	}

	/** Stellt sicher, dass bei unpassender Parameterliste MOV ein entsprechender Fehöer erzeugt wird */
	@Test
	public void testeFalscheAnzahlParameterMov() {
		final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new InstructionDefinition<Short>(INSTRUCTIONS.get(Mov.CODE), new byte[]{1}, null));
		assertThat(ex.getMessage(), containsString("erwartete Parameter: 5; erhalteneAnzahlParameter: 1"));
	}

}
