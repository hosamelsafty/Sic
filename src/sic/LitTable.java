package sic;

import java.util.HashMap;
import java.util.Stack;

public class LitTable {
	private Stack<String> letSatck=new Stack<>();
	private HashMap<String, Triplet> litTable;

	public LitTable() {
		litTable = new HashMap<>();
	}

	public void insert(String literal) {
		
		if(!letSatck.contains(literal)&&!litTable.containsKey(literal))letSatck.add(literal);
		}

	public boolean search(String literal) {
		return litTable.containsKey(literal);
	}

	

	public Triplet getTriplet(String literal) {
		return litTable.get(literal);
	}
	public String[] parse() {
		String line = litTable.toString();
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
	public Object getLiterals() {
		Object o = litTable.clone();
		litTable.clear();
		return o;
	}
	public String pop(String address){
		String literal=letSatck.pop();
		int length;
		String hexValue = "";
		if (literal.startsWith("C") || literal.startsWith("c")) {
			length = literal.length() - 3;
			for (int i = 2; i < literal.length() - 1; i++) {
				hexValue += Integer.toHexString(literal.charAt(i));
			}
		} else {
			length = (literal.length() - 3) / 2;
			hexValue += literal.substring(2, literal.length());
		}
		litTable.put(literal, new Triplet(length, hexValue,address));

	return literal;
	}
	public boolean isEmpty(){
		return letSatck.isEmpty();
	}
	
}
