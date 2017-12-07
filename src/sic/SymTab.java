package sic;

import java.util.HashMap;

public class SymTab {
	private HashMap<String, String> sym;

	public SymTab() {
		sym = new HashMap<>();
	}

	public boolean search(String label) {
		return sym.containsKey(label);
	}

	public void insert(String label, String location) {
		sym.put(label, location);
	}

	public String getSymLoc(String label) {
		return sym.get(label);
	}

	public String[] parse() {
		String line = sym.toString();
		line = line.replace('{', ' ');
		line = line.replace('}', ' ');
		line = line.trim();
		if (line.equals("")) {
			return new String[0];
		}
		line = line.replace(",", "");
		line = line.replace("=", " ");
		line = line.replace(" +", " ");
		String temp[] = line.split(" ");
		return temp;
	}

	// public static void main(String args[]) {
	// SymTab s = new SymTab();
	// s.insert("l", "1");
	// s.insert("g", "1");
	// String p[] = s.parse();
	// for (int i = 0; i < p.length; i += 2) {
	// System.out.println(p[i] + " " + p[i + 1]);
	// }
	// }
}
