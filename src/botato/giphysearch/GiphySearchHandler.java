package botato.giphysearch;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GiphySearchHandler {
	
	Random r;
	
	public GiphySearchHandler() {
		
		r = new Random();
		
	}

	public JSONObject fetchMemes(String searchQuery) throws IOException, JSONException {
		String[] keywordsArray = searchQuery.split(" ");
		String keywords = "";
		for (int i = 0; i < keywordsArray.length - 1; i++) {
			keywords += keywordsArray[i] + '+';
		}
		keywords += keywordsArray[keywordsArray.length - 1];
		URL giphy = new URL("http://api.giphy.com/v1/gifs/search?api_key=dc6zaTOxFJmzC&limit=100&q=" + keywords);
		URLConnection yc = giphy.openConnection();
		Scanner input = new Scanner(yc.getInputStream());
		
		String jsonString = "";
		while (input.hasNextLine()) {
			jsonString += input.nextLine();
		}
		
		input.close();
		
		return new JSONObject(jsonString);
	}

	public String getRandomGifUrl(String searchQuery) throws IOException, JSONException {
		JSONObject jsonMemes = fetchMemes(searchQuery);
		JSONArray arrayOfMemes = jsonMemes.getJSONArray("data");
		if (arrayOfMemes.length() == 0) {
			throw new IndexOutOfBoundsException("No GIFs found");
		}
		
		int i = r.nextInt(arrayOfMemes.length());
		String url = arrayOfMemes.
				getJSONObject(i).
				getJSONObject("images").
				getJSONObject("original").
				getString("url");
		return url;
	}
}
