
import java.io.PrintWriter;
import java.util.Date;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Leslie L Foster
 */
public class PrintThing {
	public static void main(String[] args) throws Exception {

		PrintWriter output = new PrintWriter("sample_text.txt");
		Random rng = new Random(new Date().getTime());

		for (int i = 0; i < 100; i++) {
			int number = rng.nextInt(10);
			output.print(number + " ");
			//output.print(number + ' ');
		}
		output.close();
	}
}
