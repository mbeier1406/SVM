package com.github.mbeier1406.svm.cmd;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.prg.SVMProgram;

/**
 * Steuert die {@linkplain SVM} über eingegebene Kommandos.
 */
public class SVMCliImpl implements SVMCli {

	/** Die interne Darstellung des SVM-Programms, das ausgeführt werden soll */
	private final SVMProgram<Short> svmProgram;

	public SVMCliImpl(SVMProgram<Short> svmProgram) {
		this.svmProgram = svmProgram;
	}
	
	/** {@inheritDoc} */
	@Override
	public void cli(InputStream is, OutputStream os) {
		PrintWriter out = new PrintWriter(os);
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		out.println("SVM Cli wird gestartet...");
		String ausgabe = "";
		while ( !ausgabe.equals(Ende.ENDE) ) {
			try {
				out.print("--> "); out.flush();
				String str = in.readLine();
				try ( Scanner s = new Scanner(str) ) {
					String cmdStr = s.next();
					var cmd = CommandFactory.COMMANDS.get(cmdStr);
					if ( cmd == null ) {
						s.close();
						throw new SVMException("Kommando '"+cmdStr+"' existiert nicht: "+str);
					}
					ausgabe = cmd.exec(s, this.svmProgram);
					if ( s != null && !ausgabe.isBlank() && !ausgabe.equals(Ende.ENDE) )
						out.print(ausgabe+"\n");
				}
			} catch ( Exception e ) {
				out.println("Fehler: " + e.getLocalizedMessage());
			}
		}
		out.println("SVM Cli wird beendet."); out.flush();
	}

}
