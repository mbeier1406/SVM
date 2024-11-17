package com.github.mbeier1406.SVM.impl;

import com.github.mbeier1406.SVM.MEM;
import com.github.mbeier1406.SVM.SVMException;

/**
 * Eine einfache Implementierung eines Hauptspeichers,
 * einfach ein Feld von Speicherworten (hier von Typ {@linkplain Short})
 * mit fester Größe.
 */
public class MEMShort implements MEM<Short>, MEM.Instruction<Short> {

	/** Definiert den Speicher */
	public final Short[] mem = new Short[1000];

	@Override
	public int getHighAddr() {
		return this.mem.length;
	}

	@Override
	public Short read(int addr) throws SVMException {
		return this.mem[checkAddr(addr)];
	}

	@Override
	public void write(int addr, Short data) throws SVMException {
		this.mem[checkAddr(addr)] = data;
	}

	/**
	 * Prüft eine angeforderte Adresse auf Zugriffsverletzung.
	 * @param addr die angeforderte Adresse
	 * @return die angeforderte Adresse wenn in ordnung
	 * @throws SVMException bei ungültigen Zugriffen
	 */
	private int checkAddr(int addr) throws SVMException {
		if ( addr < 0 || addr >= getHighAddr() )
			throw new SVMException("Ungültige Adresse: "+addr+" (max. "+getHighAddr()+")!");
		return addr;
	}

}
