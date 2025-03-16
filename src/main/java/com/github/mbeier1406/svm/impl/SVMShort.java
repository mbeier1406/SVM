package com.github.mbeier1406.svm.impl;

import java.net.MalformedURLException;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.cmd.SVMCli;
import com.github.mbeier1406.svm.cmd.SVMCliImpl;
import com.github.mbeier1406.svm.instructions.InstructionFactory;
import com.github.mbeier1406.svm.prg.SVMLoader;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgramShort;
import com.github.mbeier1406.svm.syscalls.SyscallFactory;

/**
 * Startet die CLI zur Bedienung der SVM.
 */
public class SVMShort implements SVM {

	/** Der Hauptspecher der {@linkplain SVM}, in den die Daten und interne Darstellung eines {@linkplain SVMProgram} geladen werden */
	public MEMShort mem;

	/** Die ALU der {@linkplain SVM} fürhrt die {@linkplain SVMLoader geladenen PRG-Programme} (interne Darstellung eines {@linkplain SVMProgram} aus */
	public ALUShort alu;

	/**
	 * Die interne Darstellung eines SVM-Programms, das in den {@linkplain MEM Speicher}
	 * geladen und in der {@linkplain ALU} ausgeführt werden kann.
	 */
	private SVMProgram<Short> svmProgram = new SVMProgramShort();

	/** {@inheritDoc} */
	@Override
	public void cli() throws SVMException {
		this.mem = new MEMShort();
		this.alu = new ALUShort(mem);
		this.alu.init();
		InstructionFactory.init(alu, mem);
		SyscallFactory.init(alu, mem);
		this.svmProgram = new SVMProgramShort();
		new SVMCliImpl(this.alu, this.svmProgram).cli();
	}

	/** Start der {@linkplain SVMCli CLI} */
	public static void main(String[] args) throws MalformedURLException, SVMException {
		new SVMShort().cli();
	}

}
