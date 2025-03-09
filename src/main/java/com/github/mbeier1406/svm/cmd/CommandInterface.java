package com.github.mbeier1406.svm.cmd;

import java.util.Scanner;

import com.github.mbeier1406.svm.SVM;

/**
 * Definiert die Kommandos, die in der {@linkplain SVM}-Cli eingegeben werdne können.
 */
@FunctionalInterface
public interface CommandInterface {

	/** Das Java-Package. in dem sich die Kommandos für die SVM-Cli befinden ist {@value} */
	public static final String PACKAGE = CommandInterface.class.getPackageName();

	/** Führt das in der CLI eingegebene Kommando aus */
	public String exec(final Scanner scanner);

}
