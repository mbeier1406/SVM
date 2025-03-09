package com.github.mbeier1406.svm.cmd;

/**
 * Enthält die Basisfunktionen für alle Kommandos.
 */
public class CommandBase {

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object anotherObject) {
		return anotherObject != null && this.getClass().getName().equals(anotherObject.getClass().getName());
	}

}
