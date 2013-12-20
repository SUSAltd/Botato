import org.jibble.pircbot.*;
import java.util.*;
import java.io.*;

public class TestBot extends PircBot {
	Scanner input;
	PrintStream output;
	Random rand;

	String fishListFile;
	String fishOutFile;
	File fishData;
	GrammarSolver fishSolver;
	FishManager fishManager;

	public TestBot(String fishListFile) throws IOException {
		rand = new Random();

		// initializes fish
		List<Fish> fishList = new ArrayList<Fish>();
		fishData = new File("data/fishdata.csv");
		if (!fishData.createNewFile()) {
			input = new Scanner(fishData);
			String[] line;
			input.nextLine(); // skips first line (headers)
			while (input.hasNextLine()) {
				line = input.nextLine().split(","); // [dateCaught,catcher,name,weight]
				for (int i = 0; i < line.length; i++) {
					String s = line[i];
					if (s.startsWith("\"") && s.endsWith("\"")) {
						line[i] = s.substring(1, s.length());
					}
				}
				Fish newFish = new Fish(line[2], line[1],
						Double.parseDouble(line[3]), line[0]);
				fishList.add(newFish);
			}
		}
		fishManager = new FishManager(fishList);

		input = new Scanner(new File(fishListFile));

		List<String> fishGrammar = new ArrayList<String>();
		while (input.hasNextLine()) {
			String next = input.nextLine().trim();
			if (next.length() > 0) {
				fishGrammar.add(next);
			}
		}
		fishSolver = new GrammarSolver(
				Collections.unmodifiableList(fishGrammar));

	}

	public void onKick(String channel, String kickerNick, String kickerLogin,
			String kickerHostname, String recipientNick, String reason) {
		sendMessage(channel, "AND STAY OUT");
	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		// !fish
		if (message.equals("!fish")) {
			if (System.currentTimeMillis() - fishManager.getLastFishCatchTime() >
					(30 + rand.nextInt(30)) * 1000) {
				try {
					fish(channel, sender, message);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				sendNotice(sender, "The fish aren't biting right now!");
			}
			
		// !fishstats
		} else if (message.startsWith("!fishstats")) {
			fishStats(sender, message);
			
		// !fishexport
		} else if (message.equals("!fishexport")) {
			dccSendFile(fishData, sender, 120000);
			
		// !fish help
		} else if (message.equals("!fish help")) {
			fishHelp(sender);

		// botate
		} else if (message.equalsIgnoreCase("botato botate")) {
			sendAction(channel, "botate botate botate");
		}
	}

	private void fish(String channel, String sender, String message)
			throws FileNotFoundException {
		Fish newFish = new Fish("temp", sender);
		String newName;

		if (newFish.weight() > Fish.MEAN_WEIGHT + 2 * Fish.STD_DEV_WEIGHT) {
			newName = fishSolver.generate("<fish_big>", 1)[0];
		} else if (newFish.weight() < Fish.MEAN_WEIGHT - 1.5
				* Fish.STD_DEV_WEIGHT) {
			newName = fishSolver.generate("<fish_sml>", 1)[0];
		} else {
			newName = fishSolver.generate("<fish_avg>", 1)[0];
		}

		newFish.setName(newName);

		int place = fishManager.catchFish(newFish) + 1;

		saveFish();

		String outMessage = sender + " caught a";
		if (newFish.name().matches("[aeiouAEIOU].*")) {
			outMessage += "n";
		}
		outMessage += " " + newFish.name() + " weighing " + newFish.weight()
				+ " lbs.!";

		sendMessage(channel, Colors.BLUE + "F" + Colors.PURPLE + "I"
				+ Colors.RED + "S" + Colors.OLIVE + "H" + Colors.YELLOW + "!!");
		sendMessage(channel, outMessage);
		if (fishManager.lastCaughtIsBiggest()) {
			sendMessage(channel, Colors.BOLD + Colors.RED + "NEW RECORD! ");
		}
		if (place <= 10) {
			sendMessage(channel, newFish.catcher() + "'s " + newFish.name() + 
					" broke the nr " + place + " record weight!");
		}
	}
	
	private void fishStats(String sender, String message) {
		int tot;
		double avg;
		Fish big;
		Fish sml;
		
		if (message.length() > 11 && message.charAt(10) == ' ') {
			String target = message.substring(11);
			try {
				tot = fishManager.getFishList(target).size();
				avg = fishManager.getAverageWeight(target);
				big = fishManager.getBiggest(target);
				sml = fishManager.getSmallest(target);
				
				if (tot == 1) {
					sendNotice(sender, target + " has caught only one fish: a " + big.name() + 
							" weighing " + big.weight());
				} else {
					sendNotice(sender, target + " has caught " + tot + 
							" fish averaging " + avg + " lbs.");
					sendNotice(sender, target + "'s biggest fish was a " + big.name() + 
							" weighing " + big.weight() + " lbs.");
					sendNotice(sender, target + "'s smallest fish was a " + sml.name() + 
							" weighing " + sml.weight() + " lbs.");
				}
			} catch (Exception e) {
				if (target.equals(sender))
					sendNotice(sender, "You have not caught any fish!");
				else
					sendNotice(sender, target + " has not caught any fish!");
			}
		} else {
			tot = fishManager.getFishList().size();
			avg = fishManager.getAverageWeight();
			big = fishManager.getBiggest();
			sml = fishManager.getSmallest();
			
			sendNotice(sender, "There have been a total of " + tot + 
					" fish caught averaging " + avg + " lbs.");
			sendNotice(sender, big.catcher() + " holds the record for biggest fish with a " + 
					big.name() + " weighing " + big.weight() + " lbs.");
			if (big.catcher().equals(sml.catcher())) {
				sendNotice(sender, sml.catcher() + " also holds the record for smallest fish " +
						"with a " + sml.name() + " weighing " + sml.weight() + " lbs.");
			} else {
				sendNotice(sender, "On the other end, " + sml.catcher() + " holds the record " +
						"for smallest fish with a " + sml.name() + " weighing " + sml.weight() + 
						" lbs.");
			}
		}
	}
	
	private void fishHelp(String sender) {
		sendNotice(sender, "!fish - Catch a fish! If the fish are not biting, " +
				"wait a bit and try again.");
		sendNotice(sender, "!fishstats - See some statistics of the total fish caught.");
		sendNotice(sender, "!fishstats <nick> - See some statistics of the fish caught " +
				"by a specified nick.");
		sendNotice(sender, "!fishexport - Save the fish database as a CSV file " +
				"for viewing in programs such as Microsoft Excel.");
	}

	private void saveFish() throws FileNotFoundException {
		Date lastUpdate = new Date(System.currentTimeMillis());
		
		output = new PrintStream(fishData);
		output.println("\"Date caught\",\"Catcher\",\"Type\",\"Weight\",\"Last updated: " + 
				lastUpdate.toString() + "\"");
		for (Fish f : fishManager.getFishList()) {
			output.println(f.dateCaught() + "," + f.catcher() + "," + f.name()
					+ "," + f.weight());
		}
	}

	public static void main(String[] args) throws Exception {
		Properties p = new Properties();
		p.load(new FileInputStream(new File("bot.ini")));

		String fishList = p.getProperty("fish_list_file");
		if (fishList == null) {
			throw new FileNotFoundException("The property fish_list_file"
					+ "was not found in bot.ini.");
		}

		TestBot bot = new TestBot(fishList);
		bot.setVerbose(true);
		bot.setName(p.getProperty("nick"));
		bot.connect(p.getProperty("server"));
		bot.joinChannel(p.getProperty("channel"));
	}
}
