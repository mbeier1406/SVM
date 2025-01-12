package com.github.mbeier1406.svm.prg;

import java.util.ArrayList;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.impl.MEMShort;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.instructions.InstructionFactory;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.instructions.Int;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;

/**
 * Tests für die Klasse {@linkplain SVMLoaderShort}.
 */
public class SVMLoaderShortTest {

	public static final Logger LOGGER = LogManager.getLogger(SVMLoaderShortTest.class);

	public static final InstructionInterface<Short> INT = InstructionFactory.INSTRUCTIONS.get(Int.CODE);

	public static final Optional<Label> EMPTY_LABEL = Optional.empty();

	public static Optional<Label>[] ONE_EMPTY_LABEL;

	public MEMShort mem;

	@BeforeEach
	public void init() {
		this.mem = new MEMShort();
		this.mem.clear();
		ONE_EMPTY_LABEL = (Optional<Label>[]) new ArrayList<Optional<Label>>().toArray();
		//{EMPTY_LABEL}
	}

	@Test
	public void testeLaden() {
		SVMProgram<Short> svmProgramm = new SVMProgramShort();
		svmProgramm.addData(new Data<Short>(new Label(LabelType.DATA, "text1"), new Short[] { (short) 'a', (short) 'b', (short) 'c', (short) '\n' }));
		svmProgramm.addData(new Data<Short>(new Label(LabelType.DATA, "text2"), new Short[] { (short) 'X', (short) 'Y', (short) '\n' }));
		svmProgramm.addInstruction(new VirtualInstruction<Short>(Optional.empty(), new InstructionDefinition<Short>(INT, new byte[] {}, Optional.empty()), new Optional[0]));
	}

}
