package botato.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;


/**
 * This class is to fix the fish CSV file because I messed it up when I did stuff with it 
 * in MS Excel.
 * 
 * Don't use MS Excel to do that sort of thing.
 * 
 * @author Ray
 *
 */
public class FixCSV {

	/**
	 * @param args matey
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Scanner console = new Scanner(System.in);
		
		System.out.print("File with dates to be fixed: ");
		String fileName = console.nextLine();
		Scanner input = new Scanner(new File(fileName));
		PrintStream output = new PrintStream(new File(fileName + "_new"));
		
		while (input.hasNextLine()) {
			String line = input.nextLine();
			String[] tokens = line.split(",", 2);
			String firstToken = tokens[0];
			String newLine = "";
			if (firstToken.matches("\"\\d\\d?/\\d\\d?/\\d\\d\\d\\d \\d\\d?:\\d\\d\"")) { // mm/dd/yyyy hh:MM
				String s = firstToken.substring(1, firstToken.length() - 1); // remove "
				String[] dateTokens = s.split(" "); // ["mm/dd/yyyy", "hh:MM"]
				String[] date = dateTokens[0].split("/");
				String[] time = dateTokens[1].split(":");
				String month = addZeroesToBeginning(date[0], 2);
				String day = addZeroesToBeginning(date[1], 2);
				String year = addZeroesToBeginning(date[2], 4);
				String hour = addZeroesToBeginning(time[0], 2);
				String minute = addZeroesToBeginning(time[1], 2);
				String second = "00";
				
				String newDate = "\"" + year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + "\",";
				newLine = newDate + tokens[1];
			} else {
				newLine = line; // no change
			}
			output.println(newLine);
		}
	}
	
	private static String addZeroesToBeginning(String s, int length) {
		String result = s;
		String zeroes = "";
		for (int i = 0; i < length - s.length(); i++) {
			zeroes += "0";
		}
		result = zeroes + result;
		return result;
	}

}
