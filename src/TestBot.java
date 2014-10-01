import org.jibble.pircbot.*;

import java.util.*;
import java.io.*;

public class TestBot extends PircBot {
	private Scanner input;
	
	private static InputHandler ih;
	
	private HashSet<String> ops;
	private HashSet<String> blacklist;
	private List<String> hello;
	
	private static boolean isConnected;

	public TestBot(File fishData) throws IOException {
		
		ops = fileToSet(new File("ops.txt"));
		blacklist = fileToSet(new File("blacklist.txt"));
		hello = fileToList(new File("data/hello.susa"));
		
		ih = new InputHandler(this, ops, hello, fishData);
	}

	public void onKick(String channel, String kickerNick, String kickerLogin,
			String kickerHostname, String recipientNick, String reason) {
		sendMessage(channel, "AND STAY OUT");
	}
	
	public void onDisconnect() {
		isConnected = false;
		int count = 0;
		try {
			Thread.sleep(2000); // wait one second, prevents "reconnect too fast" error
		} catch (InterruptedException e1) {
			// nothing
		}
		while (!isConnected && count < 60) { // makes 60 attempts to reconnect
			count++;
			System.out.println("Attempting to reconnect: attempt #" + count);
				// try to reconnect every thirty seconds
				try {
					main(null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(1000 * 30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		if (!blacklist.contains(sender))
			ih.onMessage(channel, sender, message);
	}
	
	public void onNickChange(String oldNick, String login, String hostname, String newNick) {
		ih.onNickChange(oldNick, newNick);
	}
	
	public void onPrivateMessage(String sender, String login, String hostname, String message) {
		ih.onPrivateMessage(sender, message);
	}
	
	public static void main(String[] args) throws IOException {
		
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(new File("bot.ini")));
		} catch (FileNotFoundException e1) {
			System.err.println("File \"bot.ini\" was not found");
		} catch (IOException e1) {
			System.err.println("Error occured while reading the file \"bot.ini\"");
		}
		
		String nick = p.getProperty("nick");
		String n = p.getProperty("alt_nicks", nick + "_");
		String[] altNicks = n.split("\\s?,\\s?");
		String channel = p.getProperty("channel");
		String server = p.getProperty("server");
		// String fdFile = p.getProperty("fish_data", "data/fishdata.csv");
		
		String fdFile = "data/fish-" + server + "-" + channel + ".csv";
		
		TestBot bot = new TestBot(new File(fdFile));
		bot.setVerbose(true);
		bot.setName(nick);

		// if nick is in use, goes through altNicks
		boolean nickInUse = true;
		int count = 0;
		while (nickInUse && count < altNicks.length) {
			try {
				nickInUse = false;
				bot.connect(server);
			} catch (NickAlreadyInUseException e) {
				bot.setName(altNicks[count]);
				count++;
				nickInUse = true;
				if (count > 0) {
					try {
						Thread.sleep(1000 * 10);
					} catch (InterruptedException e1) {
						// do nothing
					}
				}
			} catch (IrcException e) {
				System.out.println("Unable to connect to server ");
			}
		}
		
		isConnected = true;
		ih.refreshCommands();

		bot.identify("piiscool");
		bot.joinChannel(channel);
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
