/**
 * 
 */
package com.example.android.rhythwalk;

/**
 * @author b1012046
 *
 */
public class PlaceAnalyze {
	
	private String placeString = null;
	int ID = 0;
	private int numPlaceCase = getPlaceNumber(ID);

	public String getPlaceString() {

		switch (numPlaceCase) {
		case 1:
			placeString = "Sea";
			break;
		case 2:
			placeString = "Mountain";
			break;
		case 3:
			placeString = "Forest";
			break;
		case 4:
			placeString = "City";
			break;
		case 5:
			placeString = "Unknown";
			break;

		}
		return placeString;
	}

	public void setPlaceNumber(int setPlace) {
		numPlaceCase = setPlace;
	}

	public int getPlaceNumber(int nowplace) {
		if (nowplace >= 1 && nowplace <= 5)
			return 1;
		else if (nowplace >= 6 && nowplace <= 10)
			return 2;
		else if (nowplace >= 11 && nowplace <= 15)
			return 3;
		else if (nowplace >= 16 && nowplace <= 20)
			return 4;
		
		else return 5;
	}

}
