package com.github.mbeier1406.svm.prg;

import static org.apache.logging.log4j.CloseableThreadContext.put;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.ALU;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.instructions.InstructionIO;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.instructions.InstructionWriterInterface;
import com.github.mbeier1406.svm.instructions.InstructionWriterShort;
import com.github.mbeier1406.svm.prg.SVMProgram.Data;
import com.github.mbeier1406.svm.prg.SVMProgram.Label;

/**
 * Lädt ein {@linkplain SVMProgram} in einen {@linkplain MEM Speicher} der Wortlänge
 * {@linkplain Short} zur Ausführung mit {@linkplain ALU#start()}.
 */
public class SVMLoaderShort implements SVMLoader<Short>, InstructionIO<Short> {

	public static final Logger LOGGER = LogManager.getLogger(SVMLoaderShort.class);

	/**
	 * Die in {@linkplain #addData(com.github.mbeier1406.svm.prg.SVMProgram.Data)} übergebenen
	 * statischen Daten werden im {@linkplain MEM Hauptspeicher} beginnend ab Adresse {@linkplain MEM#getLowAddr()}
	 * aufsteigend abgelegt. Diese Variable speichert diejenige, an dem das nächste, übergebene
	 * Datum abgelegt werden soll. Es wird zusammen mit dem Bezeichner im {@linkplain Label}
	 * gespeichert und in der Liste {@linkplain #labelList} für den späteren Zugriff durch das
	 * Programm abgelegt. Die Adresse wächst also im Speicher "von unten nach oben" (zu den
	 * höheren Adressen), dem Programmcode entgegen.
	 */
	private int dataAddr = 0;

	/**
	 * Speichert die Adresse, an die der nächste {@linkplain InstructionInterface SVM-Befehl}
	 * gespeichert werden soll. Diese beginnt immer an der höchsten Adresse des {@linkplain MEM Speichers}
	 * und wird absteigend gespeichert.
	 */
	private int prgAddr;

	/**
	 * Innerhalb des {@linkplain SVMProgram}s werden bei {@linkplain SVMProgram.Data Daten} und
	 * {@linkplain SVMProgram.VirtualInstruction Instruktionen} Label verwendet, die virtuelle
	 * Adressen im Programm repräsentieren. Diese müssen beim Einspielen des Programms mit
	 * konkreten Adressen im {@linkplain MEM Speicher} versehen werden, und alle Referenzen darauf
	 * in den virtuellen Instruktionen durch konkrete Adressen für die {@linkplain InstructionInterface}
	 * ersetzt werden.
	 */
	private final Map<Label, Integer> labelList = new HashMap<>();

	/**
	 * Schreibt die Instruktionen aus {@linkplain SVMProgram#getInstructionList()} in den Hauptspeicher.
	 * Hier werden die {@linkplain InstructionDefinition Instruktionen mit Parametern} in die
	 * entsprechendne Speicherwörter übersetzt.
	 */
	private final InstructionWriterInterface<Short> instructionWriter = new InstructionWriterShort();

	/**
	 * In dieser Datenstruktur werden die echten Speicheradressen im ({@linkplain MEM} mit den zugeordenen
	 * Datenstrukturen ({@linkplain Data} und Instruktionen {@linkplain InstructionInterface} gespeichert.
	 * Diese Information kann von der {@linkplain ALU} verwendet werden, um bei der Ausführung eines SVM-Programms
	 * Debugging-Informationen auszugeben.
	 */
	private final DebuggingInfo<Short> debuggingInfo = new DebuggingInfo<Short>();


