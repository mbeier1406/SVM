package com.github.mbeier1406.svm.prg;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.instructions.InstructionFactory;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.instructions.Int;
import com.github.mbeier1406.svm.instructions.Mov;
import com.github.mbeier1406.svm.instructions.Nop;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;

/**
 * Tests für die Klasse {@linkplain SVMProgramShort}.
 */
public class SVMProgramShortTest {

	/** Das zu testende Objekt */
	public SVMProgramShort svmProgram;

	/** Ein paar Label zum Test */
	public final Label labelA = new Label(LabelType.DATA, "A");
	public final Label labelB = new Label(LabelType.DATA, "B");
	public final Label labelC = new Label(LabelType.DATA, "C");
	public final Label labelD = new Label(LabelType.INSTRUCTION, "D");

	/** Dieser Label gilt nur für Instruktionen */
	public final Label labelFalscherTypData = new Label(LabelType.INSTRUCTION, "L");

	/** Verschiedene Label-Listen für Instruktionen mit und ohne Parameter */
	@SuppressWarnings("unchecked") public Optional<SVMProgram.Label>[] emptyLabelList = (Optional<SVMProgram.Label>[]) new Optional[0];
	@SuppressWarnings("unchecked") public Optional<SVMProgram.Label>[] oneLabelList = (Optional<SVMProgram.Label>[]) new Optional[] { Optional.empty() };


	/** Ein paar Datensätze zum Test */
	public final SVMProgram.Data<Short> dataA = new SVMProgram.Data<>(labelA, new Short[]{1, 2, 3});
	public final SVMProgram.Data<Short> dataB = new SVMProgram.Data<>(labelB, new Short[]{2, 3, 4});
	public final SVMProgram.Data<Short> dataC = new SVMProgram.Data<>(labelC, new Short[]{3, 4, 5});

	/** Einige Instruktionen */
	public final InstructionInterface<Short> NOP = InstructionFactory.INSTRUCTIONS.get(Nop.CODE);
	public final InstructionInterface<Short> INT = InstructionFactory.INSTRUCTIONS.get(Int.CODE);
	public final InstructionInterface<Short> MOV = InstructionFactory.INSTRUCTIONS.get(Mov.CODE);

	/** Instruktionen mit Parametern */
	public final InstructionDefinition<Short> instrNop = new InstructionDefinition<>(NOP, new byte[] {}, Optional.empty());
	public final InstructionDefinition<Short> instrInt = new InstructionDefinition<>(INT, new byte[] {1}, Optional.empty());
	public final InstructionDefinition<Short> instrMov = new InstructionDefinition<>(MOV, new byte[] {1,2,3,4,5}, Optional.empty());

	/** Einige Instruktionen <b>ohne</b> Label */
	public final SVMProgram.VirtualInstruction<Short> virtInstrNopOhneLabel = new SVMProgram.VirtualInstruction<>(Optional.empty(), instrNop, emptyLabelList);

	/** Einige Instruktionen <b>mit</b> Label */
	public final SVMProgram.VirtualInstruction<Short> virtInstrNopMitLabelA = new SVMProgram.VirtualInstruction<>(Optional.of(labelA), instrNop, emptyLabelList);
	public final SVMProgram.VirtualInstruction<Short> virtInstrInt1 = new SVMProgram.VirtualInstruction<>(Optional.empty(), instrInt, oneLabelList);


	@BeforeEach
	public void init() {
		svmProgram = new SVMProgramShort();
	}

	/** Daten: Stellt sicher, dass die Labels für Datenobjekte eindeutig sind */
	@Test
	public void testeDoppelteDatenlabel() throws SVMException {
		Stream.of(dataA, dataB, dataC, dataA).forEach(svmProgram::addData);
		SVMException svmException = assertThrows(SVMException.class, () -> svmProgram.validate());
		assertThat(svmException.getLocalizedMessage(), containsString("label=A]: Label doppelt (an Index 0)"));
	}

	/** Instr: Stellt sicher, dass das Programm mind. eine Instruktion enthält */
	public void testeLeeresProgramm() throws SVMException {
		SVMException svmException = assertThrows(SVMException.class, () -> svmProgram.validate());
		assertThat(svmException.getLocalizedMessage(), containsString("Leeres Programm"));
	}

	/** Instr: Stellt sicher, dass die Labels der Instruktionen nicht auf die Labels der Daten verweisen */
	@Test
	public void testeDatenlabelInInstrVerwendet() throws SVMException {
		Stream.of(dataA, dataB, dataC).forEach(svmProgram::addData);
		Stream.of(virtInstrNopOhneLabel, virtInstrNopMitLabelA).forEach(svmProgram::addInstruction);
		SVMException svmException = assertThrows(SVMException.class, () -> svmProgram.validate());
		assertThat(svmException.getLocalizedMessage(), containsString("[Instr] in Daten: Index 1: Label Label[labelType=DATA, label=A]: Label doppelt (an Index 0)"));
	}

	/** Instr: Stellt sicher, dass die Labels für Sprungadressen nicht doppelt vergeben werden */
	@Test
	public void testeLabelInInstrDoppeltVerwendet() throws SVMException {
		Stream.of(virtInstrNopMitLabelA, virtInstrNopOhneLabel, virtInstrNopMitLabelA).forEach(svmProgram::addInstruction);
		SVMException svmException = assertThrows(SVMException.class, () -> svmProgram.validate());
		assertThat(svmException.getLocalizedMessage(), containsString("[Instr] Index 2: Label Label[labelType=DATA, label=A]: Label doppelt (an Index 0)"));
	}

	/** Syscall INT(1) wird als letzte Instruktion im programm erwartet */
	@Test
	public void testeLetzteInstruktionNichtInt1() {
		Stream.of(virtInstrNopMitLabelA, virtInstrInt1, virtInstrNopOhneLabel).forEach(svmProgram::addInstruction);
		SVMException svmException = assertThrows(SVMException.class, () -> svmProgram.validate());
		assertThat(svmException.getLocalizedMessage(), containsString("[Instr] INT(1) wird als letzte Instruktion erwartet"));
	}

}
