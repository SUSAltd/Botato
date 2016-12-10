package botato.util;
import java.util.Scanner;


public class SplitLines {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner console = new Scanner(System.in);
		while (console.hasNextLine()) {
			String line = console.nextLine();
			System.out.println("\"" + line.trim() + "\",");
		}
	}

}
