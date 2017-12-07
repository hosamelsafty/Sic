package sic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputIntermediateLine {
	private String label, opCode, operand, locCounter, comment;

	public InputIntermediateLine() {
		label = opCode = operand = comment = "";
		locCounter = new String();
	}

	public String getLabel() {
		return label;
	}

	public String getOpCode() {
		return opCode;
	}

	public String getOperand() {
		return operand;
	}

	public String getComment() {
		return comment;
	}

	public void setLine(String input) {
		// Regex
		String pattern = "^((\\w+)\\s+(\\*|\\w+)?\\s+((?!RSUB)(?!LTORG)(?!ORG)\\w+)\\s+(\\w+|((\\w+)\\s*[,]\\s*(\\w+))|=?[XxCc]'\\s*\\w*\\s*'|=\\*|\\w+\\[+-]\\w+|\\*|\\*[\\+\\-]\\w+|\\w+\\[+-]\\*)(\\s+.*)?\\s*)|(\\w+)?\\s+(RSUB|LTORG)\\s*(\\w+)?|\\w+\\s+(ORG)\\s+(\\w+(\\+|\\-\\w+)?(\\w+)?)?(\\s+.*)?\\s*$";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(input);
		if (m.matches()) {
//			for (int i = 0; i <m.groupCount(); i++) {
//				 System.out.println(i+" " +m.group(i));
//				 }
			if (m.group(1) != null) {
				if (m.group(3) != null) {
					label = m.group(3);
				} else {
					label = "";

				}
				locCounter = m.group(2);
				// System.out.println("loc:"+locCounter);
				if (m.group(4) != null) {
					opCode = m.group(4);
				} else {
					opCode = "";
				}
				// System.out.println("opCode:"+opCode);
				if (m.group(5) != null) {
					operand = m.group(5);
				} else {
					operand = "";
				}
				if (m.group(9) != null) {
					comment = m.group(9);
				} else {
					comment = "";
				}
				System.out.println("res"+opCode);
				 System.out.println("label:" + label);
					System.out.println("comment:" + comment);
					System.out.println("operand:" + operand);
			} else if (m.group(11) != null) {
				operand = "";
				if (m.group(10) != null) {
					label = m.group(10);
				} else {
					label = "";
				}
				if (m.group(11) != null) {
					opCode = m.group(11);
				} else {
					opCode = "";
				}
				if (m.group(12) != null) {
					comment = m.group(12);
				} else {
					comment = "";
				}
			}
				else if (m.group(13) != null) {
				opCode=m.group(13);
				if (m.group(14) != null) {
					operand = m.group(14);
				} else {
					operand = "";
				}
				if (m.group(17) != null) {
					comment = m.group(17);
				} else {
					comment = "";
				}
			}
//			System.out.println("res"+opCode);
//			 System.out.println("label:" + label);
//				System.out.println("comment:" + comment);
//				System.out.println("operand:" + operand);}
		}else {

			System.out.println("nill");
			label = opCode = operand = comment = "";
		
			
		}
	}
		// System.out.println("No:");
	

	// public static void main(String args[]) {
	// String pattern =
	// "^\\s*(\\w+)?\\s+(\\w+)\\s+(\\w+|((\\w+)\\s*,\\s*(\\w+)))\\s*$";
	// Pattern p = Pattern.compile(pattern);
	// Matcher matcher = p.matcher(" LDA k,l");
	// if (matcher.find()) {
	// // System.out.println("Full match: " + matcher.group(0));
	// for (int i = 0; i <= matcher.groupCount(); i++) {
	// System.out.println("Group " + i + ": " + matcher.group(i));
	// }
	// }
	// }
	public static void main(String[] args) {
		InputIntermediateLine line = new InputIntermediateLine();
		line.setLine("102b      *         BYTE      X'01'          ");

	}

	public String getLocCounter() {
		return locCounter;
	}

	public void setLocCounter(String locCounter) {
		this.locCounter = locCounter;
	}
}
