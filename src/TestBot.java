import org.jibble.pircbot.*;
import java.util.*;
import java.io.*;

public class TestBot extends PircBot {
	private Scanner input;
	private PrintStream output;
	private Random rand;
	
	private List<String> ops;
	
	private File fishData;
	private GrammarSolver fishSolver;
	private FishManager fishManager;
    private HashMap<String, Thread> fishers;
    
    //some "constant" stuff
    private int maxFishers = 1; // 0 or less for no maximum
    private int maxReelWait = 20; // in seconds; 0 or less for no !reel

	public TestBot(File fishListFile) throws IOException {
		rand = new Random();
		
		ops = new ArrayList<String>();
		File opsFile = new File("ops.txt");
		opsFile.createNewFile();
		input = new Scanner(opsFile);
		while (input.hasNextLine()) {
			ops.add(input.nextLine());
		}

		// initializes fish
		List<Fish> fishList = new ArrayList<Fish>();
		fishData = new File("data/fishdata.csv");
		if (!fishData.createNewFile()) {
			// get the fish from the file (skip first line for headers)
			input = new Scanner(fishData);
			String[] line;
			input.nextLine();
			while (input.hasNextLine()) {
				// line[] format = [dateCaught, catcher, name, weight]
				line = input.nextLine().split(",");
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

		// create the fish grammar
		input = new Scanner(fishListFile);

		List<String> fishGrammar = new ArrayList<String>();
		while (input.hasNextLine()) {
			String next = input.nextLine().trim();
			if (next.length() > 0 && !next.startsWith("#")) {
				fishGrammar.add(next);
			}
		}
		fishSolver = new GrammarSolver(
				Collections.unmodifiableList(fishGrammar));

		fishers = new HashMap<String, Thread>();
	}

	public void onKick(String channel, String kickerNick, String kickerLogin,
			String kickerHostname, String recipientNick, String reason) {
		sendMessage(channel, "AND STAY OUT");
	}
	
	public void onDisconnect() {
		try {
			Thread.sleep(1000 * 60);
			main(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		
		// !fish
		if (message.equals("!fish")) {
			if (System.currentTimeMillis() - fishManager.getLastFishCatchTime() >
					(30 + rand.nextInt(30)) * 1000) {
				fish(channel, sender, message);
			} else {
				sendNotice(sender, "The fish aren't biting right now!");
			}
		
		// !reel
		} else if (message.equals("!reel")) {
			reel(channel, sender, message);
			
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
		} else if (message.equalsIgnoreCase(this.getNick() + " botate")) {
			sendAction(channel, "botate botate botate");
		
		// op commands
		} else if (ops.contains(sender)) {
			// set commands
			if (message.startsWith(this.getNick() + " set ")) {
				String prefix = this.getNick() + " set ";
				String command = message.substring(prefix.length());
				
				// maxFishers
				if (command.matches("maxfishers \\d+")) {
					String n = command.substring(command.indexOf(' ') + 1);
					int num = Integer.parseInt(n);
					
					maxFishers = num;
					sendNotice(sender, "Maximum number of fishers changed to " + num);
				
				// maxReelWait
				} else if (command.matches("maxreelwait \\d+")) {
					String n = command.substring(command.indexOf(' ') + 1);
					int num = Integer.parseInt(n);
					
					maxReelWait = num;
					sendNotice(sender, "Maximum reel wait time changed to " + num + " sec");
				}
				
			// nick
			} else if (message.startsWith(this.getNick() + " nick ")) {
				String newNick = message.substring(message.lastIndexOf(' ') + 1);
				this.changeNick(newNick);
			}
		}
	}

    private class FisherHandler implements Runnable {
        TestBot bot;
        String channel;
        String sender;
        long fishTime; // timestamp in ms
        long waitingDuration; // in ms
        long biteLifetime; // in ms
        Fish myFish;

        public FisherHandler(TestBot bot, String sender, String channel, Fish myFish) {
            this.bot = bot;
            this.channel = channel;
            this.sender = sender;
            // a time between 1 and 60 seconds
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
            sendNotice(sender, "You cast out your line...");

            // wait for fish...
            try {
                Thread.sleep(this.waitingDuration);
            } catch (InterruptedException e) {
                // reeled in too quickly
                tooEarly();
                return;
            }

            fishTime = System.currentTimeMillis();
            sendMessage(channel, "A fish is on the line, " + sender + ". " +
                    Colors.BOLD + Colors.TEAL + "!reel" + Colors.NORMAL + " it in!");

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

        private void tooEarly() {
            sendNotice(sender, "You pulled out prematurely!");
        }

        private void tooLate() {
            sendMessage(channel, "Oh no, the " + myFish.weight() + "-lb. " +
                    myFish.name() + " got away!");
        }

        private void onTime() {
        	myFish.setReactionTime(System.currentTimeMillis() - fishTime);
            int place = 11;
            try {
                synchronized(bot) { // prevents weird stuff in case multiple saves
                    place = fishManager.catchFish(myFish) + 1;
                    bot.saveFish();
                }
            } catch (IOException e) {
                System.err.println("[ERROR] Couldn't save fish info.");
            }

            String outMessage = sender + " caught a";
            if (myFish.name().matches("[aeiouAEIOU].*")) {
                outMessage += "n";
            }
            outMessage += " " + myFish.name() + " weighing " + myFish.weight() + " lbs.!";
            sendMessage(channel, Colors.BLUE + "F" + Colors.PURPLE + "I"
                    + Colors.RED + "S" + Colors.OLIVE + "H" + Colors.YELLOW + "!!");
            sendMessage(channel, outMessage);
            if (fishManager.lastCaughtIsBiggest()) {
                sendMessage(channel, Colors.BOLD + Colors.RED + "NEW RECORD! ");
            }
            if (place <= 10) {
                sendMessage(channel, myFish.catcher() + "'s " + myFish.name() +
                        " broke the nr " + place + " record weight!");
            }

        }

    }
	
	private void fish(String channel, String sender, String message) {
        if (fishers.containsKey(sender)) {
            sendNotice(sender, "You already have a line out.");
        } else if (maxFishers != 0 && fishers.size() >= maxFishers) {
        	sendNotice(sender, "There ain't enough room around here for another fisher.");
		} else {
            Fish newFish = new Fish("temp", sender);
            String newName;

            // decide what adjective to attach to the fish
            if (newFish.weight() > Fish.MEAN_WEIGHT + 2 * Fish.STD_DEV_WEIGHT) {
                newName = fishSolver.generate("<fish_big>", 1)[0];
            } else if (newFish.weight() < Fish.MEAN_WEIGHT - 1.5
                    * Fish.STD_DEV_WEIGHT) {
                newName = fishSolver.generate("<fish_sml>", 1)[0];
            } else {
                newName = fishSolver.generate("<fish_avg>", 1)[0];
            }

            newFish.setName(newName);

            FisherHandler fh = new FisherHandler(this, sender, channel, newFish);
            
            if (maxReelWait <= 0) {
            	fh.onTime();
            } else {
	            Thread fht = new Thread(fh);
	            fishers.put(sender, fht);
	            fht.start();
            }
        }
	}
	
	
	
	private void reel(String channel, String sender, String message) {
		if (fishers.containsKey(sender)) {
            Thread fht = fishers.remove(sender);
            fht.interrupt();
        } else {
            sendNotice(sender, "You have not cast a line!");
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
		String fishMessage = "!fish - Try your hand at fishing! If the fish are not biting, " +
				"wait a bit and try again.";
		if (maxFishers > 1) {
			fishMessage += " Only " + maxFishers + " people can fish at a time.";
		} else if (maxFishers == 1) {
			fishMessage += " Only one person can fish at a time.";
		}
		
		sendNotice(sender, fishMessage);
		sendNotice(sender, "!reel - When a fish is on your line, " +
				"!reel it in before it gets away!");
		sendNotice(sender, "!fishstats - See some statistics of the total fish caught.");
		sendNotice(sender, "!fishstats <nick> - See some statistics of the fish caught " +
				"by a specified nick.");
		sendNotice(sender, "!fishexport - Save the fish database as a CSV file " +
				"for viewing in programs such as Microsoft Excel.");
	}

	public void saveFish() throws FileNotFoundException {
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
			System.err.println("The property fish_list_file"
					+ "was not found in bot.ini.");
		}

		TestBot bot = new TestBot(new File(fishList));
		bot.setVerbose(true);
		bot.setName(p.getProperty("nick"));
		bot.connect(p.getProperty("server"));
		bot.joinChannel(p.getProperty("channel"));
	}
}
