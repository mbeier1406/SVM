package com.github.mbeier1406.svm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.cmd.Alu;
import com.github.mbeier1406.svm.cmd.Command;
import com.github.mbeier1406.svm.cmd.CommandInterface;
import com.github.mbeier1406.svm.cmd.Ende;
import com.github.mbeier1406.svm.cmd.Hilfe;
import com.github.mbeier1406.svm.cmd.Information;
import com.github.mbeier1406.svm.cmd.LogLevel;
import com.github.mbeier1406.svm.cmd.Programm;
import com.github.mbeier1406.svm.instructions.Instruction;
import com.github.mbeier1406.svm.instructions.InstructionFactory;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.instructions.Int;
import com.github.mbeier1406.svm.instructions.Mov;
import com.github.mbeier1406.svm.instructions.Nop;
import com.github.mbeier1406.svm.syscalls.Exit;
import com.github.mbeier1406.svm.syscalls.IO;
import com.github.mbeier1406.svm.syscalls.Syscall;
import com.github.mbeier1406.svm.syscalls.SyscallFactory;
import com.github.mbeier1406.svm.syscalls.SyscallInterface;

/**
 * Test für die Klasse {@linkplain GenericFactory}.
 */
public class GenericFactoryTest {

	/** Stellt sicher, dass die die Factory die Instruktionen findet */
	@Test
	@SuppressWarnings("unchecked")
	public void testeInstructionFactory() {
		final GenericFactory<Byte, InstructionInterface<Short>> genericFactory = new GenericFactory<>();
		final var items = genericFactory.getItems(InstructionFactory.PACKAGE, Instruction.class, "code");
		assertThat(items.values(), contains(new Nop(), new Int(), new Mov()));

	}

	/** Stellt sicher, dass die die Factory die Syscalls der SVM findet */
	@Test
	@SuppressWarnings("unchecked")
	public void testeSyscallFactory() {
		final GenericFactory<Byte, SyscallInterface<Short>> genericFactory = new GenericFactory<>();
		final var items = genericFactory.getItems(SyscallFactory.PACKAGE, Syscall.class, "code");
		assertThat(items.values(), contains(new Exit(), new IO()));

	}

	/** Stellt sicher, dass die die Factory die Kommandos der SVM-Cli findet */
	@Test
	public void testeCommandFactory() {
		final GenericFactory<String, CommandInterface> genericFactory = new GenericFactory<>();
		final var items = genericFactory.getItems(CommandInterface.PACKAGE, Command.class, "command");
		assertThat(items.values(), contains(new Hilfe(), new Ende(), new LogLevel(), new Information(), new Alu(), new Programm()));

	}

}
