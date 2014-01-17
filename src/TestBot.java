import org.jibble.pircbot.*;

import java.util.*;
import java.io.*;

public class TestBot extends PircBot {
	private Scanner input;
	private Random rand;
	
	private InputHandler ih;
	
	private List<String> ops;
	private List<String> hello;

	public TestBot(File fishData) throws IOException {
		ops = fileToList(new File("ops.txt"));
		hello = fileToList(new File("data/hello.susa"));
		
		rand = new Random();
		ih = new InputHandler(this, rand, ops, hello, fishData);
	}

	public void onKick(String channel, String kickerNick, String kickerLogin,
			String kickerHostname, String recipientNick, String reason) {
		sendMessage(channel, "AND STAY OUT");
	}
	
	public void onDisconnect() {
		int count = 0;
		while(!this.isConnected() || count < 60) { // makes 60 attempts to reconnect
			count++;
			System.out.println("Attempting to reconnect: attempt #" + count);
			try {
				// try to reconnect every thirty seconds
				main(null);
				Thread.sleep(1000 * 30);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		
		ih.onMessage(channel, sender, message);
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
		String fdFile = p.getProperty("fish_data", "data/fishdata.csv");
		
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
			} catch (IOException | IrcException e) {
				System.err.println("Unable to connect to server");
			}
		}
		
		bot.joinChannel(channel);
	}
}
