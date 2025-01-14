package com.github.mbeier1406.svm.instructions;

import static com.github.mbeier1406.svm.SVM.BD_BYTE;
import static com.github.mbeier1406.svm.SVM.BD_SHORT;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;

/**
 * <b>MOV</b> Move - Werte in Register oder Speicherbereiche verschieben,<p/>
 * Aufbau:
 * <pre><code>
 * Speicherwort I                  SpeicherWort II  Speicherwort III
 * Byte I   Byte II
 * &ltcode>   &lt;Von-Code>&lt;Nach-Code>  &lt;Wert>           &lt;Ziel>
 * </code></pre>
 * Folgende Codes sind definiert:
 * <ul>
 * <li>0x1: Register</li>
 * <li>0x2: Konstante</li>
 * <li>0x3: Speicheradresse</li>
 * </ul>
 * <p/>
 * Beispiele:
 * <ul>
 * <li>MOV $1 %REG(0): 00000011.0010.0001 0000000000000001 0000000000000000</li>
 * <li>MOV %REG(1) &ADDR(7): 00000011.0001.0011 0000000000000001 0000000000000111</li>
 * </ul>
 */
@Instruction(code = Mov.CODE)
public class Mov extends InstructionBase implements InstructionInterface<Short> {

	private static final long serialVersionUID = -6101257415849808446L;
	public static final Logger LOGGER = LogManager.getLogger(Mov.class);

	/** Der Code im {@linkplain MEM Speicher}, die diesen Maschinenbefehl idebntifiziert */
	public static final byte CODE = 0x3;

	/**
	 * Parameter 1 (zweites Byte nach dem Instrunktionscode) besteht aus
	 * zwei Vier-Bit (Nibble) Werten, die Quelle und Ziel für den MOV-Befehl angeben.
	 * Folgende Werte sind erlaubt.
	 */
	public static enum MovCodes {
		REGISTER((byte) 0x1), // von einem / in ein Register verschieben
		CONSTANT((byte) 0x2), // Wert an ein Ziel verschieben, nicht als Ziel erlaubt
		ADDRESS((byte) 0x3);  // von einer / in eine Speicheradresse verschieben
		private final byte code;
		private MovCodes(byte code) {
			this.code = code;
		}
		public byte getCode() {
			return this.code;
		}
		public static MovCodes getMovCode(byte code) {
			for ( MovCodes mc : MovCodes.values() )
				if ( mc.getCode() == code )
					return mc;
			throw new IllegalArgumentException("Ungüliger MovCode: "+code);
		}
	}

	/**
	 * Erwartet fünf Bytes als Parameter:
	 * <ol>
	 * <li> Byte 1: zweiter Teil des ersten Speicherworts enthält die Codes für Quelle und Ziel</li>
	 * <li> Byte 2-3: Wert/Adresse von</li>
	 * <li> Byte 4-5: Ziel</li>
	 * </ol>
	 * <p/>
	 * {@inheritDoc}
	 */
	@Override
	public int getAnzahlParameter() {
		return 5;
	}

	/** {@inheritDoc} */
	@Override
	public int execute(byte[] params) throws SVMException {
		checkParameter(params);
		byte codeVon = (byte) (params[0] >> 4);
		byte codeNach = (byte) (params[0] & 15);
		LOGGER.trace("Quelle: {}; Ziel {} ({})", BD_BYTE.getBinaerDarstellung(codeVon), BD_BYTE.getBinaerDarstellung(codeNach), BD_BYTE.getBinaerDarstellung(params[0]));
		short wertVon = bytes2Short(params[1], params[2]);
		short wertNach = bytes2Short(params[3], params[4]);
		LOGGER.trace("wertVon={}; wertnach={}", BD_SHORT.getBinaerDarstellung(wertVon), BD_SHORT.getBinaerDarstellung(wertNach));
		var mcVon = MovCodes.getMovCode(codeVon);
		var mcNach = MovCodes.getMovCode(codeNach);
		switch ( mcVon ) {
			case REGISTER: {
				short wert = alu.getRegisterValue(wertVon);
				switch ( mcNach ) {
					case REGISTER: {
						/* Wert aus Register in ein Register */
						LOGGER.trace("REGISTER({})={} -> REGISTER({})", wertVon, wert, wertNach);
						alu.setRegisterValue(wertNach, wert);
						break;
					}
					case ADDRESS: {
						/* Wert aus Register an eine Speicheradresse */
						LOGGER.trace("REGISTER({})={} -> ADDRESS({})", wertVon, wert, wertNach);
						mem.write(wertNach, wert);
						break;
					}
					default: { throw new IllegalArgumentException("Von REGISTER: Ungültiger MoveCode nach: "+mcNach); }
				}
				break;
			}
			case CONSTANT: {
				switch ( mcNach ) {
					case REGISTER: {
						/* Konstante in ein Register */
						LOGGER.trace("CONSTANT={} -> REGISTER({})", wertVon, wertNach);
						alu.setRegisterValue(wertNach, wertVon);
						break;
					}
					case ADDRESS: {
						/* Konstante an eine Speicheradresse */
						LOGGER.trace("CONSTANT={} -> ADDRESS({})", wertVon, wertNach);
						mem.write(wertNach, wertVon);
						break;
					}
					default: { throw new IllegalArgumentException("Von CONSTANT: Ungültiger MoveCode nach: "+mcNach); }
				}
				break;
			}
			case ADDRESS: {
				short wert = mem.read(wertVon);
				switch ( mcNach ) {
					case REGISTER: {
						/* Speicheradresse in ein Register */
						LOGGER.trace("ADDRESS({})={} -> REGISTER({})", wertVon, wert, wertNach);
						alu.setRegisterValue(wertNach, wert);
						break;
					}
					default: { throw new IllegalArgumentException("Von ADDRESS: Ungültiger MoveCode nach: "+mcNach); }
				}
				break;
			}
			default: { throw new IllegalArgumentException("Ungültiger MoveCode von: "+mcVon); }
		}
		return 0;
	}

}
