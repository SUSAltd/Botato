package botato.eightball;

public class EightBallMessage {
	
	String message;
	int positivity; // -1, 0, or 1
	
	EightBallMessage(String message, int positivity) {
		this.message = message;
		this.positivity = positivity;
	}

}
