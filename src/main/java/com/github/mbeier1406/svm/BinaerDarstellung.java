package com.github.mbeier1406.svm;

import org.apache.commons.lang.StringUtils;

/**
 * Gibt den Wert eines Registers der {@linkplain SVM} in Binärdarstellung aus.
 * @param <T> Wortlänge der {@linkplain SVM}.
 */
public class BinaerDarstellung<T extends Number> {

	/** Liefert den Wert eines Speicherworts als Binär-String */
	public String getBinaerDarstellung(T val) {
		if ( val == null ) return "null";
		return switch (val) {
			case Byte b -> getBinaerDarstellung(b, 8);
			case Short s -> getBinaerDarstellung(s, 16);
			case Integer i -> getBinaerDarstellung(i, 32);
			case Long l -> getBinaerDarstellung(l, 64);
			default -> throw new IllegalArgumentException("Ungültiger Typ: "+ val.getClass());
		};
	}

	/** Liefert den Wert eines Speicherworts als Binär-String mit Wortlänge {@linkplain Byte} */
	private String getBinaerDarstellung(Byte b, int sizeOfType) {
		Byte maske = 1;
		var sb = new StringBuilder(StringUtils.repeat(" ", sizeOfType));
		for ( int i=0; i < sizeOfType; i++ ) {
			sb.setCharAt(sizeOfType-1-i, (b & maske) == 0 ? '0' : '1');
			maske = (byte) (maske << 1);
		}
		return sb.toString();
	}

	/** Liefert den Wert eines Speicherworts als Binär-String mit Wortlänge {@linkplain Short} */
	private String getBinaerDarstellung(Short s, int sizeOfType) {
		Short maske = 1;
		var sb = new StringBuilder(StringUtils.repeat(" ", sizeOfType));
		for ( int i=0; i < sizeOfType; i++ ) {
			sb.setCharAt(sizeOfType-1-i, (s & maske) == 0 ? '0' : '1');
			maske = (short) (maske << 1);
		}
		return sb.toString();
	}

	/** Liefert den Wert eines Speicherworts als Binär-String mit Wortlänge {@linkplain Integer} */
	private String getBinaerDarstellung(Integer j, int sizeOfType) {
		Integer maske = 1;
		var sb = new StringBuilder(StringUtils.repeat(" ", sizeOfType));
		for ( int i=0; i < sizeOfType; i++ ) {
			sb.setCharAt(sizeOfType-1-i, (j & maske) == 0 ? '0' : '1');
			maske = (int) (maske << 1);
		}
		return sb.toString();
	}

	/** Liefert den Wert eines Speicherworts als Binär-String mit Wortlänge {@linkplain Long} */
	private String getBinaerDarstellung(Long l, int sizeOfType) {
		Long maske = 1L;
		var sb = new StringBuilder(StringUtils.repeat(" ", sizeOfType));
		for ( int i=0; i < sizeOfType; i++ ) {
			sb.setCharAt(sizeOfType-1-i, (l & maske) == 0 ? '0' : '1');
			maske = (long) (maske << 1);
		}
		return sb.toString();
	}

}
