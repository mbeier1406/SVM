package com.github.mbeier1406.svm.prg.parser.instructions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.instructions.InstructionFactory;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;
import com.github.mbeier1406.svm.prg.SVMProgram.VirtualInstruction;
import com.github.mbeier1406.svm.prg.SVMProgramShort;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.LineInfo;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;

/**
 * Tests für die Klasse {@linkplain Mov}.
 */
public class MovTest {

	/** Das zu testende Objekt */
	public Mov mov = new Mov();

	/** Label für einen Datenbereich, auf den Bezug genommen wird Labelreferenz) */
	public static final Label label1 = new Label(LabelType.DATA, "label1");

	/** Label für einen Datenbereich, auf den Bezug genommen wird Labelreferenz) */
	public static final Label label2 = new Label(LabelType.DATA, "label2");

	/** Bei Labelrefernezen wird auf den Datenbereich des Programms zugegriffen */
	public static SVMProgram<Short> svmProgram = new SVMProgramShort();

	/** Die Konstante "1" als Ergebnis der lexikalischen Analyse */
	public SVMLexer.Symbol const1 = new SVMLexer.Symbol(Token.CONSTANT, "1");

	/** Die Konstante "500" als Ergebnis der lexikalischen Analyse */
	public SVMLexer.Symbol const500 = new SVMLexer.Symbol(Token.CONSTANT, "500");

	/** Das Register "1" als Ergebnis der lexikalischen Analyse */
	public SVMLexer.Symbol reg1 = new SVMLexer.Symbol(Token.REGISTER, "1");

	/** Das Register "2" als Ergebnis der lexikalischen Analyse */
	public SVMLexer.Symbol reg2 = new SVMLexer.Symbol(Token.REGISTER, "2");

	/** Die Labelreferenz "label1" als Ergebnis der lexikalischen Analyse */
	public SVMLexer.Symbol label1Ref = new SVMLexer.Symbol(Token.LABEL_REF, label1.label());

	/** Die Labelreferenz "label2" als Ergebnis der lexikalischen Analyse */
	public SVMLexer.Symbol label2Ref = new SVMLexer.Symbol(Token.LABEL_REF, label2.label());

	/** Instruktion muss {@linkplain Mov} sein! */
	@SuppressWarnings("serial")
	public List<Symbol> notMovInstruction = new ArrayList<Symbol>() {{ add(SVMLexer.SYM_NOP); add(const1); add(SVMLexer.SYM_COMMA); add(reg2); }};

	/** Ungültige Instruktion {@code mov $1,, %2} */
	@SuppressWarnings("serial")
	public List<Symbol> invalidInstruction = new ArrayList<Symbol>() {{ add(SVMLexer.SYM_MOV); add(const1); add(SVMLexer.SYM_COMMA); add(SVMLexer.SYM_COMMA); add(reg2); }};

	/** Instruktion {@code mov $1, %2} */
	@SuppressWarnings("serial")
	public List<Symbol> mvConst1Reg2 = new ArrayList<Symbol>() {{ add(SVMLexer.SYM_MOV); add(const1); add(SVMLexer.SYM_COMMA); add(reg2); }};

	/** Instruktion {@code mov len(label1), %2} */
	@SuppressWarnings("serial")
	public List<Symbol> mvLenLabel1Reg2 = new ArrayList<Symbol>() {{
		add(SVMLexer.SYM_MOV); add(SVMLexer.SYM_FUNCTION_LEN); add(SVMLexer.SYM_LEFTPAR); add(label2Ref); add(SVMLexer.SYM_RIGHTPAR); add(SVMLexer.SYM_COMMA); add(reg2);
	}};

