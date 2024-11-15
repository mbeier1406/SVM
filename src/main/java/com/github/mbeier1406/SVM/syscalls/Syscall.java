package com.github.mbeier1406.SVM.syscalls;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.mbeier1406.SVM.ALU;
import com.github.mbeier1406.SVM.MEM;

/**
 * Kennzeichnet eine Klasse, die einen Parameter des Systemaufrufs der Instruktion
 * {@linkplain com.github.mbeier1406.SVM.instructions.Syscall} repräsentiert.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Syscall {

	/**
	 * Hier wird der Code des Parameters der Instruktion {@linkplain com.github.mbeier1406.SVM.instructions.Syscall}
	 * angegeben. Dieser wird hier mit {@linkplain Short} angegeben, was voraussetzt, dass die {@linkplain ALU} und
	 * das {@linkplain MEM} ebenfalls mit diesem Datentyp typisiert wird!
	 */
	public short code() default 0x0;

}
