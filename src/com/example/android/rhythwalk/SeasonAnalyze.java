package com.example.android.rhythwalk;

import java.util.Calendar;

public class SeasonAnalyze {

	
	Calendar calendar = Calendar.getInstance();
	
	private String seasonString = null;
	int month = calendar.get(Calendar.MONTH) + 1;// 月の取得(1月=0)
    private int numSeasonCase = getSeasonNumber(month);
	
	public String getSeasonString() {

		switch (numSeasonCase) {
		case 1:
			seasonString = "Spring";
			break;
		case 2:
			seasonString = "Summer";
			break;
		case 3:
			seasonString = "Autumn";
			break;
		case 4:
			seasonString = "Winter";
			break;

		}
		return seasonString;
	}

	public void setSeasonNumber(int setTime){
		numSeasonCase = setTime;
	}


	public int getSeasonNumber(int nowMonth) {
		if (nowMonth >= 3 && nowMonth <= 5)
			return 1;// Spring
		else if (nowMonth >= 6 && nowMonth <= 8)
			return 2;// Summer
		else if (nowMonth >= 9 && nowMonth <= 11)
			return 3;// Autumn
		else
			return 4;// Winter
	}

}
