package com.github.mbeier1406.svm.prg;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.instructions.InstructionFactory;
import com.github.mbeier1406.svm.instructions.Nop;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;

/**
 * Tests für die Klasse {@linkplain SVMProgram}.
 */
public class SVMProgramTest {

	/** Als Parameter für die Tests */
	public SVMProgram.Label label = new SVMProgram.Label(LabelType.DATA, "A");

	/** Als Parameter für die Tests */
	public InstructionDefinition<Short> instructionDefinition = new InstructionDefinition<Short>(InstructionFactory.INSTRUCTIONS.get(Nop.CODE), new byte[] {}, 1);


	/** Label ist <b>null</b> */
	@Test
	public void testeNullLabel() {
		assertThrows(NullPointerException.class, () -> new SVMProgram.Label(LabelType.DATA, null));
	}

	/** Label ist leer */
	@Test
	public void testeEmptyLabel() {
		final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new SVMProgram.Label(LabelType.DATA, ""));
		assertThat(ex.getLocalizedMessage(), containsString("label"));
	}

	/** VirtualInstruction: InstructionDefinition ist <b>null</b> */
	@Test
	public void testeVirtualInstructionNullInstructionDefinition() {
		assertThrows(NullPointerException.class, () -> new SVMProgram.VirtualInstruction<Short>(null, new SVMProgram.Label[] {}));
	}

	/** VirtualInstruction: LabelListe ist <b>null</b> */
	@Test
	public void testeVirtualInstructionNullLabelList() {
		assertThrows(NullPointerException.class, () -> new SVMProgram.VirtualInstruction<Short>(instructionDefinition, null));
	}

	/** VirtualInstruction: LabelListe enthält einen <b>null</b>-Wert */
	@Test
	public void testeVirtualInstructionNullValueInLabelList() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () ->
			new SVMProgram.VirtualInstruction<Short>(instructionDefinition, new SVMProgram.Label[] { label, null, label}));
		assertThat(ex.getLocalizedMessage(), containsString("labelList[1]"));
	}

	/** Data: Label ist <b>null</b> */
	@Test
	public void testeNullLabelData() {
		assertThrows(NullPointerException.class, () -> new SVMProgram.Data<Short>(null, new Short[] {1}));
	}

	/** Data: Label ist ungültig */
	@Test
	public void testeIllegalLabelTypeData() {
		final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
			new SVMProgram.Data<Short>(new SVMProgram.Label(LabelType.INSTRUCTION, "A"), new Short[] {1}));
		assertThat(ex.getLocalizedMessage(), containsString("Labeltyp ungültig"));
	}

	/** Data: Daten sind <b>null</b> */
	@Test
	public void testeNullDaten1Data() {
		assertThrows(NullPointerException.class, () -> new SVMProgram.Data<Short>(label, null));
	}

	/** Data: Daten sind <b>null</b> */
	@Test
	public void testeNullDaten2Data() {
		assertThrows(IllegalArgumentException.class, () -> new SVMProgram.Data<Short>(label, new Short[]{}));
	}

	/** Data: Daten enthalten <b>null</b>-Werte */
	@Test
	public void testeNullDaten3Data() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> new SVMProgram.Data<Short>(label, new Short[]{ 1, null, 2}));
		assertThat(ex.getLocalizedMessage(), containsString("dataList[1]"));
	}

}
