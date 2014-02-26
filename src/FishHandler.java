import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

//import au.com.bytecode.opencsv.CSVReader;
//import au.com.bytecode.opencsv.CSVWriter;

import org.jibble.pircbot.Colors;

public class FishHandler {
	private TestBot bot;
	private Random rand;
	private HashMap<String, Thread> fishers;
	private HashMap<String, Bait> baitsMap;
	private HashMap<String, Integer> impatience;
	private GrammarSolver fishGrammar;
	private File fishData;
	private int coolDown;
	private Calendar c;

	private PrintStream output;
	private Scanner input;

	private FishManager fm;

	private int maxFishers = 1; // max people fishing at once: 0 or less for no
									// maximum
	private int maxReelWait = 0; // max amount of time to wait for fish in
									// seconds: 0 or less for no !reel
	private int baseCoolDown = 30; // minimum amount of wait (s) before another fish
	
	private int impatienceLimit = 3; // max number of "fish aren't biting" before kick

	public FishHandler(TestBot bot, File fishData) throws IOException {
		this.bot = bot;
		this.rand = new Random();
		this.fishers = new HashMap<String, Thread>();
		this.baitsMap = new HashMap<String, Bait>();
		this.impatience = new HashMap<String, Integer>();
		String[] g = FishGrammars.getFishGrammar(FishGrammars.Holiday.DEFAULT);
		List<String> grammar = Arrays.asList(g);
		this.fishGrammar = new GrammarSolver(grammar);

		// who needs csv parsers when you've got this stuff
		List<Fish> fishList = new ArrayList<Fish>();
		this.fishData = fishData;
		if (!fishData.createNewFile()) {
			input = new Scanner(fishData);
			String[] line;
			if (input.hasNextLine())
				input.nextLine(); // skip first line for headers
			while (input.hasNextLine()) {
				// line[] format = 
				// [dateCaught, catcher, name, weight, reactionTime, baitUsed, ___]
				line = input.nextLine().split(",");
				for (int i = 0; i < line.length; i++) {
					String s = line[i];
					if (s.startsWith("\"") && s.endsWith("\"")) {
						line[i] = s.substring(1, s.length() - 1);
					}
				}
				Fish newFish = new Fish(line[2], line[1],
						Double.parseDouble(line[3]), line[0],
						Long.parseLong(line[4]), Bait.parseBait(line[5]));
				fishList.add(newFish);
			}
		}

		this.coolDown = 0;
		this.fm = new FishManager(fishList);
	}

	public class FisherThread implements Runnable {
		String channel;
		String sender;
		long fishTime; // timestamp in ms
		long waitingDuration; // in ms
		long biteLifetime; // in ms
		Fish myFish;

		public FisherThread(String sender, String channel, Fish myFish) {
			this.channel = channel;
			this.sender = sender;

			if (maxReelWait > 0) {
				this.waitingDuration = (1 + rand.nextInt(maxReelWait - 1)) * 1000;
			} else {
				this.waitingDuration = 0;
			}
			// the time the fish stays on the line
			// the bigger the fish, the faster one must react
			this.biteLifetime = (long) (1000 * 100 / myFish.weight());
			this.myFish = myFish;
		}

		@Override
		public void run() {
			bot.sendNotice(sender, "You cast out your line...");

			// wait for fish...
			try {
				Thread.sleep(this.waitingDuration);
			} catch (InterruptedException e) {
				// reeled in too quickly
				tooEarly();
				return;
			}

			fishTime = System.currentTimeMillis();
			bot.sendMessage(channel, "A fish is on the line, " + sender + ". "
					+ Colors.BOLD + Colors.TEAL + "!reel" + Colors.NORMAL
					+ " it in!");

			try {
				Thread.sleep(this.biteLifetime);
			} catch (InterruptedException e) {
				// reeled it in on time! :D
				onTime();
				return;
			}

			// reeled it in too late :(
			tooLate();
		}

		public void tooEarly() {
			bot.sendNotice(sender, "You pulled out prematurely!");
		}

		public void tooLate() {
			bot.sendMessage(channel, "Oh no, the " + myFish.weight() + "-lb. "
					+ myFish.name() + " got away!");

			fishers.remove(sender);
		}

