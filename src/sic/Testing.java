package sic;

import java.io.File;
import java.io.IOException;

public class Testing {

	public static void main(String[] args) {
		Pass1 pass = new Pass1();
		File intermediate = pass.getIntermediatFile(new File("1.asm"));
		intermediate.exists();
		Pass2 pass2 = new Pass2(pass);
		// System.out.println("hossam");
		try {
			File f = pass2.getObjectCodetFile(intermediate);
			f.exists();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
