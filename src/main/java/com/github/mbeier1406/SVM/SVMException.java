package com.github.mbeier1406.SVM;

/**
 * Diese {@linkplain Exception} weist auf einen Fehler bei der Ausführung eines
 * Programms in der {@linkplain SVM} hin.
 */
public class SVMException extends Exception {
	private static final long serialVersionUID = 5239161298266195977L;
	public SVMException() { super(); }
	public SVMException(String msg) { super(msg); }
	public SVMException(Exception e) { super(e); }
	public SVMException(String msg, Exception e) { super(msg, e); }
}
