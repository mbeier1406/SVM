package com.github.mbeier1406.svm.instructions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.SVMException;

/**
 * <b>Int</b> Interrupt - die Instruktion unterbricht die aktuelle Programmausüfhrung und
 * ruft ein externes Modul auf, dessen <b>Code</b> als Parameter mitgegeben wird.
 * <h3>Syscall</h3>
 * Code <b>0x1</b>: ruft einen{@linkplain com.github.mbeier1406.svm.syscalls.SyscallInterface Syscall} auf.
 * Folgende Register der {@linkplain ALU} werden verwendet:
 * <ol>
 * <li><u>Register 1</u>: enthält die Nummer des aufzurufenden Syscalls {@linkplain com.github.mbeier1406.svm.syscalls.Syscall#code()}</li>
 * <li><u>Register 2</u>: enthält den ersten Parameter für den jeweiligen Syscall</li>
 * <li><u>Register 3</u>: enthält den zweiten Parameter für den jeweiligen Syscall</li>
 * <li><u>Register 4</u>: enthält den dritten Parameter für den jeweiligen Syscall</li>
 * </ol>
 * @see {@linkplain com.github.mbeier1406.svm.syscalls.Exit}
 * @see {@linkplain com.github.mbeier1406.svm.syscalls.IO}
 */
@Instruction(code=0x2)
public class Int extends InstructionBase implements InstructionInterface<Short>, IntInterface<Short> {

	public static final Logger LOGGER = LogManager.getLogger(Int.class);

	/**
	 * <i>Interrupt</i> benutzt einen Parameter (<b>Code</b> des Moduls, das aufgerufen werden soll, ein Byte)
	 * und kommt daher mit einem Speicherwort ({@linkplain Short} == zwei Byte) aus:<br/>
	 * <pre><code>
	 * Speicherwort I
	 * Byte I             Byte II
	 * &lt;0x2> (Interrupt)  &lt;Code> (Nummer des aufzurufenden Moduls)  
	 * </code></pre><p/>
	 * {@inheritDoc}
	 */
	@Override
	public int getAnzahlParameter() {
		return 1;  // Identifiziert das Modul, das den Interrupt bearbeitet
	}

	/**
	 * Entnimmt dem im Parameter übergebenen Byte den Code des Moduls, das die
	 * weitere Programmausführung übernimmt (siehe {@linkplain IntInterface#MODULES}
	 * und sucht in der Map nach dem in {@code Register 0} angegeben Service und
	 * ruft dessen {@code execute()}-Funktion mit den {@code Registern 1..} als Parameter auf.
	 * <p/>
	 * {@inheritDoc}
	 * @throws throws SVMException wenn kein Servicemodul, darin kein Service oder darin keine Execute-Funktion gefunden wird
	 * */
	@Override
	public int execute(byte[] params) throws SVMException {
		checkParameter(params);
		byte code = params[0];
		LOGGER.trace("INT: Modulcode='{}'", code);
		var module = MODULES.get(code); // Das Byte nach der Instruktion setzt das Servicemodul
		if ( module == null ) throw new SVMException("Kein Modul für Code '"+code+"'!");
		LOGGER.trace("INT: Module={}", module);
		Short function = this.alu.getRegisterValue(0); // Register 1 codiert die Funktion, die aufgerufen werden soll
		LOGGER.trace("INT: Funktion='{}'", function);
		IntInterface<Short> service = module.getFunctions().get(function.byteValue());
		if ( service == null ) throw new SVMException("Kein Service für Funktion '"+function+"' in Modul für Code '"+code+"'!");
		LOGGER.trace("INT: Service='{}'", service);
		Method execute;
		try {
			execute = service.getClass().getDeclaredMethod("execute", Short.class, Short.class, Short.class);
		}
		catch (Exception e) {
			throw new SVMException("Keine execute()-Funktion für Service '"+service+"' für Funktion '"+function+"' in Modul für Code '"+code+"'!");
		}
		var param1 = this.alu.getRegisterValue(1); // Register 1 enthält immer den ersten Parameter der Servicefunktion
		var param2 = this.alu.getRegisterValue(2); // Register 2 enthält immer den ersten Parameter der Servicefunktion
		var param3 = this.alu.getRegisterValue(3); // Register 3 enthält immer den ersten Parameter der Servicefunktion
		LOGGER.trace("INT: param1='{}'; param2='{}'; param3='{}'", param1, param2, param3);
		Object returnCode;
		try {
			returnCode = execute.invoke(service, param1, param2, param3);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new SVMException("Fehler bei execute()-Funktion für Service '"+service+"' für Funktion '"+function+"' in Modul für Code '"+code+"'"
					+ "; Param1='"+param1+"'; Param2='"+param2+"'; Param3='"+param3);
		}
		LOGGER.trace("INT: returnCode='{}'", returnCode);
		if ( returnCode instanceof Integer )
			return (int) returnCode;
		return 0; // erfolgreiche Ausführung
	}

}
