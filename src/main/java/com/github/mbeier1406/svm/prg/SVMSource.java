package com.github.mbeier1406.svm.prg;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.github.mbeier1406.svm.SVMException;

/**
 * Dieses Interface definiert die Methoden zum Speichern oder Laden eines
 * {@linkplain SVMProgram}s in interner Darstellung an/von einem vorgegebenen
 * Speicherort.
 * @param <T>
 */
public interface SVMSource<T> {

	/**
	 * Speichert ein vorhandenes SVM-Programm (in interner Darstellung) an einem vorgegebenen Ort.
	 * @param program das SVM-Programm
	 * @param target der Speicherort
	 * @throws SVMException bei technischen Problemen (zB fehlende Schreibrechte usw.)
	 */
	public void save(final SVMProgram<T> program, final File target) throws SVMException;

	/** @see #save(SVMProgram, File) */
	public default void save(final SVMProgram<T> program, final String target) throws SVMException {
		save(program, new File(target));
	}

	/**
	 * Lädt ein vorhandenes SVM-Programm (in interner Darstellung) von einem vorgegebenen Ort.
	 * @param source der Speicherort
	 * @returndas SVM-Programm
	 * @throws SVMException bei technischen Problemen (zB Quelle nicht vorhanden, kein Leserecht usw.)
	 */
	public SVMProgram<T> load(final URL source) throws SVMException;

	/** @see #load(URL) */
	public default SVMProgram<T> load(final File source) throws SVMException {
		try {
			return load(source.toURI().toURL());
		} catch (MalformedURLException | SVMException e) {
			throw new SVMException("file="+source, e);
		}
	}

	/** @see #load(File) */
	public default SVMProgram<T> load(final String source) throws SVMException {
		return load(new File(source));
	}

}
