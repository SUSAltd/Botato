package botato.bot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONException;

import botato.eightball.EightBallHandler;
import botato.fish.FishHandler;
import botato.giphysearch.GiphySearchHandler;


public class InputHandler {
	TestBot bot;
	Random rand;
	HashSet<String> blacklist;
	HashSet<String> ops;
	List<String> hello;
	HashMap<String, CommandThread> commands;
	
	FishHandler fh;
	EightBallHandler ebh;
	GiphySearchHandler gsh;
	
	Scanner input;
	
	public InputHandler(TestBot bot) throws IOException, JSONException {
		this.bot = bot;
		this.ops = fileToSet(new File("ops.txt"));
		this.hello = fileToList(new File("data/hello.susa"));
		this.blacklist = fileToSet(new File("blacklist.txt"));
		this.commands = new HashMap<String, CommandThread>();

		this.rand = new Random();
		this.fh = new FishHandler(bot);
		this.ebh = new EightBallHandler();
		this.gsh = new GiphySearchHandler();
		
		refreshCommands();
	}
	
	public void onMessage(String channel, String sender, String message) {
		if (blacklist.contains(sender))
			return;
		
		String[] mSplit = message.trim().split(" ", 2);
		String commandName = mSplit[0].toLowerCase();

		if (commands.containsKey(commandName)) {
			int maxArgs = commands.get(commandName).argsNum;
			CommandThread c = commands.get(commandName);
			if (mSplit.length == 1 || maxArgs == 0) {
				c.set(channel, sender, mSplit[0]);
			} else /* if (mSplit.length > 1 && maxArgs > 0) */ { 
				// split second index into more keywords
				c.set(channel, sender, mSplit[0], mSplit[1].split(" ", maxArgs));
			}
			new Thread(c).start();
		}
	}
	
	public void onNickChange(String oldNick, String newNick) {
		if (newNick.equals(bot.getNick())) {
			refreshCommands();
		}
	}
	
	public void onPrivateMessage(String sender, String message) {
		if (ops.contains(sender)) {
			
			// set commands
			if (message.startsWith("set ")) {
				
				String prefix = "set ";
				String command = message.substring(prefix.length());
				
				// maxFishers
				if (command.matches("max_fishers \\d+")) {
					String n = command.substring(command.indexOf(' ') + 1);
					int num = Integer.parseInt(n);
					
					fh.setMaxFishers(num);
					bot.sendMessage(sender, 
							"Maximum number of fishers changed to " + num);
				
				// maxReelWait
				} else if (command.matches("reel_wait_time \\d+")) {
					String n = command.substring(command.indexOf(' ') + 1);
					int num = Integer.parseInt(n);
					
					fh.setMaxReelWait(num);
					bot.sendMessage(sender, 
							"Maximum reel wait time changed to " + num + 
							" sec");
				
				// impatience
				} else if (command.matches("failed_tries_limit \\d+")) {
					String n = command.substring(command.indexOf(' ') + 1);
					int num = Integer.parseInt(n);
					
					fh.setImpatienceLimit(num);
					bot.sendMessage(sender, 
							"Failed tries limit changed to " + num + " tries");
				}
				
			// save stuff
			} else if (message.equals("save")) {
				try {
					fh.saveFish();
					bot.sendMessage(sender, "Fish data saved");
				} catch (FileNotFoundException | JSONException e) {
					bot.sendMessage(sender, "Could not save");
				}
				
			// say stuff
			} else if (message.startsWith("say ")) {
				for (String s : bot.getChannels()) {
					bot.sendMessage(s, message.substring(4));
				}
				
			// do action stuff
			} else if (message.startsWith("me ")) {
				for (String s : bot.getChannels()) {
					bot.sendAction(s, message.substring(3));
				}
				
			// join channel
			} else if (message.startsWith("join ")) {
				String channel = message.substring(message.indexOf(' ') + 1);
				bot.joinChannel(channel);
				
			// part channel
			} else if (message.startsWith("part ")) {
				String channel = message.substring(message.indexOf(' ') + 1);
				bot.partChannel(channel);
				
			// change nick
			} else if (message.startsWith("nick ")) {
				String newNick = message.substring(message.indexOf(' ') + 1);
				bot.changeNick(newNick);
				
			// refresh
			} else if (message.equals("refresh")) {
				// disconnects, triggers onDisconnect(), which reconnects
				bot.disconnect();
			}	
		}
	}
	
