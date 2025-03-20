package com.github.mbeier1406.svm.cmd;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Tests für die Klasse {@linkplain Information}.
 */
@ExtendWith(MockitoExtension.class)
public class InformationTest {

	/** MEM für Testausgaben */
	@Mock
	public MEM<Short> mem;

	/** ALU für Testausgaben */
	@Mock
	public ALU<Short> alu;

	/** Das zu testende Objekt */
	public Information info = new Information();

	/** Stellt sicher, dass eine Hilfe bei Benutzung ohne Parameter ausgegeben wird */
	@Test
	public void testeExecOhneParameter() {
		assertTrue(info.exec(new Scanner(""), null, null).equals(Information.USAGE));
	}

	/** Stellt sicher, dass eine Hilfe bei fehlerhafter Benutzung ausgegeben wird */
	@Test
	public void testeExecMitFalschemParameter() {
		assertTrue(info.exec(new Scanner(" yyy"), null, null).contains("yyy"));
	}

	/** Stellt sicher, dass die {@linkplain ALU} korrekt ausgegeben wird */
	@Test
	public void testeExecMitAluParameter() {
		when(alu.toString()).thenReturn("xxx");
		assertTrue(info.exec(new Scanner("alu"), alu, null).equals("xxx"));
	}

	/** Stellt sicher, dass das {@linkplain MEM} korrekt ausgegeben wird */
	@Test
	public void testeExecMitMemParameter() {
		when(mem.toString()).thenReturn("xxx");
		when(alu.getMEM()).thenReturn(mem);
		assertTrue(info.exec(new Scanner("mem"), alu, null).equals("xxx"));
	}

	/** Stellt sicher, dass bei fehlerhafte Zahlen eine passende Meldung ausgegeben wird */
	@Test
	public void testeExecAddrMitUngueltigeZahl1() {
		assertTrue(info.exec(new Scanner("addr a 1"), null, null).equals("Ungültige Zahl: For input string: \"a\""));
	}

	/** Stellt sicher, dass bei fehlerhafte Zahlen eine passende Meldung ausgegeben wird */
	@Test
	public void testeExecAddrMitUngueltigeZahl2() {
		assertTrue(info.exec(new Scanner("addr 1 b"), null, null).equals("Ungültige Zahl: For input string: \"b\""));
	}

	/** Stellt sicher, dass bei fehlenden Zahlen eine passende Meldung ausgegeben wird */
	@Test
	public void testeExecAddrMitFehlenderZahl1() {
		System.out.println(info.exec(new Scanner("addr"), null, null));
		assertTrue(info.exec(new Scanner("addr"), null, null).startsWith("Adresse erwartet"));
	}

	/** Stellt sicher, dass bei fehlenden Zahlen eine passende Meldung ausgegeben wird */
	@Test
	public void testeExecAddrMitFehlenderZahl2() {
		System.out.println(info.exec(new Scanner("addr 1"), null, null));
		assertTrue(info.exec(new Scanner("addr"), null, null).startsWith("Adresse erwartet"));
	}

	/** Stellt sicher, dass bei <i>addr</i> die Aausgabe korrekt ist */
	@Test
	public void testeExecMitAddr() throws SVMException {
		when(mem.getBinaryContentStringAt(1, 2)).thenReturn("xxx");
		when(alu.getMEM()).thenReturn(mem);
		assertTrue(info.exec(new Scanner("addr 1 2"), alu, null).equals("xxx"));
	}

	/** Stellt sicher, dass bei <i>addr</i> die Aausgabe korrekt ist */
	@Test
	public void testeExecMitAddrException() throws SVMException {
		when(mem.getBinaryContentStringAt(1, 2)).thenThrow(new IllegalArgumentException("abc"));
		when(alu.getMEM()).thenReturn(mem);
		assertTrue(info.exec(new Scanner("addr 1 2"), alu, null).equals("abc"));
	}

}
