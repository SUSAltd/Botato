import java.util.Calendar;
import java.util.Comparator;
import java.util.Random;
import java.util.Date;

/**
 * This class represents a fish with a specified name, weight, along with who
 * caught it, and when.
 * 
 * @author Ray
 * 
 */
public class Fish implements Comparable<Fish> {
	private String name;
	private String catcher;
	private double weight;
	private String dateCaught;
	private long reactionTime;

	public final static double MEAN_WEIGHT = 10.0;
	public final static double STD_DEV_WEIGHT = 4.0;
	public final static int ROUND_TO_PLACES = 4;
	private final static double VARIANCE_WEIGHT = Math.pow(STD_DEV_WEIGHT, 2);

	public Fish() {
		name = "no fish";
		catcher = "nobody";
		weight = 0;
		dateCaught = "00/00/0000 0:00:00";
	}

	/**
	 * Constructs a Fish object with a given name, caught by a specified
	 * catcher.
	 * 
	 * @param name
	 *            - name of the fish
	 * @param catcher
	 *            - name of the catcher
	 */
	public Fish(String name, String catcher) {
		this.name = name;
		this.catcher = catcher;

		// random weight based on log-normal distribution
		Random rand = new Random();
		
		double mu = Math.log(Math.pow(MEAN_WEIGHT, 2) /
				Math.sqrt(VARIANCE_WEIGHT + Math.pow(MEAN_WEIGHT, 2)));
		double sigma = Math.sqrt(Math.log(
				1 + VARIANCE_WEIGHT / Math.pow(MEAN_WEIGHT, 2)));
		weight = truncate(Math.pow(Math.E,
				mu + sigma * rand.nextGaussian()));

		Date date = new Date();
		dateCaught = String.format("%tF %<tT", date);
	}

	/**
	 * Constructs a Fish object with a given name with a given weight, caught by
	 * a specified catcher on a specified date.
	 * 
	 * @param name
	 *            - name of the fish
	 * @param catcher
	 *            - name of the catcher
	 * @param weight
	 *            - weight of the fish
	 * @param date
	 *            - date on which the fish was caught
	 * 
	 * @throws IllegalArgumentException
	 *             if weight is negative, which leads to the untimely demise of
	 *             the universe
	 */
	public Fish(String name, String catcher, double weight, Date date, long reactionTime) {
		if (weight < 0) {
			// Universe.selfDestruct();
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.catcher = catcher;
		this.weight = truncate(weight);
		this.dateCaught = String.format("%tF %<tT", date);
		this.reactionTime = reactionTime;
	}
	
	/**
	 * Constructs a Fish object with a given name with a given weight, caught by
	 * a specified catcher on a specified date.
	 * 
	 * @param name
	 *            - name of the fish
	 * @param catcher
	 *            - name of the catcher
	 * @param weight
	 *            - weight of the fish
	 * @param date
	 *            - date on which the fish was caught
	 * 
	 * @throws IllegalArgumentException
	 *             if weight is negative, which leads to the untimely demise of
	 *             the universe
	 */
	public Fish(String name, String catcher, double weight, String date, long reactionTime) {
		if (weight < 0) {
			// Universe.selfDestruct();
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.catcher = catcher;
		this.weight = truncate(weight);
		this.dateCaught = date;
		this.reactionTime = reactionTime;
	}

	/**
	 * Returns the name of the fish.
	 * 
	 * @return the name of the fish
	 */
	public String name() {
		return name;
	}
	
	/**
	 * Sets the name of the fish to the given String.
	 * 
	 * @param name - the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the catcher.
	 * 
	 * @return the name of the catcher
	 */
	public String catcher() {
		return catcher;
	}

	/**
	 * Returns the weight of the fish.
	 * 
	 * @return the weight of the fish
	 */
	public double weight() {
		return weight;
	}

	/**
	 * Returns the date on which the fish was caught in the format
	 * <code>yyyy-MM-dd hh:mm:ss</code>.
	 * 
	 * @return the date on which the fish was caught in the format
	 * <code>yyyy-MM-dd hh:mm:ss</code>
	 */
	public String dateCaught() {
		return dateCaught;
	}
	
	public long reactionTime() {
		return reactionTime;
	}

	public void setReactionTime(long millis) {
		reactionTime = millis;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Fish other) {
		if (this.weight > other.weight)
			return 1;
		else if (this.weight < other.weight)
			return -1;
		else
			return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name + ", " + weight;
	}

	/**
	 * Truncates a given double to ROUND_TO_PLACES places.
	 * 
	 * @param number
	 *            the number to be truncated
	 * @return the truncated result
	 */
	public static double truncate(double number) {
		String rounded = String.format("%." + ROUND_TO_PLACES + "f", number);
		return Double.parseDouble(rounded);
	}

	/**
	 * A method for testing purposes
	 * 
	 * @param args
	 *            mateys
	 */
	public static void main(String[] args) {
		Fish fish1 = new Fish("My fishy", "Ray");
		System.out.println("Fish 1");
		System.out.println("    Name:        " + fish1.name());
		System.out.println("    Caught by:   " + fish1.catcher());
		System.out.println("    Weight:      " + fish1.weight());
		System.out.println("    Date caught: " + fish1.dateCaught());
		System.out.println();

		Calendar c = Calendar.getInstance();
		c.set(2013, 9 - 1, 7, 15, 23, 35);

		Fish fish2 = new Fish("Magikarp", "SUSAltd", 13.874511, c.getTime(), 0);
		System.out.println("Fish 2");
		System.out.println("    Name:        " + fish2.name());
		System.out.println("    Caught by:   " + fish2.catcher());
		System.out.println("    Weight:      " + fish2.weight());
		System.out.println("    Date caught: " + fish2.dateCaught());
		System.out.println();
		
		double totalWeight = 0;
		int sampleSize = 100;
		double maxWeight = 0.0;
		for (int i = 0; i < sampleSize; i++) {
			Fish f = new Fish("fish", "me");
			totalWeight += f.weight();
			if (f.weight() > maxWeight) {
				maxWeight = f.weight();
			}
		}
		System.out.println("Average weight of " + sampleSize + " fish: "
				+ totalWeight / sampleSize);
		System.out.println("Biggest fish: " + maxWeight);
	}

	/**
	 * A Comparator that alphabetically sorts Fish by their name, ignoring case.
	 */
	public static final Comparator<Fish> NAME_ORDER = new Comparator<Fish>() {
		public int compare(Fish f1, Fish f2) {
			return f1.name.compareToIgnoreCase(f2.name);
		}
	};

	/**
	 * A Comparator that alphabetically sorts Fish by their catcher's name,
	 * ignoring case.
	 */
	public static final Comparator<Fish> CATCHER_ORDER = new Comparator<Fish>() {
		public int compare(Fish f1, Fish f2) {
			return f1.catcher.compareToIgnoreCase(f2.catcher);
		}
	};

	/**
	 * A Comparator that sorts Fish ascending by their weight.
	 */
	public static final Comparator<Fish> WEIGHT_ORDER = new Comparator<Fish>() {
		public int compare(Fish f1, Fish f2) {
			return f1.compareTo(f2);
		}
	};

	/**
	 * A Comparator that sorts Fish ascending by the date on which it was
	 * caught.
	 */
	public static final Comparator<Fish> DATE_ORDER = new Comparator<Fish>() {
		public int compare(Fish f1, Fish f2) {
			return f1.dateCaught.compareToIgnoreCase(f2.dateCaught);
		}
	};
}
