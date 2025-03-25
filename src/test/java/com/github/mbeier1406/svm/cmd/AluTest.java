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

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_INIT}</i> die richtige Meldung liefert */
	@Test
	public void testeExecInit() {
		String erg = alu.exec(new Scanner(Alu.CMD_INIT), aluShort, null);
		assertTrue(erg.equals("OK"));
		verify(aluShort).init();
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_START}</i> die richtige Meldung liefert */
	@Test
	public void testeExecStartOhneFehler() throws SVMException {
		String erg = alu.exec(new Scanner(Alu.CMD_START), aluShort, null);
		assertTrue(erg.equals("OK"));
		verify(aluShort).start();
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_START}</i> mit Exception die richtige Meldung liefert */
	@Test
	public void testeExecStartMitFehler() throws SVMException {
		when(aluShort.start()).thenThrow(new SVMException("XXX"));
		String erg = alu.exec(new Scanner(Alu.CMD_START), aluShort, null);
		assertTrue(erg.equals("Fehler: XXX"));
		verify(aluShort).start();
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_SET_STOP_FLAG}</i> die richtige Meldung liefert */
	@Test
	public void testeExecSetStopFlag() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_SET_STOP_FLAG), aluShort, null);
		assertTrue(erg.equals("OK"));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_SET_REG}</i> ohne Argumente die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetRegOhneArgument() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_SET_REG), aluShort, null);
		assertTrue(erg.equals("Register erwartet: "+Alu.USAGE_SET_REG));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_SET_REG}</i> mit nur einem Argument die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetRegNurEinArgument() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_SET_REG+" 1"), aluShort, null);
		assertTrue(erg.equals("Wert erwartet: "+Alu.USAGE_SET_REG));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_SET_REG}</i> die richtige Meldung liefert */
	@Test
	public void testeExecSetReg() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_SET_REG+" 1 2"), aluShort, null);
		assertTrue(erg.equals("OK"));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_SET_REG}</i> mit Argumnet "keine Zahl" die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetRegKeineZahl1() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_SET_REG+" a 2"), aluShort, null);
		assertTrue(erg.equals("Ungültige Zahl: For input string: \"a\""));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_SET_REG}</i> mit Argumnet "keine Zahl" die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetRegKeineZahl2() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_SET_REG+" 1 b"), aluShort, null);
		assertTrue(erg.equals("Ungültige Zahl: For input string: \"b\""));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_SET_REG}</i> mit Exception richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetRegException() throws SVMException {
		when(aluShort.getInstructionInterface()).thenReturn(aluInstruction);
		doThrow(new SVMException("XXX")).when(aluInstruction).setRegisterValue(1, (short) 2);
		String erg = alu.exec(new Scanner(Alu.CMD_SET_REG+" 1 2"), aluShort, null);
		assertTrue(erg.equals("XXX"));
		verify(aluInstruction).setRegisterValue(1, (short) 2);
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_READ_REG}</i> ohne Argumente die richtige Fehlermeldung liefert */
	@Test
	public void testeExecGetRegOhneArgument() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_READ_REG), aluShort, null);
		assertTrue(erg.equals("Register erwartet: "+Alu.USAGE_READ_REG));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_READ_REG}</i> mit Argumnet "keine Zahl" die richtige Fehlermeldung liefert */
	@Test
	public void testeExecGetRegKeineZahl() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_READ_REG+" a"), aluShort, null);
		assertTrue(erg.equals("Ungültige Zahl: For input string: \"a\""));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_READ_REG}</i> die richtige Meldung liefert */
	@Test
	public void testeExecGetReg() throws SVMException {
		when(aluShort.getInstructionInterface()).thenReturn(aluInstruction);
		when(aluInstruction.getRegisterValue(1)).thenReturn((short) 27);
		String erg = alu.exec(new Scanner(Alu.CMD_READ_REG+" 1"), aluShort, null);
		assertTrue(erg.equals("OK: 27"));
		verify(aluInstruction).getRegisterValue(1);
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_READ_REG}</i> mit Exception richtige Fehlermeldung liefert */
	@Test
	public void testeExecGetRegException() throws SVMException {
		when(aluShort.getInstructionInterface()).thenReturn(aluInstruction);
		doThrow(new SVMException("XXX")).when(aluInstruction).getRegisterValue(1);
		String erg = alu.exec(new Scanner(Alu.CMD_READ_REG+" 1"), aluShort, null);
		assertTrue(erg.equals("XXX"));
		verify(aluInstruction).getRegisterValue(1);
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_SET_MEM}</i> ohne Argumente die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetMemOhneArgument() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_SET_MEM), aluShort, null);
		assertTrue(erg.equals("Speicheradresse erwartet: "+Alu.USAGE_SET_MEM));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_SET_MEM}</i> mit nur einem Argument die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetMemNurEinArgument() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_SET_MEM+" 1"), aluShort, null);
		assertTrue(erg.equals("Wert erwartet: "+Alu.USAGE_SET_MEM));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_SET_MEM}</i> die richtige Meldung liefert */
	@Test
	public void testeExecSetMem() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_SET_MEM+" 1 2"), aluShort, null);
		assertTrue(erg.equals("OK"));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_SET_MEM}</i> mit Argument 1 "keine Zahl" die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetMemKeineZahl1() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_SET_MEM+" a 2"), aluShort, null);
		assertTrue(erg.equals("Ungültige Zahl: For input string: \"a\""));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_SET_MEM}</i> mit Argument 2 "keine Zahl" die richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetMemKeineZahl2() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_SET_MEM+" 1 b"), aluShort, null);
		assertTrue(erg.equals("Ungültige Zahl: For input string: \"b\""));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_SET_MEM}</i> mit Exception richtige Fehlermeldung liefert */
	@Test
	public void testeExecSetMemException() throws SVMException {
		when(aluShort.getMEM()).thenReturn(mem);
		when(mem.getInstructionInterface()).thenReturn(memInstruction);
		doThrow(new SVMException("XXX")).when(memInstruction).write(1, (short) 2);
		String erg = alu.exec(new Scanner(Alu.CMD_SET_MEM+" 1 2"), aluShort, null);
		assertTrue(erg.equals("XXX"));
		verify(memInstruction).write(1, (short) 2);
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_READ_MEM}</i> ohne Argumente die richtige Fehlermeldung liefert */
	@Test
	public void testeExecReadMemOhneArgument() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_READ_MEM), aluShort, null);
		assertTrue(erg.equals("Speicheradresse erwartet: "+Alu.USAGE_READ_MEM));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_READ_MEM}</i> mit Argument "keine Zahl" die richtige Fehlermeldung liefert */
	@Test
	public void testeExecreadMemKeineZahl() {
		aluShort = new ALUShort(new MEMShort());
		String erg = alu.exec(new Scanner(Alu.CMD_READ_MEM+" a"), aluShort, null);
		assertTrue(erg.equals("Ungültige Zahl: For input string: \"a\""));
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_READ_MEM}</i> die richtige Meldung liefert */
	@Test
	public void testeExecReadMem() throws SVMException {
		when(aluShort.getMEM()).thenReturn(mem);
		when(mem.getInstructionInterface()).thenReturn(memInstruction);
		when(memInstruction.read(1)).thenReturn((short) 27);
		String erg = alu.exec(new Scanner(Alu.CMD_READ_MEM+" 1"), aluShort, null);
		assertTrue(erg.equals("OK: 27"));
		verify(memInstruction).read(1);
	}

	/** Prüft, ob Parameter <i>{@linkplain Alu#CMD_READ_MEM}</i> mit Exception richtige Fehlermeldung liefert */
	@Test
	public void testeExecreadMemException() throws SVMException {
		when(aluShort.getMEM()).thenReturn(mem);
		when(mem.getInstructionInterface()).thenReturn(memInstruction);
		doThrow(new SVMException("XXX")).when(memInstruction).read(1);
		String erg = alu.exec(new Scanner(Alu.CMD_READ_MEM+" 1"), aluShort, null);
		assertTrue(erg.equals("XXX"));
		verify(memInstruction).read(1);
	}

}
