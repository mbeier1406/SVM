package com.github.mbeier1406.svm.cmd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Zeichnet die Klassen aus, die ein Kommandor der {@linkplain SVMCli} bilden.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	/** Liefert das Kommando, das in der CLI eingegeben werden muss */
	public String command() default "";
	public String[] aliases() default {};

}