	/** Ungültige Typen für die Instruktion {@code mov} */
	@SuppressWarnings("serial")
	public List<List<Symbol>> invalidInstructionList = new ArrayList<List<Symbol>>() {{
		/** {@code mov (, %2} */
		add(new ArrayList<Symbol>() {{ add(SVMLexer.SYM_MOV); add(SVMLexer.SYM_LEFTPAR); add(SVMLexer.SYM_COMMA); add(reg2); }});
		/** {@code mov $1 ( %2} */
		add(new ArrayList<Symbol>() {{ add(SVMLexer.SYM_MOV); add(const1); add(SVMLexer.SYM_LEFTPAR); add(reg2); }});
		/** {@code mov $1, (} */
		add(new ArrayList<Symbol>() {{ add(SVMLexer.SYM_MOV); add(const1); add(SVMLexer.SYM_COMMA); add(SVMLexer.SYM_LEFTPAR); }});
		/** {@code mov ,(label2), %2} */
		add(new ArrayList<Symbol>() {{
			add(SVMLexer.SYM_MOV); add(SVMLexer.SYM_COMMA); add(SVMLexer.SYM_LEFTPAR); add(label2Ref); add(SVMLexer.SYM_RIGHTPAR); add(SVMLexer.SYM_COMMA); add(reg2);
		}});
		/** {@code mov len)label2), %2} */
		add(new ArrayList<Symbol>() {{
			add(SVMLexer.SYM_MOV); add(SVMLexer.SYM_FUNCTION_LEN); add(SVMLexer.SYM_RIGHTPAR); add(label2Ref); add(SVMLexer.SYM_RIGHTPAR); add(SVMLexer.SYM_COMMA); add(reg2);
		}});
		/** {@code mov len((), %2} */
		add(new ArrayList<Symbol>() {{
			add(SVMLexer.SYM_MOV); add(SVMLexer.SYM_FUNCTION_LEN); add(SVMLexer.SYM_LEFTPAR); add(SVMLexer.SYM_LEFTPAR); add(SVMLexer.SYM_RIGHTPAR); add(SVMLexer.SYM_COMMA); add(reg2);
		}});
		/** {@code mov len(label2(, %2} */
		add(new ArrayList<Symbol>() {{
			add(SVMLexer.SYM_MOV); add(SVMLexer.SYM_FUNCTION_LEN); add(SVMLexer.SYM_LEFTPAR); add(label2Ref); add(SVMLexer.SYM_LEFTPAR); add(SVMLexer.SYM_COMMA); add(reg2);
		}});
		/** {@code mov len(label2)( %2} */
		add(new ArrayList<Symbol>() {{
			add(SVMLexer.SYM_MOV); add(SVMLexer.SYM_FUNCTION_LEN); add(SVMLexer.SYM_LEFTPAR); add(label2Ref); add(SVMLexer.SYM_RIGHTPAR); add(SVMLexer.SYM_LEFTPAR); add(reg2);
		}});
		/** {@code mov len(label2), (} */
		add(new ArrayList<Symbol>() {{
			add(SVMLexer.SYM_MOV); add(SVMLexer.SYM_FUNCTION_LEN); add(SVMLexer.SYM_LEFTPAR); add(label2Ref); add(SVMLexer.SYM_RIGHTPAR); add(SVMLexer.SYM_COMMA); add(SVMLexer.SYM_LEFTPAR);
		}});
	}};

	/** Instruktion {@code mov $1, %2} */
	@SuppressWarnings("serial")
	public List<Symbol> movConst1Reg2 = new ArrayList<Symbol>() {{ add(SVMLexer.SYM_MOV); add(const1); add(SVMLexer.SYM_COMMA); add(reg2); }};

	/** Instruktion {@code mov $500, %2} */
	@SuppressWarnings("serial")
	public List<Symbol> movConst500Reg2 = new ArrayList<Symbol>() {{ add(SVMLexer.SYM_MOV); add(const500); add(SVMLexer.SYM_COMMA); add(reg2); }};

	/** Instruktion {@code mov %1, %2} */
	@SuppressWarnings("serial")
	public List<Symbol> movReg1Reg2 = new ArrayList<Symbol>() {{ add(SVMLexer.SYM_MOV); add(reg1); add(SVMLexer.SYM_COMMA); add(reg2); }};

	/** Instruktion {@code mov len(label1), %2} */
	@SuppressWarnings("serial")
	public List<Symbol> movLenLabel1Reg2 = new ArrayList<Symbol>() {{
		add(SVMLexer.SYM_MOV); add(SVMLexer.SYM_FUNCTION_LEN); add(SVMLexer.SYM_LEFTPAR); add(label1Ref); add(SVMLexer.SYM_RIGHTPAR); add(SVMLexer.SYM_COMMA); add(reg2);
	}};

	/** Instruktion {@code mov len(label2), %1} */
	@SuppressWarnings("serial")
	public List<Symbol> movLenLabel2Reg1 = new ArrayList<Symbol>() {{
		add(SVMLexer.SYM_MOV); add(SVMLexer.SYM_FUNCTION_LEN); add(SVMLexer.SYM_LEFTPAR); add(label2Ref); add(SVMLexer.SYM_RIGHTPAR); add(SVMLexer.SYM_COMMA); add(reg1);
	}};

	/** Instruktion {@code mov (label2), %2} */
	@SuppressWarnings("serial")
	public List<Symbol> mvRefLabel2Reg2 = new ArrayList<Symbol>() {{
		add(SVMLexer.SYM_MOV); add(SVMLexer.SYM_LEFTPAR); add(label2Ref); add(SVMLexer.SYM_RIGHTPAR); add(SVMLexer.SYM_COMMA); add(reg2);
	}};


	/** SVM-Programm Datenbereich initialisieren */
	@BeforeAll
	public static void init() throws SVMException {
		svmProgram.addData(new Data<Short>(label1, new Short[] {'a', 'b', 'c'}));
		svmProgram.addData(new Data<Short>(label2, new Short[] {1, 2}));
		svmProgram.addInstruction(new VirtualInstruction<Short>(null, new InstructionDefinition<Short>(InstructionFactory.INT, new byte[] {1}, null), new Label[] {null}));
		svmProgram.validate();
	}

	/** Bei Null-LineInfo definierte Meldung schreiben */
	@Test
	public void testeNullParameterLineInfo() {
		var ex = assertThrows(NullPointerException.class, () -> mov.getVirtualInstruction(null, null, null));
		assertThat(ex.getLocalizedMessage(), containsString("lineInfo"));
	}

