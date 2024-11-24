package com.github.mbeier1406.svm.syscalls;

import static java.util.Objects.requireNonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.instructions.Int;
import com.github.mbeier1406.svm.instructions.IntInterface;

public class SyscallFactory {

	/**
	 * Diese Map enthält alle {@linkplain SyscallInterface Syscalls} mit derem Code als Wort
	 * im {@linkplain MEM}, die von {@linkplain InstructionInterface Instructions} in der {@linkplain ALU}
	 * ausgeführt werden können.
	 */
	public static final Map<Byte, SyscallInterface<Short>> SYSCALLS;

	/** Lädt die definierten {@linkplain SyscallInterface Syscalls} */
	static {
		SYSCALLS = getSyscalls();
	}

	/** Lädt alle Systemaufrufe für {@linkplain com.github.mbeier1406.Int.instructions.Syscall} */
	public static Map<Byte, SyscallInterface<Short>> getSyscalls() {
		try {
			Set<Class<?>> syscallClasses = new Reflections("com.github.mbeier1406.svm.syscalls").getTypesAnnotatedWith(Syscall.class);
			final Map<Byte, SyscallInterface<Short>> syscalls = new HashMap<>();
			syscallClasses.forEach(syscallClass -> {
				try {
					@SuppressWarnings("unchecked")
					SyscallInterface<Short> syscall = (SyscallInterface<Short>) syscallClass.getConstructor().newInstance();
					syscalls.put(syscallClass.getAnnotation(Syscall.class).code(), syscall);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new RuntimeException("syscallClass="+syscallClass, e);
				}
			});
			return syscalls;
		}
		catch ( Exception e ) {
			throw new RuntimeException("Syscalls können nicht mehr Refelction ermittelt werden!", e);
		}
	}

	/** Ermöglicht den Zugriff auf den Hauptspeicher für die Systemaufrufe */
	public static void init(final ALU.Instruction<Short> alu, final MEM.Instruction<Short> mem) {
		SYSCALLS.entrySet().forEach(s -> {
			s.getValue().setAlu(requireNonNull(alu, "alu"));
			s.getValue().setMemory(requireNonNull(mem, "mem"));
		});
	}

	/** Liefert die Syscalls im Typ für die {@linkplain Int}-Instruktion */
	public static Map<Byte, IntInterface<Short>> toIntMap() {
		Map<Byte, IntInterface<Short>> intMap = new HashMap<>();
		SyscallFactory.SYSCALLS.entrySet().stream().forEach(e -> intMap.put(e.getKey(), e.getValue()));
		return intMap;
	}

}