		public void onTime() {
			if (maxReelWait > 0)
				myFish.setReactionTime(System.currentTimeMillis() - fishTime);
			else
				myFish.setReactionTime(0);
			
			int place = 11; // initialize the variable
			try {
				place = fm.catchFish(myFish) + 1;
				saveFish();
			} catch (IOException e) {
				System.err.println("Couldn't save fish info.");
			}

			String outMessage = sender + " caught a";
			if (myFish.name().matches("[aeiou].*"))
				outMessage += "n";
			outMessage += " " + myFish.name() + " weighing " + myFish.weight()
					+ " lbs.";
			if (!myFish.baitUsed().equals(Bait.DEFAULT_BAIT))
				outMessage += (" using " + myFish.baitUsed().name());
			outMessage += '!';
				
			bot.sendMessage(channel, Colors.BLUE + "F" + Colors.PURPLE + "I"
					+ Colors.RED + "S" + Colors.OLIVE + "H" + Colors.YELLOW
					+ "!!");
			bot.sendMessage(channel, outMessage);
			
			if (fm.lastCaughtIsBiggest()) {
				bot.sendMessage(channel, Colors.BOLD + Colors.RED
						+ "NEW RECORD!");
			}
			if (place <= fm.getFishList().size() * 0.01 || place <= 10) { // top 1% or top 10
				bot.sendMessage(channel,
						myFish.catcher() + "'s " + myFish.name()
						+ " broke the nr " + place + " record weight!");
			}
		}
	}

	public void fish(String channel, String sender) {
		if (fishers.containsKey(sender)) { // you are already fishing
			bot.sendNotice(sender, "You already have a line out.");
		
		// too many fishers
		} else if (maxFishers != 0 && fishers.size() >= maxFishers) { 
			bot.sendNotice(sender,
					"There ain't enough room around here for another fisher.");
			
		// too soon since last fish was caught
		} else if (System.currentTimeMillis() - fm.getLastFishCatchTime() <
					coolDown) {
			if (!impatience.containsKey(sender)) {
				impatience.put(sender, 0);
			}
			int l = impatience.get(sender);
			l++;
			impatience.put(sender, l);
			bot.sendNotice(sender, "The fish aren't biting right now!");
			
			if (l >= impatienceLimit) {
				bot.kick(channel, sender, "They're still not biting!");
			}
			
		} else if (!baitsMap.containsKey(sender) && rand.nextDouble() < 0.04) {	// 4% chance of bait
			// you caught a bait
			Bait b = BaitStrengths.BAITS[rand.nextInt(BaitStrengths.BAITS.length)];
			baitsMap.put(sender, b);
			
			bot.sendMessage(channel, "What's this? " + sender + " found " + b.name() + "!");

		// prepare a fish
		} else {
			
			// clear the kick list
			impatience.clear();
			
			coolDown = (baseCoolDown + rand.nextInt(baseCoolDown)) * 1000;
			
			Fish newFish;
			if (baitsMap.containsKey(sender)) {
				newFish = new Fish("fish", sender, baitsMap.get(sender));
				baitsMap.remove(sender);
			} else {
				newFish = new Fish("fish", sender);
				// newFish.setWeight(65.3482); // testing purposes
			}
			String newName;

			// decide what adjective to attach to the fish
			c = Calendar.getInstance();
			if (newFish.weight() > Fish.MEAN_WEIGHT + 10 * Fish.STD_DEV_WEIGHT) {
				newName = fishGrammar.generate("<fish_huge>", 1)[0];
				
			} else if (c.get(Calendar.HOUR) == 4 && c.get(Calendar.MINUTE) == 20) {
				if (newFish.weight() > Fish.MEAN_WEIGHT + 2 * Fish.STD_DEV_WEIGHT) {
					newName = fishGrammar.generate("<fish_big_420>", 1)[0];
					
				} else if (newFish.weight() < Fish.MEAN_WEIGHT - 1.25 * Fish.STD_DEV_WEIGHT) {
					newName = fishGrammar.generate("<fish_sml_420>", 1)[0];
					
				} else {
					newName = fishGrammar.generate("<fish_avg_420>", 1)[0];
				}
			} else {
				if (newFish.weight() > Fish.MEAN_WEIGHT + 2 * Fish.STD_DEV_WEIGHT) {
					newName = fishGrammar.generate("<fish_big>", 1)[0];
					
				} else if (newFish.weight() < Fish.MEAN_WEIGHT - 1.25 * Fish.STD_DEV_WEIGHT) {
					newName = fishGrammar.generate("<fish_sml>", 1)[0];
					
				} else {
					newName = fishGrammar.generate("<fish_avg>", 1)[0];
				}
			}

			if (!newFish.baitUsed().equals(Bait.DEFAULT_BAIT))
				newName = "baited " + newName;
			
			newFish.setName(newName);

			FisherThread ft = new FisherThread(sender, channel, newFish);

			if (maxReelWait <= 0) {
				ft.onTime();
			} else {
				Thread ftt = new Thread(ft);
				fishers.put(sender, ftt);
				ftt.start();
			}
		}
	}

	public void reel(String channel, String sender) {
		if (fishers.containsKey(sender)) {
			Thread ftt = fishers.remove(sender);
			ftt.interrupt();
		} else {
			bot.sendNotice(sender, "You have not cast a line!");
		}
	}

