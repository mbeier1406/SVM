package com.github.mbeier1406.svm.impl;

import java.net.MalformedURLException;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.cmd.SVMCli;
import com.github.mbeier1406.svm.cmd.SVMCliImpl;
import com.github.mbeier1406.svm.prg.SVMProgram;
import com.github.mbeier1406.svm.prg.SVMProgramShort;

/**
 * Startet die CLI zur Bedienung der SVM.
 */
public class SVMShort implements SVM {

	/**
	 * Die interne Darstellung eines SVM-Programms, das in den {@linkplain MEM Speicher}
	 * geladen und in der {@linkplain ALU} ausgeführt werdne kann.
	 */
	private final SVMProgram<Short> svmProgram = new SVMProgramShort();

	/** {@inheritDoc} */
	@Override
	public void start() throws SVMException {
		new SVMCliImpl(svmProgram).cli();
	}

	/** Start der {@linkplain SVMCli CLI} */
	public static void main(String[] args) throws MalformedURLException, SVMException {
		new SVMShort().start();
	}

}
