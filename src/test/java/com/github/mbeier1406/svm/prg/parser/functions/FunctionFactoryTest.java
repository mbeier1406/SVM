package com.github.mbeier1406.svm.prg.parser.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;
import com.github.mbeier1406.svm.prg.SVMProgram.LabelType;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Symbol;
import com.github.mbeier1406.svm.prg.lexer.SVMLexer.Token;

/**
 * Tests für die Klasse {@linkplain FunctionFactory}.
 */
public class FunctionFactoryTest {

	/** Das Programm, das gerade erstellt wird */
	@Mock
	public SVMProgram<Short> svmProgram;

	/** Liefer die Daten für {@linkplain #svmProgram} */
	@SuppressWarnings("serial")
	public List<Data<Short>> dataList = new ArrayList<Data<Short>>() {{
		add(new Data<Short>(new Label(LabelType.DATA, "test1"), new Short[] {1, 2, 3}, null));
		add(new Data<Short>(new Label(LabelType.DATA, "test2"), new Short[] {'a', 'b', 'c'}, null));
		add(new Data<Short>(new Label(LabelType.DATA, "test3"), new Short[] {4, 5}, null));
	}};

	/** {@linkplain #svmProgram} initialisieren */
	@BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    	when(svmProgram.getDataList()).thenReturn(dataList);
    }

    /** Stellt sicher, dass eine definiert Fehlermeldung ausgegeben wird, wenn die Funktion fehlt */
    @Test
    public void testeNullFunction() {
    	var ex = assertThrows(NullPointerException.class, () -> FunctionFactory.getFunction(null));
    	assertTrue(ex.getLocalizedMessage().contains("Keine Funktion angegeben"));
    }

    /** Stellt sicher, dass eine definiert Fehlermeldung ausgegeben wird, wenn eine unbekannte Funktion aufgerufen wird */
    @Test
    public void testeIllegalFunction() {
    	var ex = assertThrows(SVMException.class, () -> FunctionFactory.getFunction("xyz"));
    	assertTrue(ex.getLocalizedMessage().contains("Keine Funktion 'xyz' definiert"));
    }

	/** Funktion {@code len(}): Die Datendefinition für Label <i>test3</i> ist zwei Speicherwörter lang */
    @Test
    public void testeLen() throws SVMException {
    	assertEquals(2, FunctionFactory.getFunction("len").apply(new Symbol(Token.LABEL_REF, "test3"), svmProgram));
    }

	/** Funktion {@code len(}): Die Datendefinition für Label <i>xyz</i> gibt es nicht */
    @Test
    public void testeLenIllegalLabel() {
    	var ex = assertThrows(SVMException.class, () -> FunctionFactory.getFunction("len").apply(new Symbol(Token.LABEL_REF, "xyz"), svmProgram));
    	assertTrue(ex.getLocalizedMessage().contains("symbol=Symbol[token=LABEL_REF, value=xyz]: den Label gibt es nicht"));
    }

	/** Funktion {@code len(}): Es wird {@linkplain Token#LABEL_REF} im Symbol erwartet */
    @Test
    public void testeLenIllegalSymbol() {
    	var ex = assertThrows(SVMException.class, () -> FunctionFactory.getFunction("len").apply(SVMLexer.SYM_COMMA, svmProgram));
    	assertTrue(ex.getLocalizedMessage().contains("es wird ein Symbol mit folgendem Token erwartet: LABEL_REF"));
    }

	/** Funktion {@code len(}): Fehler prüfen bei <b>null</b> als {@linkplain Symbol} */
    @Test
    public void testeLenNullSymbol() {
    	var ex = assertThrows(NullPointerException.class, () -> FunctionFactory.getFunction("len").apply(null, svmProgram));
    	assertTrue(ex.getLocalizedMessage().contains("symbol"));
    }

	/** Funktion {@code len(}): Fehler prüfen bei <b>null</b> als {@linkplain SVMProgram} */
    @Test
    public void testeLenNullSVMProgram() {
    	var ex = assertThrows(NullPointerException.class, () -> FunctionFactory.getFunction("len").apply(new Symbol(Token.LABEL_REF, "test3"), null));
    	assertTrue(ex.getLocalizedMessage().contains("svmProgram"));
    }

}
