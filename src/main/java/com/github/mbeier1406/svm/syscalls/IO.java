package com.github.mbeier1406.svm.syscalls;

import static com.github.mbeier1406.svm.syscalls.IO.OutStream.NULL_FILE;
import static com.github.mbeier1406.svm.syscalls.IO.OutStream.STDERR;
import static com.github.mbeier1406.svm.syscalls.IO.OutStream.STDOUT;
import static com.github.mbeier1406.svm.syscalls.IO.OutStream.TEMP_FILE;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Gibt die Zeichenkette an der Adresse Parameter 2 der Länge Parameter 3
 * in den Kanal Parameter 1 aus.
 */
@Syscall(code = 0x2)
public class IO extends SyscallBase implements SyscallInterface<Short> {

	public static final Logger LOGGER = LogManager.getLogger(IO.class);

	/** Definiert die Audgabekanäle, in die standardmäßg geschrieben werden kann */
	public static enum OutStream { NULL_FILE, STDOUT, STDERR, TEMP_FILE };

	/** In diese Datei kann während der Programmausführung der {@linkplain SVM} geschrieben werden */
	private static PrintStream tempFile = null;

	/** Setzt die temporäre Datei durch die {@linkplain com.github.mbeier1406.SVM.Runtime} (NULL für löschen) */
	public void setTempFile(final PrintStream tempFile) {
		IO.tempFile = tempFile;
		IO_MAP.put((short) TEMP_FILE.ordinal(), tempFile);
	}

	/** Diese Map ordnet den Ausgabekanälen {@linkplain OutStream} die Dateidescriptoren zu */
	@SuppressWarnings("serial")
	private Map<Short, PrintStream> IO_MAP  = new HashMap<Short, PrintStream>() {{
		put((short) NULL_FILE.ordinal(), new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException { } // nichts tun
		}));
		put((short) STDOUT.ordinal(), System.out);
		put((short) STDERR.ordinal(), System.err);
		put((short) TEMP_FILE.ordinal(), tempFile);
	}};

	/** {@inheritDoc} */
	@Override
	public int execute(Short param1, Short param2, Short param3) throws SVMException {
		LOGGER.trace("param1={}; param2={}, param3={}", param1, param2, param3);
		PrintStream out = requireNonNull(IO_MAP.get(requireNonNull(param1, "param1")), "Output '"+param1+"'!");
		for ( short i=0; i < requireNonNull(param3, "param3"); i++ )
			out.print((char) requireNonNull(super.mem, "mem").read(requireNonNull(param2, "param2")+i).shortValue());
		return 0;
	}

}
