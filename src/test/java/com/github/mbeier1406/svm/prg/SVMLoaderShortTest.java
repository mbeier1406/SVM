package com.github.mbeier1406.svm.prg;

import static com.github.mbeier1406.svm.impl.RuntimeShort.WORTLAENGE_IN_BYTES;
import static com.github.mbeier1406.svm.instructions.InstructionFactory.INSTRUCTIONS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
//import static org.hamcrest.Matchers.containsString;
//import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.MEM.Instruction;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.impl.MEMShort;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.instructions.InstructionReaderShort;
import com.github.mbeier1406.svm.instructions.Int;
import com.github.mbeier1406.svm.instructions.Mov;
import com.github.mbeier1406.svm.instructions.Nop;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;

/**
 * Tests für die Klasse {@linkplain SVMLoaderShort}.
 */
public class SVMLoaderShortTest {

	public static final Logger LOGGER = LogManager.getLogger(SVMLoaderShortTest.class);

	/** Instruktionen @lin {@linkplain Nop}, {@linkplain Int} usw. */
	public static final InstructionInterface<Short> NOP = INSTRUCTIONS.get(Nop.CODE);
	public static final InstructionInterface<Short> INT = INSTRUCTIONS.get(Int.CODE);
	public static final InstructionInterface<Short> MOV = INSTRUCTIONS.get(Mov.CODE);

	/** Leere Label-Referenz */
	public static final Optional<Label> EMPTY_VIRT_LABEL = Optional.empty();

	/** Verschiedene Label-Definitionen */
	public static final Label LABEL1 = new Label(LabelType.DATA, "text1");
	public static final Label LABEL2 = new Label(LabelType.DATA, "text2");

	/** Labellisten für virtuelle Instruktionen */
	@SuppressWarnings({ "unchecked", "serial" })
	public static final Optional<Label>[] ZERO_LABEL = (Optional<Label>[]) new ArrayList<Optional<Label>>() {}.toArray(new Optional[0]);
	@SuppressWarnings({ "unchecked", "serial" })
	public static final Optional<Label>[] ONE_EMPTY_LABEL = (Optional<Label>[]) new ArrayList<Optional<Label>>() {{ // ein Parameter ohne Label
		add(EMPTY_VIRT_LABEL);
	}}.toArray(new Optional[0]);

	/** Daten: 4-Worte String */
	public static final Data<Short> FOUR_WORDS_DATA = new Data<Short>(LABEL1, new Short[] { (short) 'a', (short) 'b', (short) 'c', (short) '\n' });

	/** Daten: 3-Worte String */
	public static final Data<Short> THREE_WORDS_DATA = new Data<Short>(LABEL2, new Short[] { (short) 'X', (short) 'Y', (short) '\n' });

	/** Verschiedene Instruktionsdefinition */
	public static final InstructionDefinition<Short> NOP0 = new InstructionDefinition<Short>(NOP, new byte[] {}, Optional.empty()); // NOP
	public static final InstructionDefinition<Short> INT1 = new InstructionDefinition<Short>(INT, new byte[] {1}, Optional.empty()); // INT(1)

	/** Verschiedene virtuelle Instruktion */
	public static final VirtualInstruction<Short> NOP0_OHNE_LABEL = new VirtualInstruction<Short>(Optional.empty(), NOP0, ZERO_LABEL); // INT(1) kein Label
	public static final VirtualInstruction<Short> INT1_OHNE_LABEL = new VirtualInstruction<Short>(Optional.empty(), INT1, ONE_EMPTY_LABEL); // INT(1) kein Label

	/** Das einzuspielende Programm */
	public SVMProgram<Short> svmProgramm;

	/** Der Speicher, in den das {@linkplain #svmProgramm} geladen wird */
	public MEMShort mem;

	/** Zugriff auf einzelne Speicheradressen in {@linkplain #mem} */
	public Instruction<Short> memInstrInterface;

	/** Das zu testende Objekt */
	public SVMLoader<Short> svmLoader;


	/** Initialisiert {@linkplain #svmLoader}, {@linkplain #mem} und {@linkplain #memInstrInterface} */
	@BeforeEach
	public void init() {
		svmLoader = new SVMLoaderShort();
		this.mem = new MEMShort();
		this.mem.clear();
		memInstrInterface = mem.getInstructionInterface();
	}


	/** Erstellt ein einfaches Programm mit etwas IO usw. */
	public static SVMProgram<Short> getKorrektesProgramm() {
		SVMProgram<Short> prg = new SVMProgramShort();

		/* Ein paar Daten einspielen */
		prg.addData(FOUR_WORDS_DATA);
		prg.addData(THREE_WORDS_DATA);

		/* ein paar Instruktionen einspielen */
		prg.addInstruction(NOP0_OHNE_LABEL);
		prg.addInstruction(INT1_OHNE_LABEL);

		return prg;
	}

	/** Lädt ein korrektes Programm und prüft die Werte im Speicher */
	@Test
	public void testeKorrektesLaden() throws SVMException {

		/* Programm laden, darf keine Exception werfen */
		svmProgramm = getKorrektesProgramm();
		svmLoader.load(mem, svmProgramm);

		/* Prüfen, ob die Labelliste korrekt gesetzt ist */
		var labelList = svmLoader.getLabelList();
		LOGGER.info("labelList={}", labelList);
		assertThat(labelList.size(), equalTo(2));
		assertThat(labelList.get(LABEL1), equalTo(mem.getLowAddr()));
		assertThat(labelList.get(LABEL2), equalTo(mem.getLowAddr()+FOUR_WORDS_DATA.dataList().length));

		/* Prüfen, ob die Daten korrekt übernommen wurden */
		int addr = mem.getLowAddr();
		assertThat(memInstrInterface.read(addr+2), equalTo(FOUR_WORDS_DATA.dataList()[2]));
		addr += FOUR_WORDS_DATA.dataList().length;
		assertThat(memInstrInterface.read(addr+1), equalTo(THREE_WORDS_DATA.dataList()[1]));

		/* Prüfen, ob die Instruktionen korrekt übernommen wurden */
		var instructionReader = new InstructionReaderShort();
		addr = mem.getHighAddr();
		var instrDef = instructionReader.getInstruction(mem, addr);
		assertThat(instrDef.instruction(), equalTo(NOP0.instruction()));
		assertThat(instrDef.params(), equalTo(NOP0.params()));
		addr -= instructionReader.getInstrLenInWords(NOP, WORTLAENGE_IN_BYTES);

		instrDef = instructionReader.getInstruction(mem, addr);
		assertThat(instrDef.instruction(), equalTo(INT1.instruction()));
		assertThat(instrDef.params(), equalTo(INT1.params()));
		addr -= instructionReader.getInstrLenInWords(INT, WORTLAENGE_IN_BYTES);

	}

}
