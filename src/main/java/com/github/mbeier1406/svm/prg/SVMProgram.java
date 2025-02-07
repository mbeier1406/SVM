package com.github.mbeier1406.svm.prg;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
public interface SVMProgram<T> extends Serializable {

	/** Typen von symbolischen Adressen */
	public static enum LabelType { INSTRUCTION, DATA };

	/**
	 * Definiert eine symbolische Adresse in einem SVM-Programm. Es handelt sich
	 * um Sprungadressen für die {@code JMP}-Anweisung, Lade- oder Speicheradressen für
	 * {@linkplain Mov} usw.
	 */
	public static record Label(LabelType labelType, String label) implements Serializable {
		public Label {
			if ( requireNonNull(label, "label").isEmpty() ) throw new IllegalArgumentException("label");
		}
	}

	/**
	 * {@linkplain InstructionDefinition Instruktionen} können einem {@linkplain Label} nachgestellt
	 * sein, d. h. sie sind das Ziel eines Sprungbefehls sein. Sie können auch  Parameter erthalten, die
	 * Adressen sind, deren Wert zum Zeitpunkt der Programmerstellung noch nicht bekannt ist. Diese werden
	 * erst beim Laden in den Speicher der SVM bestimmt. Bei einer virtuellen Instruktion werden für jeden
	 * Parameter in der Liste ({@linkplain InstructionDefinition#params()}, Anzahl bestimmt durch
	 * {@linkplain InstructionInterface#getAnzahlParameter()}) eine Information bereitgestellt, die
	 * aussagt, ob es sich bei dem Parameter um eine absolute (oder gar keine) Adresse handelt, oder
	 * ob es sich um eine virtuelle Adresse (bezeichnet durch {@linkplain Label} handelt, die beim
	 * Laden mit {@linkplain SVMLoader#load(MEM, SVMProgram)} berechnet wird, handelt. Darstellung:
	 * <code>&lt;Instruction>&lt;Param1>&lt;Param2>&lt;...></code> mit Liste der Labels<code>
	 * &lt;Label1>&lt;Label2>&lt;...></code>. Die beiden Listen müssen gleich lang sein. Folgende
	 * Kombinationen sind zulässig:
	 * <ul>
	 * <li>{@code Label[i]} ist <b>nicht gesetzt</b>: es wird der Wert in {@code Param[i]} verwendet.</li>
	 * <li>{@code Label[i]} ist <b>gesetzt</b>: es wird die sich beim Laden ergebende Adresse des Labels verwendet.</li>
	 * </ul>
	 * @implNote Standard hashCode()/equals()-Methoden funktionieren nicht!
	 * @param label der Label, der dieser Instruktion vorangestellt ist, d. h. die Instruktion kann Ziel eines Sprungbefehls sein, <b>null</b> wenn keine Sprungmarke
	 * @param instruction die auszuführende Instruktion
	 * @param labelList falls die Instruktion Parameter erhält, können hier virtuelle Adresse/Label eingesetzt werden, <b>null</b> wenn nicht
	 * @param <T> Die Wortänge der {@linkplain SVM}
	 */
	public record VirtualInstruction<T>(Label label, InstructionDefinition<T> instruction, Label[] labelList) implements Serializable {
		public VirtualInstruction {
			requireNonNull(instruction, "instruction");			
			requireNonNull(labelList, "labelList");
			int erwarteteAnzahlParameter = instruction.instruction().getAnzahlParameter();
			int erhalteneAnzahlParameter = labelList.length;
			if ( erwarteteAnzahlParameter != erhalteneAnzahlParameter )
				throw new IllegalArgumentException("instruction="+instruction+": erwartete Parameter: "+erwarteteAnzahlParameter+"; erhalteneAnzahlParameter: "+erhalteneAnzahlParameter);
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(labelList);
			result = prime * result + Objects.hash(instruction, label);
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("rawtypes")
			VirtualInstruction other = (VirtualInstruction) obj;
			return Objects.equals(instruction, other.instruction) && Objects.equals(label, other.label)
					&& Arrays.equals(labelList, other.labelList);
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

	/**
	 * Daten, die im {@linkplain MEM Speicher} abgelegt werden, können über einen Label adressiert werden.
	 * @implNote Standard hashCode()/equals()-Methoden funktionieren nicht!
	 * @param <T> Wortlänge der {@linkplain SVM}
	 */
	public static record Data<T>(Label label, T[] dataList) implements Serializable {
		public Data {
			requireNonNull(label, "label");
			if ( label.labelType != LabelType.DATA ) throw new IllegalArgumentException("Labeltyp ungültig: "+label.labelType);
			if ( requireNonNull(dataList, "dataList").length == 0 ) throw new IllegalArgumentException("dataList");
			for ( int i=0; i < dataList.length; i++ )
				requireNonNull(dataList[i], "dataList["+i+"]");
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.deepHashCode(dataList);
			result = prime * result + Objects.hash(label);
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("rawtypes")
			Data other = (Data) obj;
			return Arrays.deepEquals(dataList, other.dataList) && Objects.equals(label, other.label);
		}
	}

	/** Fügt ein Datenobjekt in die Liste der Objekte ein */
	public void addData(final Data<T> data);

	/** Liefert die Liste der Datenobjekte, diese hat keine Ordnung, da die Objekte selber Adressen über den Label erhalten */
	public List<Data<T>> getDataList();

	/**
	 * Stellt sicher, dass das Programm in sich konsitent ist (z. B. die Parameterlisten in
	 * {@linkplain VirtualInstruction} gleich lang sind usw.) und 
	 * @throws SVMException bei fehlerhaften Programmdefinitionen mit einer entsprechenden Fehlermeldung
	 */
	public void validate() throws SVMException;

}
