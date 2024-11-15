package com.github.mbeier1406.SVM.syscalls;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.github.mbeier1406.SVM.ALU;
import com.github.mbeier1406.SVM.MEM;
import com.github.mbeier1406.SVM.instructions.Instruction;

public class SyscallFactory {

	/**
	 * Diese Map enthält alle {@linkplain SyscallInterface Syscalls} mit derem Code als Wort
	 * im {@linkplain MEM}, die von {@linkplain Instruction Instructions} in der {@linkplain ALU}
	 * ausgeführt werden können.
	 */
	public static final Map<Short, SyscallInterface<Short>> SYSCALLS;

	/** Lädt die definierten {@linkplain SyscallInterface Syscalls} */
	static {
		SYSCALLS = getSyscalls();
	}

	public static Map<Short, SyscallInterface<Short>> getSyscalls() {
		try {
			Set<Class<?>> syscallClasses = new Reflections("com.github.mbeier1406.SVM.syscalls").getTypesAnnotatedWith(Syscall.class);
			final Map<Short, SyscallInterface<Short>> syscalls = new HashMap<>();
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

}
