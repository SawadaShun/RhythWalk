package com.example.android.rhythwalk;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

/**
 * 外部ストレージ上の音楽をあらわすクラス。
 */
public class Item implements Comparable<Object> {
	private static final String TAG = "Item";
	final long id;
	final String artist;
	final String title;
	final String album;
	final int truck;
	final long duration;
	final int spring;
	final int summer;
	final int autumn;
	final int winter;
	final int morning;
	final int daytime;
	final int evening;
	final int night;
	final int sunny;
	final int cloudy;
	final int rain;
	final int snow;
	final int sea;
	final int mountain;
	final int forest;
	final int city;

	public Item(long id, String artist, String title, String album, int truck,
			long duration, int spring, int summer, int autumn, int winter,
			int morning, int daytime, int evening, int night, int sunny,
			int cloudy, int rain, int snow, int sea, int mountain, int forest,
			int city) {

		this.id = id;
		this.artist = artist;
		this.title = title;
		this.album = album;
		this.truck = truck;
		this.duration = duration;

		this.spring = spring;
		this.summer = summer;
		this.autumn = autumn;
		this.winter = winter;

		this.morning = morning;
		this.daytime = daytime;
		this.evening = evening;
		this.night = night;

		this.sunny = sunny;
		this.cloudy = cloudy;
		this.rain = rain;
		this.snow = snow;

		this.sea = sea;
		this.mountain = mountain;
		this.forest = forest;
		this.city = city;
	}

	public Uri getURI() {
		return ContentUris.withAppendedId(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
	}

	/**
	 * 外部ストレージ上から音楽を探してリストを返す。
	 * 
	 * @param context
	 *            コンテキスト
	 * @return 見つかった音楽のリスト
	 */
	public static List<Item> getItems(Context context) {
		List<Item> items = new LinkedList<Item>();

		// ContentResolver を取得
		ContentResolver cr = context.getContentResolver();

		// 外部ストレージから音楽を検索
		Cursor cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				null, MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);

		if (cur != null) {
			if (cur.moveToFirst()) {
				Log.i(TAG, "Listing...");

				// 曲情報のカラムを取得
				int artistColumn = cur
						.getColumnIndex(MediaStore.Audio.Media.ARTIST);
				int titleColumn = cur
						.getColumnIndex(MediaStore.Audio.Media.TITLE);
				int albumColumn = cur
						.getColumnIndex(MediaStore.Audio.Media.ALBUM);
				int durationColumn = cur
						.getColumnIndex(MediaStore.Audio.Media.DURATION);
				int idColumn = cur.getColumnIndex(MediaStore.Audio.Media._ID);
				int idTruck = cur.getColumnIndex(MediaStore.Audio.Media.TRACK);

				Log.i(TAG, "Title column index: " + String.valueOf(titleColumn));
				Log.i(TAG, "ID column index: " + String.valueOf(idColumn));

				// リストに追加
				do {
					SongSituationDB song = new SongSituationDB(
							cur.getInt(durationColumn));

					Log.i(TAG,
							"Duration: " + cur.getLong(durationColumn)
									+ " ID: " + cur.getString(idColumn)
									+ " Title: " + cur.getString(titleColumn)
									+ " Spring: " + song.getSpring());
					items.add(new Item(cur.getLong(idColumn), cur
							.getString(artistColumn), cur
							.getString(titleColumn),
							cur.getString(albumColumn), cur.getInt(idTruck),
							cur.getLong(durationColumn),

							song.getSpring(), song.getSummer(), song
									.getAutumn(), song.getWinter(),

							song.getMorning(), song.getDaytime(), song
									.getEvening(), song.getNight(),

							song.getSunny(), song.getCloudy(), song.getRain(),
							song.getSnow(),

							song.getSea(), song.getMountain(),
							song.getForest(), song.getCity()));

				} while (cur.moveToNext());

				Log.i(TAG, "Done querying media. MusicRetriever is ready.");
			}
			// カーソルを閉じる
			cur.close();
		}

		Collections.sort(items);
		return items;
	}

	@Override
	public int compareTo(Object another) {
		/*
		 * if (another == null) { return 1; }
		 */
		Item item = (Item) another;

		if (ConfigActivity.seasonSwitch) {

			int season = 0;
			switch (SeasonAnalyze.numSeasonCase) {
			case 1:
				season = item.spring - this.spring;
				break;
			case 2:
				season = item.summer - this.summer;
				break;
			case 3:
				season = item.autumn - this.autumn;
				break;
			case 4:
				season = item.winter - this.winter;
				break;

			}
			
			return season;

		} else if (ConfigActivity.timeSwitch) {

			int time = 0;
			switch (TimeAnalyze.numTimeCase) {
			case 1:
				time = item.morning - this.morning;
				break;
			case 2:
				time = item.evening - this.evening;
				break;
			case 3:
				time = item.daytime - this.daytime;
				break;
			case 4:
				time = item.night - this.night;
				break;

			}
			
			return time;
			
		} else if (ConfigActivity.placeSwitch) {

			int place = 0;
			switch (PlaceAnalyze.numPlaceCase) {
			case 1:
				place = item.sea - this.sea;
				break;
			case 2:
				place = item.mountain - this.mountain;
				break;
			case 3:
				place = item.forest - this.forest;
				break;
			case 4:
				place = item.city - this.city;
				break;

			}
			
			return place;
			
		} else if (ConfigActivity.weatherSwitch) {

			int weather = 0;
			
			switch (WeatherAnalyze.numWeatherCase) {
			case 1:
				weather = item.rain - this.rain;
				break;
			case 2:
				weather = item.snow - this.snow;
				break;
			case 3:
				weather = item.sunny - this.sunny;
				break;
			case 4:
				weather = item.cloudy - this.cloudy;
				break;

			}
			
			return weather;
			
		} else {
			return truck - item.truck;
		}
		/*
		 * int result = spring.compareTo(item.spring); if (result != 0) { return
		 * result; }
		 */
	}
}
