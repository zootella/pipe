package pipe.main;

import java.util.Calendar;
import java.util.Date;

import base.time.Now;

public class Snippet {

	public static void snippet(Program program) {

		/*
		Now now = new Now();
		System.out.println("raw: " + now.time);
		
		Date date = new Date(now.time);
		
		/*
		System.out.println("cmt string:    " + date.toGMTString());
		System.out.println("locale string: " + date.toLocaleString());
		System.out.println("string:        " + date.toString());
		/
		
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		
		
		
		int i;
		i = c.get(Calendar.DAY_OF_WEEK);
		i = c.get(Calendar.HOUR);
		i = c.get(Calendar.MINUTE);
		i = c.get(Calendar.SECOND);
		i = c.get(Calendar.MILLISECOND);

		String s = "";
		if (c.get(Calendar.AM_PM) == Calendar.AM)
			s += " AM";
		else
			s += " PM";

		int day = c.get(Calendar.DAY_OF_WEEK);
		switch (day) {
		case Calendar.MONDAY:
		case 		Calendar.TUESDAY:
				Calendar.WEDNESDAY:
				Calendar.THURSDAY:
				Calendar.FRIDAY:
				Calendar.SATURDAY:
				Calendar.SUNDAY:
		}
		
		
		
		
		
		// Tue 2:15:12.358 AM
		
		// 2009 Aug 5 2:15p 22s
		*/

	}
	
	public static String numerals(long n, int digits) {
		if (digits < 1) throw new IllegalArgumentException();
		String s = n + "";
		while (s.length() < digits)
			s = "0" + s;
		return s;
	}
}
