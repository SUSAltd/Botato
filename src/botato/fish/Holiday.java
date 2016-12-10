package botato.fish;

import java.util.Calendar;

public class Holiday {
	
	public static enum HolidayName {
		
		/**
		 * St. Valentine's Day
		 */
		VALENTINE,
		
		/**
		 * St. Patrick's Day
		 */
		PATRICK,
		
		/**
		 * Halloween
		 */
		HALLOWEEN,
		
		/**
		 * Christmas
		 */
		CHRISTMAS,
		
		/**
		 * No particular holiday
		 */
		DEFAULT
	}
	private HolidayName name;
	private Calendar startDate;
	private Calendar endDate;
	
	public Holiday(HolidayName name, int startMonth, int startDay, int endMonth, int endDay) {
		this.name = name;
		
		startDate = Calendar.getInstance();
		int currYear = startDate.get(Calendar.YEAR);
		startDate.set(currYear, startMonth - 1, startDay, 0, 0, 0);
		
		endDate = Calendar.getInstance();
		endDate.set(currYear, endMonth - 1, endDay, 23, 59, 59);
		
		// takes care of wrapping over to the next year
		if (endDate.before(startDate))
			endDate.set(Calendar.YEAR, currYear + 1);
	}
	
	public HolidayName getName() {
		return this.name;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}
}