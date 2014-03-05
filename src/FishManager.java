import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


public class FishManager {
	private List<Fish> fishList;
	private HashMap<String, SortedSet<Fish>> fishMap;
	private Fish biggestFish;
	private Fish smlFish;
	private double avgFishWeight;
	private long lastFishCatchTime;
	private boolean lastCaughtIsBiggest;
	
	public FishManager(List<Fish> fishList) {
		this.fishList = new ArrayList<Fish>(fishList);
		fishMap = new HashMap<String, SortedSet<Fish>>();
		lastFishCatchTime = 0;
		lastCaughtIsBiggest = false;
		
		sortFishBy(SortOrder.WEIGHT_D);
		
		if (fishList.isEmpty()) {
			biggestFish = new Fish();
			smlFish = new Fish();
			
			avgFishWeight = 0.0;
		} else {
			biggestFish = fishList.get(0);
			smlFish = fishList.get(fishList.size() - 1);
			
			// the map
			double cumSum = 0;
			for (Fish f : fishList) {
				if (!fishMap.containsKey(f.catcher().toLowerCase())) {
					fishMap.put(f.catcher().toLowerCase(), new TreeSet<Fish>());
				}
				fishMap.get(f.catcher().toLowerCase()).add(f);
				
				cumSum += f.weight();
			}
			
			avgFishWeight = cumSum / fishList.size();
			avgFishWeight = Fish.truncate(avgFishWeight);
		}
	}
	
	public int catchFish(Fish newFish) {
		fishList.add(newFish);
		if (!fishMap.containsKey(newFish.catcher())) {
			fishMap.put(newFish.catcher(), new TreeSet<Fish>());
		}
		fishMap.get(newFish.catcher()).add(newFish);
		lastCaughtIsBiggest = false;
		
		if (newFish.weight() > biggestFish.weight()) {
			biggestFish = newFish;
			lastCaughtIsBiggest = true;
		}
		if (newFish.weight() < smlFish.weight() || fishList.size() == 1) {
			smlFish = newFish;
		}
		
		avgFishWeight =
				(avgFishWeight * (fishList.size() - 1) + newFish.weight()) /
				fishList.size();
		
		avgFishWeight = Fish.truncate(avgFishWeight);
		
		lastFishCatchTime = System.currentTimeMillis();
		
		sortFishBy(SortOrder.WEIGHT_D);
		
		return fishList.indexOf(newFish);
	}
	
	public void sortFishBy(SortOrder order) {
		switch (order) {
		case NAME_A:
			Collections.sort(fishList, Fish.NAME_ORDER);
			break;
		case NAME_D:
			Collections.sort(fishList, Collections.reverseOrder(Fish.NAME_ORDER));
			break;
		case CATCHER_A:
			Collections.sort(fishList, Fish.CATCHER_ORDER);
			break;
		case CATCHER_D:
			Collections.sort(fishList, Collections.reverseOrder(Fish.CATCHER_ORDER));
			break;
		case WEIGHT_A:
			Collections.sort(fishList, Fish.WEIGHT_ORDER);
			break;
		case WEIGHT_D:
			Collections.sort(fishList, Collections.reverseOrder(Fish.WEIGHT_ORDER));
			break;
		case DATE_A:
			Collections.sort(fishList, Fish.DATE_ORDER);
			break;
		case DATE_D:
			Collections.sort(fishList, Collections.reverseOrder(Fish.DATE_ORDER));
			break;
		}
	}
	
	public List<Fish> getFishList() {
		return Collections.unmodifiableList(fishList);
	}
	
	public List<Fish> getFishList(String catcher) {
		catcher = catcher.toLowerCase();
		if (!fishMap.containsKey(catcher)) {
			throw new IllegalArgumentException("The name was not found.");
		}
		return new ArrayList<Fish>(fishMap.get(catcher));
	}
	
	public Fish getBiggest() {
		return biggestFish;
	}
	
	public Fish getBiggest(String catcher) {
		catcher = catcher.toLowerCase();
		if (!fishMap.containsKey(catcher)) {
			throw new IllegalArgumentException("The name was not found.");
		}
		return fishMap.get(catcher).last();
	}

	public Fish getSmallest() {
		return smlFish;
	}
	
	public Fish getSmallest(String catcher) {
		catcher = catcher.toLowerCase();
		if (!fishMap.containsKey(catcher)) {
			throw new IllegalArgumentException("The name was not found.");
		}
		return fishMap.get(catcher).first();
	}
	
	public double getAverageWeight() {
		return avgFishWeight;
	}
	
	public double getAverageWeight(String catcher) {
		catcher = catcher.toLowerCase();
		if (!fishMap.containsKey(catcher)) {
			throw new IllegalArgumentException("The name was not found.");
		}
		
		double cumSum = 0;
		
		for (Fish f : fishMap.get(catcher)) {
			cumSum += f.weight();
		}
		
		double avg = cumSum / fishMap.get(catcher).size();
		avg = Fish.truncate(avg);
		
		return avg;
	}
	
	public long getLastFishCatchTime() {
		return lastFishCatchTime;
	}
	
	public boolean lastCaughtIsBiggest() {
		return lastCaughtIsBiggest;
	}

	public static enum SortOrder {
		/**
		 * Sort fish by name in ascending order.
		 */
		NAME_A,
		
		/**
		 * Sort fish by name in descending order.
		 */
		NAME_D,
		
		/**
		 * Sort fish by catcher in ascending order.
		 */
		CATCHER_A,
		
		/**
		 * Sort fish by catcher in descending order.
		 */
		CATCHER_D,
		
		/**
		 * Sort fish by weight in ascending order.
		 */
		WEIGHT_A,
		
		/**
		 * Sort fish by weight in descending order.
		 */
		WEIGHT_D,
		
		/**
		 * Sort fish by date caught in ascending order.
		 */
		DATE_A,
		
		/**
		 * Sort fish by date caught in descending order.
		 */
		DATE_D
	}
}
