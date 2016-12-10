package botato.test;

import java.util.Calendar;


public class CalendarTest {

	public static void main(String[] args) {

		Calendar date = Calendar.getInstance();
		int currYear = date.get(Calendar.HOUR_OF_DAY);
		
		
		System.out.println(currYear);
//		date.set(year, month, date, hourOfDay, minute, second);
	}	

}
