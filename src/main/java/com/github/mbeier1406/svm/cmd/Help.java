package com.github.mbeier1406.svm.cmd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Zeichnet die mit {@linkplain Command} annotierten Klassen aus
 * und gibt Hilfestellung für ihre Verwendung.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Help {

	/** Zeigt den Zweck und die Syntax eine Kommandos an */
	public String shortHelp() default "";

	/** Zeigt den Zweck und die Syntax eine Kommandos an und gibt Hinweise zur Benutzung  */
	public String longHelp() default "";

}
