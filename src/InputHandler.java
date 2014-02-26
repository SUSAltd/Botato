import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class InputHandler {
	TestBot bot;
	Random rand;
	HashSet<String> ops;
	List<String> hello;
	HashMap<String, CommandThread> commands;
	
	FishHandler fh;
	
	public InputHandler(TestBot bot, HashSet<String> ops, 
			List<String> hello, File fishData) throws IOException {
		this.bot = bot;
		this.ops = ops;
		this.hello = hello;
		this.commands = new HashMap<String, CommandThread>();

		this.rand = new Random();
		this.fh = new FishHandler(bot, fishData);
		
		refreshCommands();
	}
	
	public void onMessage(String channel, String sender, String message) {
		
		String[] mSplit = message.trim().split(" ", 2);

		if (commands.containsKey(mSplit[0])) {
			int maxArgs = commands.get(mSplit[0]).argsNum;
			CommandThread c = commands.get(mSplit[0]);
			if (mSplit.length == 1) {
				c.set(channel, sender, mSplit[0]);
				new Thread(c).start();
			} else if (mSplit.length > 1 && maxArgs > 0) { 
				// split second index into more keywords
				c.set(channel, sender, mSplit[0], mSplit[1].split(" ", maxArgs));
				new Thread(c).start();
			}
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
				if (command.matches("maxfishers \\d+")) {
					String n = command.substring(command.indexOf(' ') + 1);
					int num = Integer.parseInt(n);
					
					fh.setMaxFishers(num);
					bot.sendMessage(sender, 
							"Maximum number of fishers changed to " + num);
				
				// maxReelWait
				} else if (command.matches("maxreelwait \\d+")) {
					String n = command.substring(command.indexOf(' ') + 1);
					int num = Integer.parseInt(n);
					
					fh.setMaxReelWait(num);
					bot.sendMessage(sender, 
							"Maximum reel wait time changed to " + num + 
							" sec");
				
				// impatience
				} else if (command.matches("impatience \\d+")) {
					String n = command.substring(command.indexOf(' ') + 1);
					int num = Integer.parseInt(n);
					
					fh.setImpatienceLimit(num);
					bot.sendMessage(sender, 
							"Impatience limit changed to " + num + " tries");
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
				fh.fish(channel, sender);
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
				if (args.length > 0) {
					fh.fishStats(sender, args[0].trim());
				} else {
					fh.fishStats(sender, "");
				}
			}
		});
		
		commands.put("!fishexport", new CommandThread(0) {
			public void run() {
				fh.fishExport(sender);
			}
		});
		
		commands.put("!fishhelp", new CommandThread(0) {
			public void run() {
				fh.fishHelp(sender);
			}
		});
		
		commands.put(bot.getNick(), new CommandThread(1) {
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
}