	/**	{@inheritDoc} */
	@Override
	public void load(final MEM<Short> mem, final SVMProgram<Short> svmProgram) throws SVMException {

		try ( CloseableThreadContext.Instance ctx = put("mem", mem.toString()).put("svmProgramm", svmProgram.toString()) ) {

			/* Schritt I: Sicherstellen, dass das Programm in sich konsistent ist und Initialisierung */
			LOGGER.debug("Prg: Validierung...");
			svmProgram.validate();

			/* Schritt II: Daten einspielen, definierte Label mit Adresse speichern */
			LOGGER.debug("Data einspielen...");
			for ( var data : svmProgram.getDataList() ) {
				LOGGER.trace("Data: dataAddr={}; label={}", this.dataAddr, data.label());
				this.labelList.put(data.label(), this.dataAddr);
				this.debuggingInfo.getDataAdresses().put(this.dataAddr, data);
				for ( int i=0; i < data.dataList().length; i++ )
					mem.getInstructionInterface().write(this.dataAddr+i, (Short) data.dataList()[i]);
				this.dataAddr += data.dataList().length;
				if ( this.dataAddr >= mem.getHighAddr() )
					throw new SVMException("Zu viele Daten: dataAddr="+this.dataAddr+"; prgAddr="+this.prgAddr);
			}

			/* Schritt III: Für im Programmcode definierte (!!) Label Adressen ermitteln und speichern */
			this.prgAddr = mem.getHighAddr();
			LOGGER.debug("Code Labeladressen ermitteln...");
			for ( var virtInstr : svmProgram.getInstructionList() ) {
				LOGGER.trace("Label: prgAddr={}; instr={}", this.prgAddr, virtInstr);
				var label = virtInstr.label();
				if ( label != null ) { // Diese Instruktion dient als Ziel für einen Sprungbefehl
					LOGGER.trace("Label: prgAddr={}; label={}", this.prgAddr, label);
					this.labelList.put(label, this.prgAddr);
				}
				int instrLenInWords = instructionWriter.instruction2Array(virtInstr.instruction()).length;
				LOGGER.trace("instrLenInWords={}", instrLenInWords);
				this.prgAddr += instrLenInWords;
			}

			/* Schritt IV: Programmcode einspielen, Adressen für verwendete Label ersetzen */
			this.prgAddr = mem.getHighAddr();
			LOGGER.debug("Code schreiben...");
			for ( int i=0; i < svmProgram.getInstructionList().size(); i++ ) {
				var virtInstr = svmProgram.getInstructionList().get(i);
				LOGGER.trace("Code: prgAddr={}; instrIndex={}; instr={}", this.prgAddr, i, virtInstr);
				var instrDef = virtInstr.instruction();
				for ( int j=0; j < virtInstr.labelList().length; j++ ) {
					Label label = virtInstr.labelList()[j];
					LOGGER.trace("Code: Index={}; label={}", j, label);
					if ( label!= null ) { // Die Instruktion verwendet eine virtuelle Adresse, deren Wert erst jetzt (zum Zeitpunkt des Ladens) ermittelt werdne kann
						Integer addr = this.labelList.get(label);
						if ( addr == null )
							/* Darf nie passieren, muss zuvor oben mit validate() geprüft worden sein */
							throw new SVMException("[Intern] Label nicht definiert: Instr "+i+"; Index "+j+": "+virtInstr);
						if ( addr > Short.MAX_VALUE )
							throw new SVMException("[Intern] Label-Adresse größer Wortgröße MEM: Instr "+i+"; Index "+j+" ("+virtInstr+" / "+label+" / "+mem+"): addr="+addr);
						if ( addr < mem.getLowAddr() || addr >= mem.getHighAddr() )
							throw new SVMException("[Intern] Label-Adresse außerhalb MEM: Instr "+i+"; Index "+j+" ("+virtInstr+" / "+label+" / "+mem+"): addr="+addr);
						// Jetzt die ermittelte Adresse einsetzen
						instrDef.params()[j+1] = (byte) (addr.shortValue() & 255);
						instrDef.params()[j] = (byte) (addr.shortValue() >> 8);
					}
				}
				int instrLenInWords = instructionWriter.writeInstruction(mem.getInstructionInterface(), this.prgAddr, instrDef);
				this.debuggingInfo.getInstructionAdresses().put(this.prgAddr, virtInstr);
				LOGGER.trace("instrLenInWords={}", instrLenInWords);
				this.prgAddr -= instrLenInWords;
			};

		}
		catch ( Exception e ) {
			LOGGER.error("{} {}", mem, svmProgram, e);
			throw new SVMException(e);
		}
	}

	/**	{@inheritDoc} */
	@Override
	public Map<Label, Integer> getLabelList() {
		return this.labelList;
	}

	/**	{@inheritDoc} */
	@Override
	public DebuggingInfo<Short> getDebuggingInfo() {
		return debuggingInfo;
	}

	@Override
	public String toString() {
		return "SVMLoaderShort [dataAddr=" + dataAddr + ", prgAddr=" + prgAddr + ", labelList=" + labelList + "]";
	}

}
