package com.github.mbeier1406.svm;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.github.mbeier1406.svm.instructions.InstructionFactory;
import com.github.mbeier1406.svm.syscalls.SyscallFactory;

/**
 * An verschiedenen Stellen werden Funktionen (Klassen) der SVM über Annotationen geladen.
 * Diese Factory vereinhetlicht das Laden und liefert eine Map der Items.
 * @param <K>
 * @param <V>
 * @see InstructionFactory
 * @see SyscallFactory
 */
public class GenericFactory<K, V> {

	/**
	 * Liefert eine Map mit den gesuchten Items. Für jede gefundene Kalsse wird der
	 * paramterlose Konstruktor aufgerufen, und as erhaltene Objekt als Value in die Map
	 * eingesetzt. Als Key wird der Wert eingesetzt, der sich als Wert nach Aufruf der angegebenen
	 * ergibt, die in der Annotationsklasse enthalten ist.
	 * @param path Der Packagename, in dem sich die gesuchten items befinden
	 * @param annotationClass Die Annotation, mit der die gesuchten Items (Klassen) ausgezeichnet sind
	 * @param keyMethod Die Methode in der Annotation, die den Wert für den Key liefert
	 * @return eine Map mit den Objekten der gefunden Klassen und dem Key aus dem Methodenaufruf der Annotation
	 */
	@SuppressWarnings("unchecked")
	public Map<K, V> getItems(String path, Class<? extends Annotation> annotationClass, String keyMethod) {
		try {
			Set<Class<?>> classes = new Reflections(path).getTypesAnnotatedWith(annotationClass);
			final Map<K, V> liste = new HashMap<>();
			classes.forEach(klasse -> {
				try {
					V obj = (V) klasse.getConstructor().newInstance();
					var annotation = klasse.getAnnotation(annotationClass);
	                Method[] methods = annotation.annotationType().getDeclaredMethods();
	                for (Method method : methods) {
	                    Object value = method.invoke(annotation);
	                    if ( method.getName().equals(keyMethod) )
						liste.put((K) value, obj);
	                }
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new RuntimeException("itemClass="+klasse, e);
				}
			});
			return liste;
		}
		catch ( Exception e ) {
			throw new RuntimeException("Objekte können nicht per Refelction ermittelt werden!", e);
		}
	}

}
