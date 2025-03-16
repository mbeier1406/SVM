package com.github.mbeier1406.svm.cmd;

import java.util.NoSuchElementException;
import java.util.Scanner;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.prg.SVMProgram;

/**
 * Beendet die {@linkplain SVMCliImpl}.
 */
@Help(shortHelp="Stellt den Loglevel ein", longHelp="loglevel <level> (INFO, ERROR usw.)")
@Command(command="loglevel", aliases={"ll"})
public class LogLevel extends CommandBase implements CommandInterface {

	/** {@inheritDoc} */
	@Override
	public <T> String exec(final Scanner scanner, final ALU<T> alu, final SVMProgram<T> svmProgram) {
		try {
			String level = scanner.next();
			Level logLevel = Level.getLevel(level);
			if ( logLevel == null ) return "Unbekannter Loglevel: "+level;
			Configurator.setRootLevel(logLevel);
			return "OK " + logLevel;
		}
		catch ( NoSuchElementException e) {
			return "Kein Loglevel angegeben!";
		}
	}

}
