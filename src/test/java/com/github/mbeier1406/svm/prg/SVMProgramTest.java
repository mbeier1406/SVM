package com.github.mbeier1406.svm.prg;

import static com.github.mbeier1406.svm.instructions.InstructionFactory.INSTRUCTIONS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.instructions.Int;
import com.github.mbeier1406.svm.instructions.Mov;
import com.github.mbeier1406.svm.instructions.Nop;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;

/**
 * Tests für die Klasse {@linkplain SVMProgram}.
 */
public class SVMProgramTest {

	/** Als Parameter für die Tests */
	public SVMProgram.Label label = new SVMProgram.Label(LabelType.DATA, "A");

	/** NOP Als Parameter für die Tests */
	public InstructionDefinition<Short> instructionDefinitionNOP = new InstructionDefinition<Short>(INSTRUCTIONS.get(Nop.CODE), new byte[] {}, 1);

	/** INT Als Parameter für die Tests */
	public InstructionDefinition<Short> instructionDefinitionINT = new InstructionDefinition<Short>(INSTRUCTIONS.get(Int.CODE), new byte[] {1}, 1);

	/** MOV Als Parameter für die Tests */
	public InstructionDefinition<Short> instructionDefinitionMOV = new InstructionDefinition<Short>(INSTRUCTIONS.get(Mov.CODE), new byte[] {1,2,3,4,5}, 1);

	/** Leere Label-Liste für Instruktionen ohne Parameter */
	public SVMProgram.Label[] emptyLabelList = new SVMProgram.Label[0];

	/** Label-Liste für Instruktionen mit einem Parameter */
	public SVMProgram.Label[] oneLabelList = new SVMProgram.Label[] { label };

	/** Label-Liste für Instruktionen mit drei Parametern */
	public SVMProgram.Label[] threeLabelList = new SVMProgram.Label[] { label, label,label };


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
		assertThrows(NullPointerException.class, () -> new SVMProgram.VirtualInstruction<Short>(null, null, emptyLabelList, null));
	}

	/** VirtualInstruction: LabelListe ist <b>null</b> */
	@Test
	public void testeVirtualInstructionNullLabelList() {
		assertThrows(NullPointerException.class, () -> new SVMProgram.VirtualInstruction<Short>(null, instructionDefinitionNOP, null, null));
	}

	/** VirtualInstruction (NOP): LabelListe enthält nicht die erwartete Anzahl Elemente */
	@Test
	public void testeVirtualInstructionLenLabelListNOP() {
		final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
			new SVMProgram.VirtualInstruction<Short>(null, instructionDefinitionNOP, oneLabelList, null));
		assertThat(ex.getLocalizedMessage(), containsString("erwartete Parameter: 0; erhalteneAnzahlParameter: 1"));
	}

	/** VirtualInstruction (INT): LabelListe enthält nicht die erwartete Anzahl Elemente */
	@Test
	public void testeVirtualInstructionLenLabelListINT() {
		final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
			new SVMProgram.VirtualInstruction<Short>(null, instructionDefinitionINT, emptyLabelList, null));
		assertThat(ex.getLocalizedMessage(), containsString("erwartete Parameter: 1; erhalteneAnzahlParameter: 0"));
	}

	/** VirtualInstruction (MOV): LabelListe enthält nicht die erwartete Anzahl Elemente */
	@Test
	public void testeVirtualInstructionLenLabelListMOV() {
		final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
			new SVMProgram.VirtualInstruction<Short>(null, instructionDefinitionMOV, threeLabelList, null));
		assertThat(ex.getLocalizedMessage(), containsString("erwartete Parameter: 5; erhalteneAnzahlParameter: 3"));
	}

	/** Data: Label ist <b>null</b> */
	@Test
	public void testeNullLabelData() {
		assertThrows(NullPointerException.class, () -> new SVMProgram.Data<Short>(null, new Short[] {1}, null));
	}

	/** Data: Label ist ungültig */
	@Test
	public void testeIllegalLabelTypeData() {
		final IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
			new SVMProgram.Data<Short>(new SVMProgram.Label(LabelType.INSTRUCTION, "A"), new Short[] {1}, null));
		assertThat(ex.getLocalizedMessage(), containsString("Labeltyp ungültig"));
	}

	/** Data: Daten sind <b>null</b> */
	@Test
	public void testeNullDaten1Data() {
		assertThrows(NullPointerException.class, () -> new SVMProgram.Data<Short>(label, null, null));
	}

	/** Data: Daten sind <b>null</b> */
	@Test
	public void testeNullDaten2Data() {
		assertThrows(IllegalArgumentException.class, () -> new SVMProgram.Data<Short>(label, new Short[]{}, null));
	}

	/** Data: Daten enthalten <b>null</b>-Werte */
	@Test
	public void testeNullDaten3Data() {
		final NullPointerException ex = assertThrows(NullPointerException.class, () -> new SVMProgram.Data<Short>(label, new Short[]{ 1, null, 2}, null));
		assertThat(ex.getLocalizedMessage(), containsString("dataList[1]"));
	}

}
