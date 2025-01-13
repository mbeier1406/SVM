package com.github.mbeier1406.svm.prg;

import static org.apache.logging.log4j.CloseableThreadContext.put;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.mbeier1406.svm.SVMException;

/**
 * Die Standardimplementierung zum Speichern und Laden sienes {@linkplain SVMProgram}s
 * verwenden den {@linkplain ObjectOutputStream} bzw. den {@linkplain ObjectInputStream}.
 */
public class SVMSourceShort implements SVMSource<Short> {

	public static final Logger LOGGER = LogManager.getLogger(SVMSourceShort.class);

	/** {@inheritDoc} */
	@Override
	public void save(SVMProgram<Short> program, File target) throws SVMException {
		try ( @SuppressWarnings("unused") CloseableThreadContext.Instance ctx = put("program", program.toString()).put("target", target.toString()) ) {
			try ( ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(target)) ) {
				LOGGER.info("Schreibe File...");
				out.writeObject(program);
				out.flush();
			}
		} catch (IOException e) {
			throw new SVMException("program="+program+"; File="+target, e);
		}
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public SVMProgram<Short> load(URL source) throws SVMException {
		try ( @SuppressWarnings("unused") CloseableThreadContext.Instance ctx = put("source", source.toString()) ) {
			SVMProgram<Short> prg;
			var c = source.openConnection();
			try ( ObjectInputStream in = new ObjectInputStream(c.getInputStream()) ) {
				LOGGER.info("Lese URL...");
				prg = (SVMProgram<Short>) in.readObject();
			}
			LOGGER.info("prg= {}", prg);
			return prg;
		} catch (ClassNotFoundException | IOException e) {
			throw new SVMException("URL="+source, e);
		}
	}

}
