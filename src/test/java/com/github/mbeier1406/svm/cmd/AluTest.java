package com.github.mbeier1406.svm.cmd;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.ALU.Instruction;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.impl.ALUShort;
import com.github.mbeier1406.svm.impl.MEMShort;

/**
 * Tests für die Klasse {@linkplain Alu}.
 */
@ExtendWith(MockitoExtension.class)
public class AluTest {

	@Mock
	public ALU<Short> aluShort;

	@Mock
	public Instruction<Short> aluInstruction;

	@Mock
	public MEM<Short> mem;

	@Mock
	public MEM.Instruction<Short> memInstruction;

	/** Das zu testende Objekt */
	public Alu alu = new Alu();

	/** Prüft, ob ohne Parameter die richtige Meldung geliefert wird */
	@Test
	public void testeExecOhneParameter() {
		String erg = alu.exec(new Scanner(""), null, null);
		assertTrue(erg.equals(Alu.USAGE));
	}

	/** Prüft, ob Parameter <i>init</i> die richtige Meldung liefert */
	@Test
	public void testeExecInit() {
		String erg = alu.exec(new Scanner(" init"), aluShort, null);
		assertTrue(erg.equals("OK"));
		verify(aluShort).init();
	}

	/** Prüft, ob Parameter <i>start</i> die richtige Meldung liefert */
	@Test
	public void testeExecStartOhneFehler() throws SVMException {
		String erg = alu.exec(new Scanner("start"), aluShort, null);
		assertTrue(erg.equals("OK"));
		verify(aluShort).start();
	}

	/** Prüft, ob Parameter <i>start</i> mit Exception die richtige Meldung liefert */
	@Test
	public void testeExecStartMitFehler() throws SVMException {
		when(aluShort.start()).thenThrow(new SVMException("XXX"));
		String erg = alu.exec(new Scanner("start"), aluShort, null);
		assertTrue(erg.equals("Fehler: XXX"));
		verify(aluShort).start();
	}

	/** Prüft, ob Parameter <i>set_stop_flag</i> die richtige Meldung liefert */
	@Test
	public void testeExecSetStopFlag() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner("set_stop_flag"), aluShort, null);
		assertTrue(erg.equals("OK"));
	}

	/** Prüft, ob Parameter <i>set_reg</i> ohne Argumente die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetRegOhneArgument() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner("set_reg"), aluShort, null);
		assertTrue(erg.equals("Register erwartet: set_reg <nr> <wert>"));
	}

	/** Prüft, ob Parameter <i>set_reg</i> mit nur einem Argument die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetRegNurEinArgument() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner("set_reg 1"), aluShort, null);
		assertTrue(erg.equals("Wert erwartet: set_reg <nr> <wert>"));
	}

	/** Prüft, ob Parameter <i>set_reg</i> die richtige Meldung liefert */
	@Test
	public void testeExecSetReg() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner("set_reg 1 2"), aluShort, null);
		assertTrue(erg.equals("OK"));
	}

	/** Prüft, ob Parameter <i>set_reg</i> mit Argumnet "keine Zahl" die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetRegKeineZahl1() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner("set_reg a 2"), aluShort, null);
		assertTrue(erg.equals("Ungültige Zahl: For input string: \"a\""));
	}

	/** Prüft, ob Parameter <i>set_reg</i> mit Argumnet "keine Zahl" die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetRegKeineZahl2() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner("set_reg 1 b"), aluShort, null);
		assertTrue(erg.equals("Ungültige Zahl: For input string: \"b\""));
	}

	/** Prüft, ob Parameter <i>set_reg</i> mit Exception richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetRegException() throws SVMException {
		when(aluShort.getInstructionInterface()).thenReturn(aluInstruction);
		doThrow(new SVMException("XXX")).when(aluInstruction).setRegisterValue(1, (short) 2);
		String erg = alu.exec(new Scanner("set_reg 1 2"), aluShort, null);
		assertTrue(erg.equals("XXX"));
		verify(aluInstruction).setRegisterValue(1, (short) 2);
	}

	/** Prüft, ob Parameter <i>get_reg</i> ohne Argumente die richtige Fehlermeldung liefert */
	@Test
	public void testeExecGetRegOhneArgument() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner("get_reg"), aluShort, null);
		assertTrue(erg.equals("Register erwartet: read_reg <nr>"));
	}

	/** Prüft, ob Parameter <i>get_reg</i> mit Argumnet "keine Zahl" die richtige Fehlermeldung liefert */
	@Test
	public void testeExecGetRegKeineZahl() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner("get_reg a"), aluShort, null);
		assertTrue(erg.equals("Ungültige Zahl: For input string: \"a\""));
	}

	/** Prüft, ob Parameter <i>get_reg</i> die richtige Meldung liefert */
	@Test
	public void testeExecGetReg() throws SVMException {
		when(aluShort.getInstructionInterface()).thenReturn(aluInstruction);
		when(aluInstruction.getRegisterValue(1)).thenReturn((short) 27);
		String erg = alu.exec(new Scanner("get_reg 1"), aluShort, null);
		assertTrue(erg.equals("OK: 27"));
		verify(aluInstruction).getRegisterValue(1);
	}

	/** Prüft, ob Parameter <i>get_reg</i> mit Exception richtige Fehlermeldung liefert */
	@Test
	public void testeExecGetRegException() throws SVMException {
		when(aluShort.getInstructionInterface()).thenReturn(aluInstruction);
		doThrow(new SVMException("XXX")).when(aluInstruction).getRegisterValue(1);
		String erg = alu.exec(new Scanner("get_reg 1"), aluShort, null);
		assertTrue(erg.equals("XXX"));
		verify(aluInstruction).getRegisterValue(1);
	}

	/** Prüft, ob Parameter <i>set_mem</i> ohne Argumente die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetMemOhneArgument() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner("set_mem"), aluShort, null);
		assertTrue(erg.equals("Speicheradresse erwartet: set_mem <addr> <wert>"));
	}

	/** Prüft, ob Parameter <i>set_mem</i> mit nur einem Argument die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetMemNurEinArgument() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner("set_mem 1"), aluShort, null);
		assertTrue(erg.equals("Wert erwartet: set_mem <addr> <wert>"));
	}

	/** Prüft, ob Parameter <i>set_mem</i> die richtige Meldung liefert */
	@Test
	public void testeExecSetMem() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner("set_mem 1 2"), aluShort, null);
		assertTrue(erg.equals("OK"));
	}

	/** Prüft, ob Parameter <i>set_mem</i> mit Argument 1 "keine Zahl" die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetMemKeineZahl1() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner("set_mem a 2"), aluShort, null);
		assertTrue(erg.equals("Ungültige Zahl: For input string: \"a\""));
	}

	/** Prüft, ob Parameter <i>set_mem</i> mit Argument 2 "keine Zahl" die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetMemKeineZahl2() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner("set_mem 1 b"), aluShort, null);
		assertTrue(erg.equals("Ungültige Zahl: For input string: \"b\""));
	}

	/** Prüft, ob Parameter <i>set_mem</i> mit Exception richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetMemException() throws SVMException {
		when(aluShort.getMEM()).thenReturn(mem);
		when(mem.getInstructionInterface()).thenReturn(memInstruction);
		doThrow(new SVMException("XXX")).when(memInstruction).write(1, (short) 2);
		String erg = alu.exec(new Scanner("set_mem 1 2"), aluShort, null);
		assertTrue(erg.equals("XXX"));
		verify(memInstruction).write(1, (short) 2);
	}


}
