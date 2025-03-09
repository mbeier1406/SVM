package com.github.mbeier1406.svm.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.github.mbeier1406.svm.GenericFactory;
import com.github.mbeier1406.svm.SVM;
import com.github.mbeier1406.svm.SVMException;
import com.github.mbeier1406.svm.cmd.Command;
import com.github.mbeier1406.svm.cmd.CommandInterface;
import com.github.mbeier1406.svm.instructions.Instruction;
import com.github.mbeier1406.svm.instructions.InstructionFactory;
import com.github.mbeier1406.svm.instructions.InstructionInterface;

public class SVMShort implements SVM {

	@Override
	public int run(URL programm) throws SVMException {

		GenericFactory<String, CommandInterface> gfc = new GenericFactory<>();
		Map<String, CommandInterface> commands = gfc.getItems("com.github.mbeier1406.svm.cmd", Command.class, "id");
		System.out.println("Map: "+commands);
		GenericFactory<Byte, InstructionInterface<Short>> gfi = new GenericFactory<>();
		Map<Byte, InstructionInterface<Short>> instructions = gfi.getItems(InstructionFactory.PACKAGE, Instruction.class, "code");
		System.out.println("Map: "+instructions);
		return 0;
	}

	public static void main(String[] args) throws MalformedURLException, SVMException {
		new SVMShort().run(null);
	}

}
