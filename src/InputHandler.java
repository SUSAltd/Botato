import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class InputHandler {
	TestBot bot;
	Random rand;
	List<String> ops;
	List<String> hello;
	
	FishHandler fh;
	
	public InputHandler(TestBot bot, Random rand, List<String> ops, List<String> hello, File fishData) throws IOException {
		this.bot = bot;
		this.rand = rand;
		this.ops = ops;
		this.hello = hello;
		
		this.fh = new FishHandler(bot, fishData);
	}
	
	public void onMessage(String channel, String sender, String message) {
		// !fish
		if (message.equals("!fish")) {
			fh.fish(channel, sender, message);
		
		// !reel
		} else if (message.equals("!reel")) {
			fh.reel(channel, sender, message);
			
		// !fishstats
		} else if (message.startsWith("!fishstats")) {
			fh.fishStats(sender, message);
			
		// !fishexport
		} else if (message.equals("!fishexport")) {
			fh.fishExport(sender);
			
		// !fish help
		} else if (message.equals("!fish help")) {
			fh.fishHelp(sender, ops.contains(sender));

		// directed at bot
		} else if (message.startsWith(bot.getNick() + " ")) {
			String prefix = bot.getNick() + " ";
			String command = message.substring(prefix.length());
			
			// botate
			if (command.equals("botate")) {
				bot.sendAction(channel, "botate botate botate");
				
			// hello
			} else if (hello.contains(command)) {
				int r = rand.nextInt(hello.size() - 1);
				bot.sendMessage(channel, sender + " " + hello.get(r));
			}
		}

		// op commands
		if (ops.contains(sender)) {
			
			// set commands
			if (message.startsWith(bot.getNick() + " set ")) {
				
				String prefix = bot.getNick() + " set ";
				String command = message.substring(prefix.length());
				
				// maxFishers
				if (command.matches("maxfishers \\d+")) {
					String n = command.substring(command.indexOf(' ') + 1);
					int num = Integer.parseInt(n);
					
					fh.setMaxFishers(num);
					bot.sendNotice(sender, "Maximum number of fishers changed to " + num);
				
				// maxReelWait
				} else if (command.matches("maxreelwait \\d+")) {
					String n = command.substring(command.indexOf(' ') + 1);
					int num = Integer.parseInt(n);
					
					fh.setMaxReelWait(num);
					bot.sendNotice(sender, "Maximum reel wait time changed to " + num + " sec");
				}
				
			// directed at bot
			} else if (message.startsWith(bot.getNick() + " ")) {
				String prefix = bot.getNick() + " ";
				String command = message.substring(prefix.length());
				
				// change nick
				if (command.equals("nick")) {
					String newNick = command.substring(command.indexOf(' ') + 1);
					bot.changeNick(newNick);
					
				// refresh
				} else if (command.equals("refresh")) {
					// disconnects, triggers onDisconnect(), which reconnects
					bot.disconnect();
				}
			}
		}
	}
}
