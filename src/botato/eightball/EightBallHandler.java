package botato.eightball;
import java.util.Random;

import org.jibble.pircbot.Colors;

public class EightBallHandler {
	
	public static final EightBallMessage[] MESSAGES = {
		new EightBallMessage("It is certain", 1),
		new EightBallMessage("It is decidedly so", 1),
		new EightBallMessage("Without a doubt", 1),
		new EightBallMessage("Yes definitely", 1),
		new EightBallMessage("You may rely on it", 1),
		new EightBallMessage("As I see it, yes", 1),
		new EightBallMessage("Most likely", 1),
		new EightBallMessage("Outlook good", 1),
		new EightBallMessage("Yes", 1),
		new EightBallMessage("Signs point to yes", 1),
		new EightBallMessage("Reply hazy try again", 0),
		new EightBallMessage("Ask again later", 0),
		new EightBallMessage("Better not tell you now", 0),
		new EightBallMessage("Cannot predict now", 0),
		new EightBallMessage("Concentrate and ask again", 0),
		new EightBallMessage("Don't count on it", -1),
		new EightBallMessage("My reply is no", -1),
		new EightBallMessage("My sources say no", -1),
		new EightBallMessage("Outlook not so good", -1),
		new EightBallMessage("Very doubtful", -1),
	};
	private Random rand;
	
	public EightBallHandler() {
		this.rand = new Random();
	}

	public String getRandomMessage() {
		EightBallMessage ebm = MESSAGES[rand.nextInt(MESSAGES.length)];
		String message = "> " + Colors.NORMAL + ebm.message; // prefix bullet point
		switch (ebm.positivity) {
		case -1: 
			message = Colors.RED + message;
			break;
		case 0:
			message = Colors.YELLOW + message;
			break;
		case 1:
			message = Colors.DARK_GREEN + message;
			break;
		}
		
		return message;
	}
}
