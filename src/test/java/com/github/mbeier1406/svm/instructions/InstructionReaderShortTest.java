package com.github.mbeier1406.svm.instructions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.impl.MEMShort;
import com.github.mbeier1406.svm.instructions.InstructionReaderInterface.InstructionDefinition;

/**
 * Tests für die Klasse {@linkplain InstructionReaderShort}.
 */
public class InstructionReaderShortTest {

	public static final Logger LOGGER = LogManager.getLogger(InstructionReaderShortTest.class);

	/** Der Speicher, aus dem die Instruktion gelesen werden soll */
	public MEMShort mem = new MEMShort();

	/** Das zu testende Objekt */
	public InstructionReaderInterface<Short> instructionReader = new InstructionReaderShort();

	/** Prüft, ob die {@linkplain Nop}-Instruktion korrekt eingelesen wird */
	@Test
	public void testeNop() throws SVMException {
		mem.write(mem.getHighAddr(), (short) 256);
		var instructionDefinition = instructionReader.getInstruction(mem, mem.getHighAddr());
		LOGGER.info("instructionDefinition={}",instructionDefinition);
		assertThat(instructionDefinition.instruction().getClass(), equalTo(Nop.class));
		assertThat(instructionDefinition.len(), equalTo(1));
	}

}
