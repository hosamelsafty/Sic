package sic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class Pass1 {
	private SymTab symTab;
	private int locCtr;
	private OpTab opTab;
	private InputLine line;
	private File intermediate;
	private int progLength;
	private String error;
	private boolean errorFlag;
	private PrintWriter p;
	private String oper;
	private String[] resWords;
	private LitTable litTab;
	private int beforeLoc;
	private String ORGoper;
	private int beforeLocCtr;
	
	
	public Pass1() {
		
		symTab = new SymTab();
		locCtr = 0;
		beforeLoc = 0;
		ORGoper = "";
		line = new InputLine();
		intermediate = new File("Intermediate.txt");
		opTab = new OpTab();
		error = "";
		errorFlag = false;
		oper = "";
		resWords = new String[] { "BYTE", "RESB", "RESW", "WORD" };
		litTab = new LitTable();
		try {
			p = new PrintWriter(new FileOutputStream(intermediate));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public File getIntermediatFile(File asmFile) {
		Scanner in;
		try {
			in = new Scanner(asmFile);
			String temp = null;
			while (in.hasNextLine()) {
				temp = in.nextLine();
				if (temp.length() > 0 && temp.charAt(0) != '.') {
					break;
				}
				// write comments as they are
				p.println(temp);
			}
			int startAddress = 0;
			line.setLine(temp);
			if (line.getOpCode().equals("START")) {
				try {
					// Address is always regarded as a hexadecimal value
					if (line.getOperand().startsWith("0x")) {
						locCtr = Integer.parseInt(line.getOperand().substring(2, line.getOperand().length()), 16);
					} else {
						locCtr = Integer.parseInt(line.getOperand(), 16);
					}
					
				} catch (Exception e) {
					// In case of invalid address
					locCtr = 0;
				}
				beforeLocCtr=locCtr;
				startAddress = locCtr;
				symTab.insert(line.getLabel(), Integer.toHexString(locCtr).toUpperCase());
				writeToIntermediate(line);
			}
			while (in.hasNextLine()) {
				beforeLocCtr=locCtr;
				writeError();
				String s = in.nextLine();
				while (s.length() > 0 && s.charAt(0) == '.' && in.hasNextLine()) {
					p.println(s);
					s = in.nextLine();
				}
				error = "";
				line.setLine(s);
				if (line.getOpCode().equals("END")) {

					writeToIntermediate(line);
					progLength = locCtr - startAddress;
					while (!litTab.isEmpty()) {
						String litter = litTab.pop(Integer.toHexString(locCtr));
						p.println(Integer.toHexString(locCtr) + "      *                   " + litter);
						locCtr+=litTab.getTriplet(litter).getLength();
					}
					break;
				}

				if (!line.getLabel().equals("")) {
					if (line.getLabel().length() > 8) {
						error += "Long label       ";
						errorFlag = true;
					}
					if (!Character.isAlphabetic(line.getLabel().charAt(0))) {
						error += "Label name must start with a letter     ";
						errorFlag = true;
					}
					if (symTab.search(line.getLabel())) {
						// setErrorFlag
						error = "Duplicate label   ";
						errorFlag = true;
					} else if (opTab.search(line.getLabel().toUpperCase())
							|| Arrays.binarySearch(resWords, line.getLabel().toUpperCase()) >= 0) {
						error = "Reserved word     ";
						errorFlag = true;
					} else {
						symTab.insert(line.getLabel(), Integer.toHexString(locCtr).toUpperCase());
					}
				}
				writeToIntermediate(line);
				if (opTab.search(line.getOpCode())) {
					locCtr += 3;
					if ((line.getOperand().startsWith("=C'") || line.getOperand().startsWith("=X'")
							|| line.getOperand().startsWith("=c'") || line.getOperand().startsWith("=x'"))
							&& line.getOperand().endsWith("'")) {
						litTab.insert(line.getOperand().substring(1, line.getOperand().length()));
						if ("Cc".contains("" + line.getOperand().charAt(1))) {
//							if(line.getOperand().length() > 7){
//								errorFlag = true;
//								error += "Long operand       ";
//							}
						}
						else{
							if(line.getOperand().length() % 2 == 1){
								errorFlag = true;
								error += "Illegal hexadecimal literal         ";
							}
						}
					}
					else if (line.getOperand().equals("=*")){
						String hex=Integer.toHexString(beforeLocCtr);
						while (hex.length()<6){
							hex="0"+hex;
							}
						hex="X'" + hex + "'";
						litTab.insert(hex);
						
					}
				} else if (line.getOpCode().equals("WORD")) {
					locCtr += 3;

				} else if (line.getOpCode().equals("RESW")) {
					locCtr += 3 * Integer.parseInt(line.getOperand());

				} else if (line.getOpCode().equals("RESB")) {
					locCtr += Integer.parseInt(line.getOperand());

				} else if (line.getOpCode().equals("BYTE")) {
					oper = line.getOperand();
					// BYTE starts only with 'X', or 'C'. No 'W';
					if (oper.length() > 0 && (oper.startsWith("C'") || oper.startsWith("X'") || oper.startsWith("c'")
							|| oper.startsWith("x'")) && oper.endsWith("'")) {
						if (oper.startsWith("C'") || oper.startsWith("c'")) {
							locCtr += oper.length() - 3;

						} else {
							// locCtr += Integer.parseInt(oper.substring(2,
							// oper.length() - 1), 16);
							if (oper.length() % 2 == 0) {
								error += "Illegal hexadecimal value     ";
								errorFlag = true;
							} else {
								locCtr += (oper.length() - 3) / 2;

							}
						}
					} else {
						// setErrorFlag
						error += "Undefined definition    ";
						errorFlag = true;
					}
				} else if (line.getOpCode().equals("ORG") && !line.getOperand().equals("")) {
					beforeLoc = locCtr;
					// System.out.println("beforeORGonly"+Integer.toHexString(locCtr).toUpperCase());
					ORGoper = line.getOperand();
					if (line.getOperand().contains("+")) {
						String shift = line.getOperand().substring(0, line.getOperand().indexOf("+"));
						if (symTab.search(shift)) {
							String disp = line.getOperand().substring(line.getOperand().indexOf("+") + 1,
									line.getOperand().length());
							locCtr = Integer.parseInt(symTab.getSymLoc(shift), 16) + Integer.parseInt(disp);
						} else {
							errorFlag=true;
							error+="error shift operand is not in the symtab";
						}
					} else if (line.getOperand().contains("-")) {
						String shift = line.getOperand().substring(0, line.getOperand().indexOf("-"));
						if (symTab.search(shift)) {
							String disp = line.getOperand().substring(line.getOperand().indexOf("-") + 1,
									line.getOperand().length());
							locCtr = Integer.parseInt(symTab.getSymLoc(shift), 16) - Integer.parseInt(disp);
						} else {
							errorFlag=true;
							error+="error shift operand is not in the symtab";
						}
					} else {
						String shift = line.getOperand();
						if (symTab.search(shift)) {
							locCtr = Integer.parseInt(symTab.getSymLoc(shift), 16);
						} else {

							errorFlag=true;
							error+="error shift operand is not in the symtab";   
						
							// 
						}
					}
				} else if (line.getOpCode().equals("ORG") && line.getOperand().equals("")) {
					locCtr = beforeLoc;
					// System.out.println("AfterORGonly"+Integer.toHexString(locCtr).toUpperCase());
				} else if (line.getOpCode().equalsIgnoreCase("LTORG")) {
					while (!litTab.isEmpty()) {
						String litter = litTab.pop(Integer.toHexString(locCtr));
						p.println(Integer.toHexString(locCtr) + "      *                   " + litter);
						locCtr+=litTab.getTriplet(litter).getLength();
					}

				} else if (line.getOpCode().equalsIgnoreCase("EQU") && !line.getOperand().equals("")
						&& !line.getLabel().equals("")) {

					if (isNumeric(line.getOperand()))
						symTab.insert(line.getLabel(),
								Integer.toHexString(Integer.parseInt(line.getOperand())).toUpperCase());
					else if (symTab.search(line.getOperand()))
						symTab.insert(line.getLabel(), symTab.getSymLoc(line.getOperand()));
					else if (line.getOperand().contains("+")) {

						String label[] = line.getOperand().split("\\+");
						if (label.length == 2) {
							if (symTab.search(label[0]) && symTab.search(label[1]))
								symTab.insert(line.getLabel(),
										Integer.toHexString(Integer.parseInt(symTab.getSymLoc(label[1]), 16)
												+ Integer.parseInt(symTab.getSymLoc(label[0]), 16)));

							else if (symTab.search(label[0]) && isNumeric(label[1]))
								// symTab.insert(line.getLabel(),
								// Integer.toHexString(
								// Integer.parseInt(label[1]) +
								// Integer.parseInt(symTab.getSymLoc(label[0]))));
								symTab.insert(line.getLabel(), Integer.toHexString(
										Integer.parseInt(label[1]) + Integer.parseInt(symTab.getSymLoc(label[0]), 16)));

							else if (symTab.search(label[1]) && isNumeric(label[0]))
								symTab.insert(line.getLabel(), Integer.toHexString(
										Integer.parseInt(label[0]) + Integer.parseInt(symTab.getSymLoc(label[1]), 16)));
							else if (isNumeric(label[0]) && isNumeric(label[1]))
								symTab.insert(line.getLabel(),
										Integer.toHexString(Integer.parseInt(label[0]) + Integer.parseInt(label[1])));
							else if (isNumeric(label[0].trim()) && label[1].trim().equals("*")) {
								symTab.insert(line.getLabel(),
										Integer.toHexString(Integer.parseInt(label[0]) + locCtr));
							} else if (isNumeric(label[1].trim()) && label[0].trim().equals("*")) {
								symTab.insert(line.getLabel(),
										Integer.toHexString(Integer.parseInt(label[1]) + locCtr));
							} else {
								errorFlag=true;
								error+="EQU misuse     ";   
							}
						}
					} else if (line.getOperand().equals("*")) {
						symTab.insert(line.getLabel(), Integer.toHexString(locCtr));
					} else if (line.getOperand().contains("-")) {

						String label[] = line.getOperand().split("\\-");
						if (label.length == 2) {
							if (symTab.search(label[0]) && symTab.search(label[1]))
								symTab.insert(line.getLabel(),
										Integer.toHexString(Integer.parseInt(symTab.getSymLoc(label[0]), 16)
												- Integer.parseInt(symTab.getSymLoc(label[1]), 16)));

							else if (symTab.search(label[0]) && isNumeric(label[1]))
								// symTab.insert(line.getLabel(),
								// Integer.toHexString(
								// Integer.parseInt(label[1]) +
								// Integer.parseInt(symTab.getSymLoc(label[0]))));
								symTab.insert(line.getLabel(), Integer.toHexString(
										Integer.parseInt(symTab.getSymLoc(label[0]), 16) - Integer.parseInt(label[1])));

							else if (symTab.search(label[1]) && isNumeric(label[0]))
								symTab.insert(line.getLabel(), Integer.toHexString(
										Integer.parseInt(label[0]) - Integer.parseInt(symTab.getSymLoc(label[1]), 16)));
							else if (isNumeric(label[0]) && isNumeric(label[1]))
								symTab.insert(line.getLabel(),
										Integer.toHexString(Integer.parseInt(label[0]) - Integer.parseInt(label[1])));
							else if (isNumeric(label[0].trim()) && label[1].trim().equals("*")) {
								symTab.insert(line.getLabel(),
										Integer.toHexString(Integer.parseInt(label[0]) - locCtr));
							} else if (isNumeric(label[1].trim()) && label[0].trim().equals("*")) {
								symTab.insert(line.getLabel(),
										Integer.toHexString(locCtr - Integer.parseInt(label[1])));
							} else {

								errorFlag=true;
								error+="EQU misuse     ";   
							
							}
						}
					} else {

						errorFlag=true;
						error+="Error !!!!!!     ";   
					

					}
				} else {
					// setErrorFlag
					error += "Undefined definition        ";
					errorFlag = true;
				}
				// From column 18 to column 35
				if (line.getOperand().length() > 18) {
					error += "Long operand        ";
					errorFlag = true;
				}
				// writeToIntermediate(line);
			}
			writeMap();
			writelitTabMap();
			in.close();
			p.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return intermediate;
	}

	public int getProgLength() {
		return progLength;
	}

	private void writeToIntermediate(InputLine in) {
		Indent(Integer.toHexString(locCtr).toUpperCase(), 10);
		Indent(in.getLabel(), 10);
		Indent(in.getOpCode(), 7);
		Indent(in.getOperand(), 7);

		if (in.getComment() != "") {
			Indent(in.getComment(), in.getComment().length());
		}
		p.println();

	}

	private void writeError() {
		if (error.length() > 0) {
			p.println("." + error);
		}
	}

	private void writeMap() {
		String map[] = symTab.parse();
		p.println("---------------------");
		for (int i = 0; i < map.length; i += 2) {
			p.println(map[i] + " | " + map[i + 1]);
		}
		p.println("---------------------");
	}
	private void writelitTabMap() {
		String map[] = litTab.parse();
		p.println("---------------------");
		for (int i = 0; i < map.length; i += 2) {
			p.println(map[i] + " | " + map[i + 1]);
		}
		p.println("---------------------");
	}

	private void Indent(String s, int limit) {
		p.print(s);
		int i = 0;
		while (s.length() + i < limit) {
			p.print(" ");
			i++;
		}
	}

	public boolean hasErrors() {
		return errorFlag;
	}

	public SymTab getSymTab() {
		return symTab;
	}

	public OpTab getOpTab() {
		return opTab;
	}

	private boolean isNumeric(String s) {
		boolean a = s != null && s.matches("[-+]?\\d+");
		return a;
	}

	public LitTable getLitTab() {
		return litTab;
	}

	public void setLitTab(LitTable litTab) {
		this.litTab = litTab;
	}
}
