package com.github.mbeier1406.svm.prg.parser.functions;

import java.util.HashMap;
import java.util.Map;

import com.github.mbeier1406.svm.prg.SVMProgram.Data;

public class FunctionFactory {

	@SuppressWarnings({ "serial" })
	private static final Map<String, Function> MAP = new HashMap<String, Function>() {{

		/*
		 * Die Funktion len() erhält ein Symbol mit einer Labelreferenz als Parameter
		 * und liefert die Länge der zugehörigen Datendefinition als Wert.
		 * Beispiel: /SVM/src/test/resources/com/github/mbeier1406/svm/prg/example.svm
		 * 	&data
		 * ...
		 * .text2
		 * 	XY
		 * ...
		 * &code
		 * ...
		 * 	mov len(text2), %3
		 * ...
		 */
		put("len", ( symbol, svmProgram ) -> {
			Data<Short> data = svmProgram
				.getDataList()
				.stream()
				.filter(dat -> dat.label().label().equals(symbol.getStringValue().get()))
				.findAny()
				.orElseThrow(IllegalArgumentException::new);
			return data.dataList().length;
		});

	}};

	public static Function getFunction(String f) {
		return MAP.get(f);
	}

}
