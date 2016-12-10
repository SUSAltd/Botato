package botato.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import botato.fish.Fish;

public class SplitFishToMonths {

	/**
	 * @param args
	 * @throws JSONException 
	 * @throws ParseException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws JSONException, ParseException, IOException {
		List<Fish> fishList = new ArrayList<Fish>();
		File inputFile = new File("WebContent/fish-irc.rizon.net-#lelandcs.json");
		String outputFileName = "WebContent/test/fish-irc.rizon.net-#lelandcs";
			
		String jsonString = "";
		
		Scanner input = new Scanner(inputFile);
		while (input.hasNextLine()) {
			jsonString += input.nextLine();
		}
		
		if (jsonString.isEmpty()) {
			jsonString = "[]";
		}
		
		JSONArray fishListArr = new JSONArray(jsonString);
		Map<String, JSONArray> fishSeparated = new HashMap<String, JSONArray>();
		
		for (int i = 0; i < fishListArr.length(); i++) {
			JSONObject fishObj = (JSONObject) fishListArr.get(i);
			
			String fullDate = (String) fishObj.getString("dateCaught");
			String yearAndMonth = fullDate.substring(0, 7);
			if (!fishSeparated.containsKey(yearAndMonth)) {
				fishSeparated.put(yearAndMonth, new JSONArray());
			}
			fishSeparated.get(yearAndMonth).put(fishObj);
		}
		
		for (String key : fishSeparated.keySet()) {
			File outputFile = new File(outputFileName + '-' + key + ".json");
			outputFile.createNewFile();
			PrintStream output = new PrintStream(outputFile);
			String jsonText = fishSeparated.get(key).toString();
			output.println(jsonText);
		}

	}

}
