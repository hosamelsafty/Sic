package sic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class OpTab {
	private HashMap<String, String> opTab;

	public OpTab() {
		opTab = new HashMap<>();
		fillMap();
	}

	public boolean search(String opCode) {
		return opTab.containsKey(opCode);
	}

	public String getLength(String opCode) {
		return opTab.get(opCode);
	}

	private void fillMap() {
		try {
			Scanner in = new Scanner(new File("Mnemonics.txt"));
			while (in.hasNextLine()) {
				//To make it case insensitive
				opTab.put(in.next().toUpperCase(), Integer.toHexString(in.nextInt()));
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
