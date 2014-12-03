/**
 * 
 */
package com.example.android.rhythwalk;

import java.util.Calendar;

/**
 * @author b1012046
 *
 */
public class TimeAnalyze {

    static Calendar calendar = Calendar.getInstance();
    
	private String timeString = null;
	static int time = calendar.get(Calendar.HOUR_OF_DAY);
	static int numTimeCase = getTimeNumber(time); 
			
    
	public String getTimeString() {

		switch (numTimeCase) {
		case 1:
			timeString = "Morning";
			break;
		case 2:
			timeString = "Evening";
			break;
		case 3:
			timeString = "Daytime";
			break;
		case 4:
			timeString = "Night";
			break;
		}
		return timeString;
	}
	
	public void setTimeNumber(int setTime){
		numTimeCase = setTime;
	}

	public static int getTimeNumber(int nowTime) {
		if (nowTime >= 4 && nowTime <= 10)
			return 1;// Morning
		else if (nowTime >= 11 && nowTime <= 15)
			return 2; // Daytime
		else if (nowTime >= 16 && nowTime <= 18)
			return 3;// Evening
		else
			return 4;// Night
	}

}