	/** Bei Null-SVM-Programm definierte Meldung schreiben */
	@Test
	public void testeNullParameterSvmProgram() {
		var ex = assertThrows(NullPointerException.class, () -> mov.getVirtualInstruction(null, new LineInfo(1, "", mvLenLabel1Reg2), null));
		assertThat(ex.getLocalizedMessage(), containsString("svmProgram"));
	}

	/** Die Instruktion Mov erwartet vier/sieben Parameter */
	@Test
	public void testeFalscheZahlParameter() {
		var ex = assertThrows(SVMException.class, () -> mov.getVirtualInstruction(null, new LineInfo(1, "", invalidInstruction), null));
		assertThat(ex.getLocalizedMessage(), containsString("MOV erwartet drei, fünf bzw. sechs Parameter"));
	}

	/** Die Instruktion Mov wird erwartet */
	@Test
	public void testeFalscheInstruktion() {
		var ex = assertThrows(SVMException.class, () -> mov.getVirtualInstruction(null, new LineInfo(1, "", notMovInstruction), null));
		assertThat(ex.getLocalizedMessage(), containsString("MOV erwartet Symbol Symbol[token=CODE, value=mov]"));
	}

	/** Ungültige Typen in der Parameterliste für die Instruktion Mov */
	@Test
	public void testeFalscheParameterFuerInstruktion() {
		invalidInstructionList.stream().forEach(symbols -> {
			var ex = assertThrows(SVMException.class, () -> mov.getVirtualInstruction(null, new LineInfo(1, "", symbols), null));
			assertThat(ex.getLocalizedMessage(), containsString("Erhaltenes Token:"));
		});
	}

	/** Prüft die Instruktion {@linkplain #movConst1Reg2} mit Label {@code abc} */
	@Test
	public void testeMovConst1Reg2() throws SVMException {
		var i = mov.getVirtualInstruction(new Symbol(Token.LABEL, "abc"), new LineInfo(1, "", movConst1Reg2), svmProgram);
		assertThat(i.label().label(), equalTo("abc"));
		assertThat(i.labelList(), equalTo(new Label[] { null, null, null, null, null }));
		assertThat(i.instruction().lenInWords(), equalTo(null));
		assertThat(i.instruction().instruction(), equalTo(InstructionFactory.MOV));
		assertThat(i.instruction().params(), equalTo(new byte[] { 0x21, 0, 0x1, 0, 0x2}));
	}

	/** Prüft die Instruktion {@linkplain #movConst500Reg2} ohne Label */
	@Test
	public void testeMovConst500Reg2() throws SVMException {
		var i = mov.getVirtualInstruction(null, new LineInfo(1, "", movConst500Reg2), svmProgram);
		assertThat(i.label(), equalTo(null));
		assertThat(i.instruction().params(), equalTo(new byte[] { (byte) 0x21, (byte) 1, (byte) 0xf4, (byte) 0, (byte) 0x2}));
	}

	/** Prüft die Instruktion {@linkplain #movReg1Reg2} ohne Label */
	@Test
	public void testeMovRegReg2() throws SVMException {
		var i = mov.getVirtualInstruction(null, new LineInfo(1, "", movReg1Reg2), svmProgram);
		assertThat(i.instruction().params(), equalTo(new byte[] { 0x11, 0, 0x1, 0, 0x2}));
	}

	/** Prüft die Instruktion {@linkplain #movReg1Reg2} Länge 3 (a, b, c) */
	@Test
	public void testeMovLenLabel1Reg2() throws SVMException {
		var i = mov.getVirtualInstruction(null, new LineInfo(1, "", movLenLabel1Reg2), svmProgram);
		assertThat(i.instruction().params(), equalTo(new byte[] { 0x21, 0, 0x3, 0, 0x2}));
	}

	/** Prüft die Instruktion {@linkplain #movReg2Reg1} Länge 2 (1, 2) */
	@Test
	public void testeMovLenLabel2Reg1() throws SVMException {
		var i = mov.getVirtualInstruction(null, new LineInfo(1, "", movLenLabel2Reg1), svmProgram);
		assertThat(i.instruction().params(), equalTo(new byte[] { 0x21, 0, 0x2, 0, 0x1}));
	}

	/** Prüft die Instruktion {@linkplain #mvRefLabel2Reg2} Labelref "label2" */
	@Test
	public void testeMvLenLabel1Reg2() throws SVMException {
		var i = mov.getVirtualInstruction(new Symbol(Token.LABEL, "abc"), new LineInfo(1, "", mvRefLabel2Reg2), svmProgram);
		assertThat(i.label().label(), equalTo("abc"));
		assertThat(i.labelList(), equalTo(new Label[] { null, new Label(LabelType.DATA, label2.label()), null, null, null }));
		assertThat(i.instruction().lenInWords(), equalTo(null));
		assertThat(i.instruction().instruction(), equalTo(InstructionFactory.MOV));
		assertThat(i.instruction().params(), equalTo(new byte[] { 0x21, 0, 0, 0, 0x2})); // mv $0 (wird ersetzt duech Labelref) , %2
	}

}
