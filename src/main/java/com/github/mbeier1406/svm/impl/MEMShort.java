package com.github.mbeier1406.svm.impl;

import com.github.mbeier1406.svm.BinaerDarstellung;
import com.github.mbeier1406.svm.MEM;
import com.github.mbeier1406.svm.SVMException;

/**
 * Eine einfache Implementierung eines Hauptspeichers,
 * einfach ein Feld von Speicherworten (hier von Typ {@linkplain Short})
 * mit fester Größe.
 */
public class MEMShort implements MEM<Short>, MEM.Instruction<Short> {

	/** Definiert den Speicher */
	public final Short[] mem = new Short[1000];

	/** Zur Darstellung von Speicherwörtern im Binärformat */
	private BinaerDarstellung<Short> bd = new BinaerDarstellung<>();


	/** {@inheritDoc} */
	@Override
	public void clear() {
		for ( int i=getLowAddr(); i < getHighAddr(); i++ )
			mem[i] = 0;
	}

	/** {@inheritDoc} */
	@Override
	public int getHighAddr() {
		return this.mem.length-1;
	}

	/** {@inheritDoc} */
	@Override
	public Short read(int addr) throws SVMException {
		return this.mem[checkAddr(addr)];
	}

	/** {@inheritDoc} */
	@Override
	public void write(int addr, Short data) throws SVMException {
		this.mem[checkAddr(addr)] = data;
	}

	/** {@inheritDoc} */
	@Override
	public String getBinaryContentStringAt(int addr, int len) throws SVMException {
		try {
			String dmp = "";
			if ( len <=0 ) throw new SVMException("len <= 0: "+len);
			checkAddr(addr);
			checkAddr(addr+len);
			for ( int i=0; i < len; i++ )
				dmp += bd.getBinaerDarstellung(mem[addr+i]);
			return dmp;
		}
		catch ( SVMException e ) {
			throw new SVMException("Ungültiger Speicherzugriff ("+toString()+"): addr="+addr+"; len="+len+" - "+e.getLocalizedMessage());
		}
	}

	/**
	 * Prüft eine angeforderte Adresse auf Zugriffsverletzung.
	 * @param addr die angeforderte Adresse
	 * @return die angeforderte Adresse wenn in ordnung
	 * @throws SVMException bei ungültigen Zugriffen
	 */
	private int checkAddr(int addr) throws SVMException {
		if ( addr < 0 || addr > getHighAddr() )
			throw new SVMException("Ungültige Adresse: "+addr+" (max. "+getHighAddr()+")!");
		return addr;
	}

	@Override
	public String toString() {
		return "MEMShort: len="+this.mem.length;
	}

}