	public void fishStats(String sender, String target) {
		int tot;
		double avg;
		Fish big;
		Fish sml;

		if (!target.isEmpty()) {
			try {
				tot = fm.getFishList(target).size();
				avg = fm.getAverageWeight(target);
				big = fm.getBiggest(target);
				sml = fm.getSmallest(target);

				if (tot == 1) {
					bot.sendNotice(sender, target
							+ " has caught only one fish: a " + big.name()
							+ " weighing " + big.weight());
				} else {
					bot.sendNotice(sender, target + " has caught " + tot
							+ " fish averaging " + avg + " lbs.");
					bot.sendNotice(sender, target + "'s biggest fish was a "
							+ big.name() + " weighing " + big.weight()
							+ " lbs.");
					bot.sendNotice(sender, target + "'s smallest fish was a "
							+ sml.name() + " weighing " + sml.weight()
							+ " lbs.");
				}
			} catch (Exception e) {
				if (target.equals(sender))
					bot.sendNotice(sender, "You have not caught any fish!");
				else
					bot.sendNotice(sender, target + " has not caught any fish!");
			}
		} else {
			tot = fm.getFishList().size();
			avg = fm.getAverageWeight();
			big = fm.getBiggest();
			sml = fm.getSmallest();

			bot.sendNotice(sender, "There have been a total of " + tot
					+ " fish caught averaging " + avg + " lbs.");
			bot.sendNotice(sender, big.catcher()
					+ " holds the record for biggest fish with a " + big.name()
					+ " weighing " + big.weight() + " lbs.");
			if (big.catcher().equals(sml.catcher())) {
				bot.sendNotice(sender, sml.catcher()
						+ " also holds the record for smallest fish "
						+ "with a " + sml.name() + " weighing " + sml.weight()
						+ " lbs.");
			} else {
				bot.sendNotice(sender, "On the other end, " + sml.catcher()
						+ " holds the record " + "for smallest fish with a "
						+ sml.name() + " weighing " + sml.weight() + " lbs.");
			}
		}
	}

	public void fishExport(String sender) {
		bot.dccSendFile(fishData, sender, 120000);
		bot.sendNotice(sender, "The DCC file transfer probably won't work. " +
				"In the meantime, use https://www.dropbox.com/s/7xwpovmqycc4d37/fishdata.csv");
	}

	public void fishHelp(String sender) {
		String fishMessage = "!fish - Try your hand at fishing! If the fish are not biting, "
				+ "wait a bit and try again.";
		if (maxFishers > 1) {
			fishMessage += " Only " + maxFishers
					+ " people can fish at a time.";
		} else if (maxFishers == 1) {
			fishMessage += " Only one person can fish at a time.";
		}

		bot.sendNotice(sender, fishMessage);
		if (maxReelWait > 0)
			bot.sendNotice(sender, "!reel - When a fish is on your line, "
				+ "!reel it in before it gets away!");
		bot.sendNotice(sender,
				"!fishstats - See some statistics of the total fish caught.");
		bot.sendNotice(sender,
				"!fishstats <nick> - See some statistics of the fish caught "
						+ "by a specified nick.");
		bot.sendNotice(sender,
				"!fishexport - Save the fish database as a CSV file "
						+ "for viewing in programs such as Microsoft Excel.");
	}

	public synchronized void saveFish() throws FileNotFoundException {
		Date lastUpdate = new Date(System.currentTimeMillis());

		// csv writers are for noobs
		output = new PrintStream(fishData);
		output.println("\"Date caught\",\"Catcher\",\"Type\",\"Weight\"," + 
				"\"Reaction time (ms)\",\"Bait used\",\"Unmodified weight\"," +
				"\"Last updated: " + lastUpdate.toString() + "\"");
		for (Fish f : fm.getFishList()) {
			output.println("\"" + f.dateCaught() + "\",\"" + f.catcher() + "\",\"" + f.name()
					+ "\",\"" + f.weight() + "\",\"" + f.reactionTime() + "\",\"" + 
					f.baitUsed() + "\",\"" + 
					Fish.truncate(f.weight() / f.baitUsed().strength()) + "\"");
		}
	}
	
	
	public void giveBaitTo(String target, String channel, String sender, Bait bait) {
		if (Arrays.asList(bot.getUsers(channel)).contains(target)) {
			baitsMap.put(target, bait);
			
			bot.sendNotice(sender, bait.name() + " given to " + target);
			bot.sendNotice(target, sender + " gave you a " + bait.name() + "!");
		}
	}
	

	public int getMaxFishers() {
		return maxFishers;
	}

	public void setMaxFishers(int maxFishers) {
		this.maxFishers = maxFishers;
	}

	public int getMaxReelWait() {
		return maxReelWait;
	}

	public void setMaxReelWait(int maxReelWait) {
		this.maxReelWait = maxReelWait;
	}

	public int getBaseCoolDown() {
		return baseCoolDown;
	}

	public void setBaseCoolDown(int baseCoolDown) {
		this.baseCoolDown = baseCoolDown;
	}

	public int getImpatienceLimit() {
		return impatienceLimit;
	}

	public void setImpatienceLimit(int impatienceLimit) {
		this.impatienceLimit = impatienceLimit;
	}
}