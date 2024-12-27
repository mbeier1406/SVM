package com.github.mbeier1406.svm.instructions;

import static com.github.mbeier1406.svm.instructions.InstructionFactory.INSTRUCTIONS;
import static com.github.mbeier1406.svm.instructions.InstructionInterface.Codes.MOV;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.mbeier1406.svm.SVMException;

/**
 * Tests für die Klasse {@linkplain Mov}.
 */
public class MovTest extends TestBase {

	public static final Logger LOGGER = LogManager.getLogger(MovTest.class);

	/** Instruktion setzen */
	@BeforeEach
	public void init() {
		this.instruction = INSTRUCTIONS.get(MOV.getCode());
	}

	/** Wert aus dem Speicher in ein Register schreiben */
	@Test
	public void testeAddress2Register() throws SVMException {
		this.instruction.execute(new byte[] {
				(byte) 49,	// 00110001 (3, 1): ADDR -> REG
				(byte) 0,
				(byte) 4,	// in TestBase wird an Adresse 4 ein 'x' geschrieben
				(byte) 0,
				(byte) 3	// in Register 3 schreiben
		});
		short ergebnis = this.alu.getRegisterValue(3);
		LOGGER.info("Ergebnis: '{}'", (char) ergebnis);
		assertThat((char) ergebnis, equalTo('x'));
	}

	/** Konstante in den Speicher schreiben */
	@Test
	public void testeConstant2Address() throws SVMException {
		this.instruction.execute(new byte[] {
				(byte) 35,	// 00100011 (2, 3): CONST -> ADDR
				(byte) 3,
				(byte) 3,	// Ergibt die Konstante 771
				(byte) 0,
				(byte) 99	// an die Adresse 99 schreiben
		});
		short ergebnis = this.mem.read(99);
		LOGGER.info("Ergebnis: '{}'", ergebnis);
		assertThat(ergebnis, equalTo((short) 771));
	}

	/** Konstante in ein Register schreiben */
	@Test
	public void testeConstant2Register() throws SVMException {
		this.instruction.execute(new byte[] {
				(byte) 33,	// 00100001 (2, 1): CONST -> REG
				(byte) 7,
				(byte) 7,	// Ergibt die Konstante 1799
				(byte) 0,
				(byte) 3	// in Register 3 schreiben
		});
		short ergebnis = this.alu.getRegisterValue(3);
		LOGGER.info("Ergebnis: '{}'", ergebnis);
		assertThat(ergebnis, equalTo((short) 1799));
	}

	/** Register in den Speicher schreiben */
	@Test
	public void testeRegister2Address() throws SVMException {
		this.alu.setRegisterValue(2, (short) 1234);
		this.instruction.execute(new byte[] {
				(byte) 19,	// 00010011 (1, 3): REG -> ADDR
				(byte) 0,
				(byte) 2,	// Register 2 lesem
				(byte) 0,
				(byte) 55	// an die Adresse 199 schreiben
		});
		short ergebnis = this.mem.read(55);
		LOGGER.info("Ergebnis: '{}'", ergebnis);
		assertThat(ergebnis, equalTo((short) 1234));
	}

	/** Register nach Register schreiben */
	@Test
	public void testeRegister2Register() throws SVMException {
		this.alu.setRegisterValue(0, (short) 0);
		this.alu.setRegisterValue(2, (short) 12345);
		this.instruction.execute(new byte[] {
				(byte) 17,	// 00010011 (1, 1): REG -> REG
				(byte) 0,
				(byte) 2,	// Register 2 lesem
				(byte) 0,
				(byte) 0	// Register schreiben
		});
		short ergebnis = this.alu.getRegisterValue(0);
		LOGGER.info("Ergebnis: '{}'", ergebnis);
		assertThat(ergebnis, equalTo((short) 12345));
	}

}
