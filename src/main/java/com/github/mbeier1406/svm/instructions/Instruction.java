package com.github.mbeier1406.svm.instructions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;

/**
 * Kennzeichnet eine Klasse, die einen von der {@linkplain ALU} direkt
 * ausführbaren Maschinenbefehl repräsentiert.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Instruction {

	/**
	 * Jeder Maschinenbefehl hat einen eigenen, ein Byte langen, Code.
	 * Dieser bildet immer das erste Byte eines Speicherworts im
	 * Hauptspeicher {@linkplain MEM}.
	 * @return den Code der Instruktion
	 */
	public byte code() default 0x0;

}
