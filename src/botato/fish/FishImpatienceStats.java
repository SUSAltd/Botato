package botato.fish;

import java.io.File;
import java.util.LinkedHashSet;


public class FishImpatienceStats {
	private LinkedHashSet<Impatience> impatience;
	
	public FishImpatienceStats(File fileIn) {
		
		
		// import CSV file
		// something like "date, nick, was kicked?"
		// MAKE CSV PARSER
		
	}
	
	public void add(String date, String nick, boolean wasKicked) {
		impatience.add(new Impatience(date, nick, wasKicked));
	}
	
	public void save(File fileOut) {
		// CSV I guess
	}
	
	private class Impatience {
		String date;
		String nick;
		boolean wasKicked;
		
		public Impatience (String date, String nick, boolean wasKicked) {
			this.date = date;
			this.nick = nick;
			this.wasKicked = wasKicked;
		}
	}
}
