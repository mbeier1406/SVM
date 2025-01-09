package com.github.mbeier1406.svm.prg;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;

/**
 * Tests für die Klasse {@linkplain SVMProgramShort}.
 */
public class SVMProgramShortTest {

	/** Das zu testende Objekt */
	public final SVMProgramShort svmProgram = new SVMProgramShort();

	/** Ein paar Label zum Test */
	public final Label labelA = new Label(LabelType.DATA, "A");
	public final Label labelB = new Label(LabelType.DATA, "B");
	public final Label labelC = new Label(LabelType.DATA, "C");

	/** Dieser Label gilt nur für Instruktionen */
	public final Label labelFalscherTypData = new Label(LabelType.INSTRUCTION, "L");

	/** Ein paar Datensätze zum Test */
	public final SVMProgram.Data<Short> dataA = new SVMProgram.Data<>(labelA, new Short[]{1, 2, 3});
	public final SVMProgram.Data<Short> dataB = new SVMProgram.Data<>(labelB, new Short[]{2, 3, 4});
	public final SVMProgram.Data<Short> dataC = new SVMProgram.Data<>(labelC, new Short[]{3, 4, 5});

	/** Datenprüfung: Stellt sicher, dass die Labels für Datenobjekte eindeutig sind */
	@Test
	public void testeDoppelteDatenlabel() throws SVMException {
		Stream.of(dataA, dataB, dataC, dataA).forEach(svmProgram::addData);
		SVMException svmException = assertThrows(SVMException.class, () -> svmProgram.validate());
		assertThat(svmException.getLocalizedMessage(), containsString("label=A]: Label doppelt (an Index 0)"));
	}


}