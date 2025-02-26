package com.github.mbeier1406.svm.prg.parser.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
		add(new Data<Short>(new Label(LabelType.DATA, "test1"), new Short[] {1, 2, 3}));
		add(new Data<Short>(new Label(LabelType.DATA, "test2"), new Short[] {'a', 'b', 'c'}));
		add(new Data<Short>(new Label(LabelType.DATA, "test3"), new Short[] {4, 5}));
	}};

	/** {@linkplain #svmProgram} initialisieren */
	@BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    	when(svmProgram.getDataList()).thenReturn(dataList);
    }

    @Test
    public void testeLen() throws SVMException {
    	assertEquals(2, FunctionFactory.getFunction("len").apply(new Symbol(Token.LABEL_REF, "test3"), svmProgram));
    }

    /*
     * Teste Funktion null FunctionFactory.getFunction("xyz")
     * Teste ungültigen Label
     * Teste NULL Werte in getFunction und den Funktionen
     * Repariere die Tests in instructions Parser
     * Fehlendes Javadoc
     */
}
