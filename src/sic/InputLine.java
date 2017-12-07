package sic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputLine {
	private String label, opCode, operand, comment;

	public InputLine() {
		label = opCode = operand = comment = "";
	}

	public String getLabel() {
		return label;
	}

	public String getOpCode() {
		return opCode.toUpperCase();
	}

	public String getOperand() {
		return operand;
	}

	public String getComment() {
		return comment;
	}

	public void setLine(String input) {

		// Regex
		String pattern = "^((\\w+)?\\s+((?!RSUB)(?!ORG)(?!LTORG)\\w+)\\s+(\\w+|((\\w+)\\s*,\\s*(\\w+))|=?[XCcx]'\\s*\\w*\\s*'|=\\*|\\w+[\\+\\-]\\w+|\\*|\\*[\\+\\-]\\w+|\\w+[\\+\\-]\\*)(\\s+.*)?\\s*)|(\\w+)?\\s+(RSUB|LTORG)\\s*(\\w+)?|\\s+(ORG)\\s+(\\w+(\\+|\\-\\w+)?(\\w+)?)?(\\s+.*)?\\s*$";

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(input);

		if (m.matches()) {
//			for (int i = 0; i < m.groupCount(); i++) {
//				System.out.println(m.group(i)+"  "+i);
//			}
			
			if (m.group(1) != null) {
				if (m.group(2) != null) {
					label = m.group(2);
				} else {
					label = "";
				}
				if (m.group(3) != null) {
					opCode = m.group(3);
				} else {
					opCode = "";
				}
				if (m.group(4) != null) {
					operand = m.group(4);
				} else {
					operand = "";
				}
				if (m.group(8) != null) {
					comment = m.group(8);
				} else {
					comment = "";
				}
				System.out.println("label:" + label);
				System.out.println("opCode:" + opCode);
				System.out.println("operand:" + operand);
				System.out.println("comment: " + comment);
			} else if (m.group(10) != null) {

				operand = "";

				if (m.group(9) != null) {
					label = m.group(9);
				} else {
					label = "";
				}
				if (m.group(10) != null) {
					opCode = m.group(10);
				} else {
					opCode = "";
				}
				if (m.group(11) != null) {
					comment = m.group(11);
				} else {
					comment = "";
				}
				System.out.println("label:" + label);
				System.out.println("opCode:" + opCode);
				System.out.println("operand:" + operand);
				System.out.println("comment: " + comment);

			} else if (m.group(12) != null) {
				label="";
				opCode = m.group(12);
				if (m.group(13) != null) {
					operand = m.group(13);
				} else {
					operand = "";
				}
				if (m.group(16) != null) {
					comment = m.group(16);
				} else {
					comment = "";
				}
				System.out.println("label:" + label);
				System.out.println("opCode:" + opCode);
				System.out.println("operand:" + operand);
				System.out.println("comment: " + comment);
				
			}

		} else {
			System.out.println("nill");
			label = opCode = operand = comment = "";
		}
	}

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
		InputLine line = new InputLine();
		line.setLine(" STR      =*");
	}
}
