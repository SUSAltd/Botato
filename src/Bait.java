public class Bait {
	private String name;
	private double strength;
	
	public static final Bait DEFAULT_BAIT = new Bait("no bait", 1.0);

	public Bait(String name, double strength) {
		this.name = name;
		this.strength = strength;
	}
	
	
	public String name() {
		return name;
	}


	public double strength() {
		return strength;
	}
	
	public String toString() {
		return name + " (" + strength + ")";
	}
	
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (o instanceof Bait) {
			Bait anotherBait = (Bait) o;
			if (this.name().equals(anotherBait.name()) && this.strength() == anotherBait.strength()) {
				return true;
			}
		}
		return false;
	}
	
	public static Bait parseBait(String s) {
		try {
			String str = s.substring(s.lastIndexOf('(') + 1, s.lastIndexOf(')'));
			double strength1 = Double.parseDouble(str);
			
			String name1 = s.substring(0, s.lastIndexOf('(') - 1);
			
			return new Bait(name1, strength1);
			
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("The given String \"" + s + "\" could not be parsed to a Bait.");
		}
	}
}
