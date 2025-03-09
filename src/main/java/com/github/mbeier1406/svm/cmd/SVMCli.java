package com.github.mbeier1406.svm.cmd;

import java.io.InputStream;
import java.io.OutputStream;

import com.github.mbeier1406.svm.SVM;

/**
 * Definiert das Command Line Interface (CLI), mit dem die {@linkplain SVM} gesteuert werden kann.
 */
public interface SVMCli {

	/**
	 * Startet einen Prompt und liest ein SVM-Kommando ein und führt es aus.
	 * Valide Kommandos werden über {@linkplain CommandInterface} definiert.
	 * @param is Der Stream, aus dem die Kommandos gelesen werden.
	 * @param os Der Stream, in den der Promp (und ggf. die Ausgaben der Kommandos) geschrieben werdne sollen
	 * @see {@linkplain Command}
	 */
	public void cli(final InputStream is, final OutputStream os);

	/** @see {@linkplain #cli(InputStream,OutputStream)} */
	public default void cli() {
		cli(System.in, System.out);
	}

}