	public void refreshCommands() {
		commands.clear();
		
		commands.put("!fish", new CommandThread(0) {
			public void run() {
				try {
					fh.fish(channel, sender);
				} catch (IOException | JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		commands.put("!fosh", commands.get("!fish"));
		commands.put("!fush", commands.get("!fish"));
		
		commands.put("!reel", new CommandThread(0) {
			public void run() {
				fh.reel(channel, sender);
			}
		});
		
		commands.put("!fishstats", new CommandThread(1) {
			public void run() {
				if (args.length == 1) {
					try {
						fh.fishStats(sender, args[0].trim());
					} catch (IOException | JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						fh.fishStats(sender, "");
					} catch (IOException | JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		commands.put("!fishtop", new CommandThread(1) {
			public void run() {
				if (args.length == 1) {
					try {
						int n = Integer.parseInt(args[0]);
						fh.fishTop(sender, n);
					} catch (NumberFormatException e) {
						// nothing
					}
				}
			}
		});
		
		commands.put("!fishexport", new CommandThread(0) {
			public void run() {
				fh.fishExport(sender);
			}
		});
		
		commands.put("!8ball", new CommandThread(1) {
			public void run() {
				bot.sendMessage(channel, ebh.getRandomMessage());
			}
		});
		
		commands.put("!gif", new CommandThread(1) {
			public void run() {
				try {
					bot.sendMessage(channel, gsh.getRandomGifUrl(args[0]));
				} catch (IOException | JSONException e) {
					bot.sendMessage(channel, "Something wrong happened SUSAltd fix it");
					e.printStackTrace();
				} catch (IndexOutOfBoundsException e) {
					bot.sendNotice(sender, "No GIFs found!");
				}
			}
		});
		
		commands.put("!help", new CommandThread(0) {
			public void run() {
				fh.fishHelp(sender);
			}
		});
		
		commands.put(bot.getNick().toLowerCase(), new CommandThread(1) {
			public void run() {
				
				// botate
				if (args[0].equals("botate")) {
					bot.sendAction(channel, "botate botate botate");
					
				// hello
				} else if (hello.contains(args[0])) {
					int r = rand.nextInt(hello.size() - 1);
					bot.sendMessage(channel, sender + " " + hello.get(r));
				}
			}
		});
		
		// the first hello
		String firstHelloKey = hello.get(0);
		commands.put(firstHelloKey, new CommandThread(1) {
			public void run() {
				if (args.length > 0 && args[0].equals(bot.getNick())) {
					int r = rand.nextInt(hello.size() - 1);
					bot.sendMessage(channel, sender + ' ' + hello.get(r));
				}
			}
		});
		// the other hellos
		for (int i = 1; i < hello.size(); i++) {
			String s = hello.get(i);
			commands.put(s, commands.get(firstHelloKey));
		}
	}
	
	private class CommandThread implements Runnable {
		protected String channel;
		protected String sender;
		protected String message;
		protected String[] args;
		public int argsNum;
		
		public CommandThread(int argsNum) {
			this.argsNum = argsNum;
		}
		
		public void run() {
			// should be overwritten
		}
		
		public void set(String channel, String sender, String message,
				String[] args) {
			this.channel = channel;
			this.sender = sender;
			this.message = message;
			this.args = args;
		}
		
		public void set(String channel, String sender, String message) {
			this.set(channel, sender, message, new String[0]);
		}
	}
	
	private HashSet<String> fileToSet(File fileIn) throws IOException {
		HashSet<String> set = new HashSet<String>();
		fileIn.createNewFile();
		input = new Scanner(fileIn);
		while (input.hasNextLine()) {
			set.add(input.nextLine());
		}
		return set;
	}
	
	private List<String> fileToList(File fileIn) throws IOException {
		List<String> list = new ArrayList<String>();
		fileIn.createNewFile();
		input = new Scanner(fileIn);
		while (input.hasNextLine()) {
			list.add(input.nextLine());
		}
		return list;
	}
}
