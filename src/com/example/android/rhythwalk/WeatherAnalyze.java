/**
 * 
 */
package com.example.android.rhythwalk;

/**
 * @author b1012046
 *
 */
public class WeatherAnalyze {

	private String weatherString = null;
	int ID = 0;
	private int numWeatherCase = getWeatherNumber(ID);

	public String getWeatherString() {

		switch (numWeatherCase) {
		case 1:
			weatherString = "Rain";
			break;
		case 2:
			weatherString = "Snow";
			break;
		case 3:
			weatherString = "Sunny";
			break;
		case 4:
			weatherString = "Cloudy";
			break;
		case 5:
			weatherString = "Unknown";
			break;

		}
		return weatherString;
	}

	public void setWeatherNumber(int setWeather) {
		numWeatherCase = setWeather;
	}

	public int getWeatherNumber(int nowWeather) {
		if (nowWeather >= 200 && nowWeather <= 531)
			return 1;// Rain
		else if (nowWeather >= 600 && nowWeather <= 622)
			return 2;// Snow
		else if (nowWeather >= 800 && nowWeather <= 801)
			return 3;// Sunny
		else if (nowWeather >= 802 && nowWeather <= 804)
			return 4;// Clouds
		else return 5;
	}

}
