package com.github.mbeier1406.svm.prg;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.instructions.InstructionDefinition;
import com.github.mbeier1406.svm.instructions.InstructionInterface;
import com.github.mbeier1406.svm.instructions.Mov;

/**
 * Definiert alle Datenstrukturen und Methoden zur internen Darstellung
 * eines ausführbaren {@linkplain SVM}-Programms. Es handelt sich um die interne Repräsentation.
 */
public interface SVMProgram<T> {

	/** Typen von symbolischen Adressen */
	public static enum LabelType { INSTRUCTION, DATA };

	/**
	 * Definiert eine symbolische Adresse in einem SVM-Programm. Es handelt sich
	 * um Sprungadressen für die {@code JMP}-Anweisung, Lade- oder Speicheradressen für
	 * {@linkplain Mov} usw.
	 */
	public static record Label(LabelType labelType, String label) {
		public Label {
			if ( requireNonNull(label, "label").isEmpty() ) throw new IllegalArgumentException("label");
		}
	}

	/**
	 * {@linkplain InstructionDefinition Instruktionen} können Parameter erthalten, die Adressen sind,
	 * deren Wert zum Zeitpunkt der Programmerstellung noch nicht bekannt ist. Diese werden erst beim
	 * Laden in den Speicher der SVM bestimmt. Bei einer virtuellen Instruktion werden für jeden
	 * Parameter in der Liste ({@linkplain InstructionDefinition#params()}, Anzahl bestimmt durch
	 * {@linkplain InstructionInterface#getAnzahlParameter()}) eine Information bereitgestellt, die
	 * aussagt, ob es sich bei dem Parameter um eine absolute (oder gar keine) Adresse handelt, oder
	 * ob es sich um eine virtuelle Adresse (bezeichnet durch {@linkplain Label} handelt, die beim
	 * Laden mit {@linkplain SVMLoader#load(MEM, SVMProgram)} berechnet wird, handelt. Darstellung:
	 * <code>&lt;Instruction>&lt;Param1>&lt;Param2>&lt;...></code> mit Liste der Labels<code>
	 * &lt;Label1>&lt;Label2>&lt;...></code>. Die beiden Listen müssen gleich lang sein. Folgende
	 * Kombinationen sind zulässig:
	 * <ul>
	 * <li>{@code Label[i]} ist <b>null</b>: es wird der Wert in {@code Param[i]} verwendet.</li>
	 * <li>{@code Label[i]} ist <u>nicht</u> <b>null</b>: es wird die sich beim Laden ergebende
	 * Adresse des labels verwendet.</li>
	 * </ul>
	 * @param <T> Die Wortänge der {@linkplain SVM}
	 */
	public record VirtualInstruction<T>(InstructionDefinition<T> instruction, Label[] labelList) {
		public VirtualInstruction {
			requireNonNull(instruction, "instruction");			
			requireNonNull(labelList, "labelList");			
			for ( int i=0; i < labelList.length; i++ )
				requireNonNull(labelList[i], "labelList["+i+"]");
		}
	}

	/**
	 * Fügt eine Anweisung der Liste des Programms hinzu. Die Liste muss eine Reihenfolge enthalten,
	 * damit das Programm später in der korrekten Reihenfolge ausgeführt wird.
	 * @param instruction die nächste, auszuführende Instruktion
	 * @see #getInstructionList()
	 */
	public void addInstruction(final VirtualInstruction<T> instruction);

	/**
	 * Liefert die geordnete Liste der Instruktionen des Programms.
	 * @return Liste der Instruktionen
	 * @see #addInstruction(InstructionDefinition)
	 */
	public List<VirtualInstruction<T>> getInstructionList();

	/** Daten, die im {@linkplain MEM Speicher} abgelegt werden, können über einen Labe adressiert werden */
	public static record Data<T>(Label label, T[] dataList) {
		public Data {
			requireNonNull(label, "label");
			if ( label.labelType != LabelType.DATA ) throw new IllegalArgumentException("Labeltyp ungültig: "+label.labelType);
			if ( requireNonNull(dataList, "dataList").length == 0 ) throw new IllegalArgumentException("dataList");
			for ( int i=0; i < dataList.length; i++ )
				requireNonNull(dataList[i], "dataList["+i+"]");
		}
	}

	/** Fügt ein Datenobjekt in die Liste der Objekte ein */
	public void addData(final Data<T> data) throws SVMException;

	/** Liefert die Liste der Datenobjekte, diese hat keine Ordnung, da die Objekte selber Adressen über den Label erhalten */
	public List<Data<T>> getDataList();

	/**
	 * Stellt sicher, dass das Programm in sich konsitent ist (z. B. die Parameterlisten in
	 * {@linkplain VirtualInstruction} gleich lang sind usw.) und 
	 * @throws SVMException bei fehlerhaften Programmdefinitionen mit einer entsprechenden Fehlermeldung
	 */
	public void validate() throws SVMException;

}
