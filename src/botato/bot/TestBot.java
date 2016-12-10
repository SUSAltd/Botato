package botato.bot;
import org.jibble.pircbot.*;
import org.json.JSONException;

import java.util.*;
import java.io.*;

public class TestBot extends PircBot {
	private static InputHandler ih;
	
	private static boolean isConnected;

	public TestBot() throws IOException, JSONException {
		
		ih = new InputHandler(this);
	}

	public void onKick(String channel, String kickerNick, String kickerLogin,
			String kickerHostname, String recipientNick, String reason) {
		sendMessage(channel, "AND STAY OUT");
	}
	
	public void onDisconnect() {
		isConnected = false;
		int count = 0;
		try {
			// wait two seconds, prevents "reconnect too fast" error
			Thread.sleep(2000); 
		} catch (InterruptedException e1) {
			// nothing
		}
		while (!isConnected && count < 60) { // makes 60 attempts to reconnect
			count++;
			System.out.println("Attempting to reconnect: attempt #" + count);
			// try to reconnect every 30 seconds
			try {
				main(null);
			} catch (IOException | JSONException e) {
				//nothing
			}
			try {
				Thread.sleep(1000 * 30);
			} catch (InterruptedException e) {
				//nothing
			}
		}
	}

	public void onMessage(String channel, String sender, String login,
			String hostname, String message) {
		ih.onMessage(channel, sender, message);
	}
	
	public void onNickChange(String oldNick, String login, String hostname, String newNick) {
		ih.onNickChange(oldNick, newNick);
	}
	
	public void onPrivateMessage(String sender, String login, String hostname, String message) {
		ih.onPrivateMessage(sender, message);
	}
	
	public static void main(String[] args) throws IOException, JSONException {
		
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
//		String fdFile = "data/fish-" + server + "-" + channel + ".csv";
		String fdFile = "WebContent/data/fish-" + server + "-" + channel + "-";
		
		TestBot bot = new TestBot();
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
				break;
			}
		}
		
		isConnected = true;
		ih.refreshCommands();

		bot.identify("piiscool");
		bot.joinChannel(channel);
	}
}
