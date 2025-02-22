package com.github.mbeier1406.svm.instructions;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.reflections.Reflections;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;

public class InstructionFactory {

	/**
	 * Diese Map enthält alle {@linkplain InstructionInterface Maschinenbefehle} (Instructions) mit deren
	 * {@linkplain Instruction#code() Code}	im {@linkplain MEM}, die von der {@linkplain ALU}
	 * ausgeführt werden können.
	 */
	public transient static final Map<Byte, InstructionInterface<Short>> INSTRUCTIONS;

	/** Lädt die definierten {@linkplain InstructionInterface Maschinenbefehle} */
	static {
		INSTRUCTIONS = getInstructions();
		NOP = INSTRUCTIONS.get(Nop.CODE);
		MOV = INSTRUCTIONS.get(Mov.CODE);
		INT = INSTRUCTIONS.get(Int.CODE);
	}

	/** Die Instruktion {@linkplain Nop} */
	public static final InstructionInterface<Short> NOP;

	/** Die Instruktion {@linkplain Mov} */
	public static final InstructionInterface<Short> MOV;

	/** Die Instruktion {@linkplain Int} */
	public static final InstructionInterface<Short> INT;


	/** Lädt alle Maschinenbefehle/Insructions, die die {@linkplain ALU} ausführen kann */
	public static Map<Byte, InstructionInterface<Short>> getInstructions() {
		try {
			Set<Class<?>> instructionClasses = new Reflections("com.github.mbeier1406.svm.instructions").getTypesAnnotatedWith(Instruction.class);
			final Map<Byte, InstructionInterface<Short>> instructions = new HashMap<>();
			instructionClasses.forEach(instructionClass -> {
				try {
					@SuppressWarnings("unchecked")
					InstructionInterface<Short> instruction = (InstructionInterface<Short>) instructionClass.getConstructor().newInstance();
					instructions.put(instructionClass.getAnnotation(Instruction.class).code(), instruction);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new RuntimeException("instructionClass="+instructionClass, e);
				}
			});
			return instructions;
		}
		catch ( Exception e ) {
			throw new RuntimeException("Instructions können nicht per Refelction ermittelt werden!", e);
		}
	}

	/** Ermöglicht den Zugriff auf den Hauptspeicher für die Systemaufrufe */
	public static void init(final ALU.Instruction<Short> alu, final MEM.Instruction<Short> mem) {
		INSTRUCTIONS.entrySet().forEach(s -> {
			s.getValue().setAlu(Objects.requireNonNull(alu, "alu"));
			s.getValue().setMemory(Objects.requireNonNull(mem, "mem"));
		});
	}

}
