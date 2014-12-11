/**
 * 
 */
package com.example.android.rhythwalk;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

/**
 * 
 * 音楽のシチュエーションをサーバ、ファイル、アセットから取得できるクラス
 * 
 * @author kiefer7126
 *
 */
public class SongSituationDB {

	private long id;
	private int spring;
	private int summer;
	private int autumn;
	private int winter;
    private int morning;
    private int evening;
    private int daytime;
    private int night;
    private int sunny;
    private int cloudy;
    private int rain;
    private int snow;
    private int sea;
    private int mountain;
    private int forest;
    private int city;
    private int bpm;
    private static final String FILENAME = "SongSituationDB.txt";
    public final String SPRING = "spring";
    public final String SUMMER = "summer";
    public final String AUTUMN = "autumn";
    public final String WINTER = "winter";
    public final String MORNING = "morning";
    public final String EVENING = "evening";
    public final String DAYTIME = "daytime";
    public final String NIGHT = "night";
    public final String SUNNY = "sunny";
    public final String CLOUDY = "cloudy";
    public final String RAIN = "rain";
    public final String SNOW = "snow";
    public final String SEA = "sea";
    public final String MOUNTAIN = "mountain";
    public final String FOREST = "forest";
    public final String CITY = "city";
    public final String BPM = "bpm";
    /**
     * 未解析のときの値
     */
    public static final int DEFAULT_SPRING = -2;
    /**
     * 失敗などで解析できない時の値
     */
    public static final int UNKNOWN_SPRING = -1;
    /**
     * 未解析のときの値
     */
    public static final int DEFAULT_BPM = -2;
    /**
     * 失敗などで解析できない時の値
     */
    public static final int UNKNOWN_BPM = -1;
    
    /**
     * 空のシチュエーションDBを作成する
     */
	SongSituationDB() {
		id = 0;
		spring = DEFAULT_SPRING;
		summer = 0;
		autumn = 0;
		winter = 0;
		morning = 0;
	    evening = 0;
	    daytime = 0;
	    night = 0;
	    sunny = 0;
		cloudy = 0;
		rain = 0;
		snow = 0;
		sea = 0;
		mountain = 0;
		forest = 0;
		city = 0;
		bpm = DEFAULT_BPM;
	}
	
//	public int getMorning() {
//		return morning;
//	}
//
//	public int getDaytime() {
//		return daytime;
//	}
//
//	public int getEvening() {
//		return evening;
//	}
//
//	public int getNight() {
//		return night;
//	}
//
//	public void setMorning(int morning) {
//		this.morning = morning;
//	}
//
//	public void setDaytime(int daytime) {
//		this.daytime = daytime;
//	}
//
//	public void setEvening(int evening) {
//		this.evening = evening;
//	}
//
//	public void setNight(int night) {
//		this.night = night;
//	}
//	
//	public int getSpring() {
//		return spring;
//	}
//
//	public int getSummer() {
//		return summer;
//	}
//
//	public int getAutumn() {
//		return autumn;
//	}
//
//	public int getWinter() {
//		return winter;
//	}
//
//	public void setSpring(int spring) {
//		this.spring = spring;
//	}
//
//	public void setSummer(int summer) {
//		this.summer = summer;
//	}
//
//	public void setAutumn(int autumn) {
//		this.autumn = autumn;
//	}
//
//	public void setWinter(int winter) {
//		this.winter = winter;
//	}
//	
//	public int getSunny() {
//		return sunny;
//	}
//
//	public int getCloudy() {
//		return cloudy;
//	}
//
//	public int getRain() {
//		return rain;
//	}
//
//	public int getSnow() {
//		return snow;
//	}
//
//	public void setSunny(int sunny) {
//		this.sunny = sunny;
//	}
//
//	public void setCloudy(int cloudy) {
//		this.cloudy = cloudy;
//	}
//
//	public void setRain(int rain) {
//		this.rain = rain;
//	}
//
//	public void setSnow(int snow) {
//		this.snow = snow;
//	}
//	
//	public int getSea() {
//		return sea;
//	}
//
//	public int getMountain() {
//		return mountain;
//	}
//
//	public int getForest() {
//		return forest;
//	}
//
//	public int getCity() {
//		return city;
//	}
//
//	public void setSea(int sea) {
//		this.sea = sea;
//	}
//
//	public void setMountain(int mountain) {
//		this.mountain = mountain;
//	}
//
//	public void setForest(int forest) {
//		this.forest = forest;
//	}
//
//	public void setCity(int city) {
//		this.city = city;
//	}
//	
//	public int getBPM(){
//		return bpm;
//	}
//	
//	public void setBPM(int bpm){
//		this.bpm = bpm;
//	}
	
	/**
	 * 
	 * IDを返す
	 * 
	 * @return
	 */
	public long getID() {
		return id;
	}
	
	/**
	 * 
	 * IDを設定する
	 * 
	 * @param id
	 */
	public void setID(long id){
		this.id = id;  
	}
	
	/**
	 * 
	 * int型のパラメータを返す<br>
	 * 各シチュエーションの値など<br>
	 * 
	 * @param key パラメータ名
	 * @return パラメータの値。keyのパラメータ名が存在しないときは-1
	 */
	public int getInt(String key){
		int value;
		if (key.equals(SPRING)) {
			value = spring;
		} else if (key.equals(SUMMER)) {
			value = summer;
		} else if (key.equals(AUTUMN)) {
			value = autumn;
		} else if (key.equals(WINTER)) {
			value = winter;
		} else if (key.equals(MORNING)) {
			value = morning;
		} else if (key.equals(DAYTIME)) {
			value = daytime;
		} else if (key.equals(EVENING)) {
			value = evening;
		} else if (key.equals(NIGHT)) {
			value = night;
		} else if (key.equals(SUNNY)) {
			value = sunny;
		} else if (key.equals(CLOUDY)) {
			value = cloudy;
		} else if (key.equals(RAIN)) {
			value = rain;
		} else if (key.equals(SNOW)) {
			value = snow;
		} else if (key.equals(SEA)) {
			value = sea;
		} else if (key.equals(MOUNTAIN)) {
			value = mountain;
		} else if (key.equals(FOREST)) {
			value = forest;
		} else if (key.equals(CITY)) {
			value = city;
		} else if (key.equals(BPM)) {
			value = bpm;
		} else {
			value = -1;
		}
		return value;
	}
	
	/**
	 * 
	 * int型のパラメータを設定する<br>
	 * 各シチュエーションの値など<br>
	 * 
	 * @param key パラメータ名
	 * @param value パラメータの値
	 */
	public void setInt(String key, int value){
		if (key.equals(SPRING)) {
			spring = value;
		} else if (key.equals(SUMMER)) {
			summer = value;
		} else if (key.equals(AUTUMN)) {
			autumn = value;
		} else if (key.equals(WINTER)) {
			winter = value;
		} else if (key.equals(MORNING)) {
			morning = value;
		} else if (key.equals(DAYTIME)) {
			daytime = value;
		} else if (key.equals(EVENING)) {
			evening = value;
		} else if (key.equals(NIGHT)) {
			night = value;
		} else if (key.equals(SUNNY)) {
			sunny = value;
		} else if (key.equals(CLOUDY)) {
			cloudy = value;
		} else if (key.equals(RAIN)) {
			rain = value;
		} else if (key.equals(SNOW)) {
			snow = value;
		} else if (key.equals(SEA)) {
			sea = value;
		} else if (key.equals(MOUNTAIN)) {
			mountain = value;
		} else if (key.equals(FOREST)) {
			forest = value;
		} else if (key.equals(CITY)) {
			city = value;
		} else if (key.equals(BPM)) {
			bpm = value;
		}
	}
	
	/**
	 * 
	 * 将来用。サーバを検索し、何かに一致するSongSituationDBを返す<br>
	 * 返るのはリストではない<br>
	 * 
	 * @return 
	 */
	public static List<SongSituationDB> getSongSituationDBByServer(){
		return null;
	}
	
	/**
	 * 
	 * 保存ファイルを読み出して、リストを返す
	 * 
	 * @param context Activityあたりから取得できるコンテキスト
	 * @return 読み出したSongSituationDBのリスト
	 */
	public static List<SongSituationDB> getSongSituationDBsByFile(Context context){
		List<SongSituationDB> dbs = new ArrayList<SongSituationDB>();
		String dbs_text = FileWriterUtility.readPublicFile(context, FILENAME);
		if (dbs_text != null) {
			String[] rows = dbs_text.split("\n\n");
			for (int i = 0; i < rows.length; i++) {
				SongSituationDB db = new SongSituationDB();
				String[] columns = rows[i].split("\n");
				for (int j = 0; j < columns.length; j++) {
					String[] keyvalue = columns[j].split("="); 
					String key = keyvalue[0];
					String value;
					try {
						value = keyvalue[1];
					} catch (ArrayIndexOutOfBoundsException e) {
						value = "";
					}
					if (key.equals("id")) {
						long db_id;
						try {
							db_id = Long.parseLong(value);
						} catch (NumberFormatException e) {
							db_id = 0;
						}
						db.setID(db_id);
					}else{
						int value_int;
						try {
							value_int = Integer.parseInt(value);
						} catch (NumberFormatException e) {
							value_int = UNKNOWN_SPRING;
						}
						db.setInt(key, value_int);						
					}
				}
				dbs.add(db);
			}
		}		
		return dbs;
	}
	
	/**
	 * 
	 * リストをファイルに保存する
	 * 
	 * @param context Activityあたりから取得できるコンテキスト
	 * @param dbs ファイルに書き込むリスト
	 * @return 書き込んだSongSituationDBのリスト。失敗のときはnull
	 */
	public static List<SongSituationDB> saveSongSituationDBsByFile(Context context, List<SongSituationDB> dbs){
		String dbs_text = "";
		for (int i = 0; i < dbs.size(); i++) {
			SongSituationDB db = dbs.get(i);
			dbs_text += "id=" + db.getID() + "\n";
			dbs_text += db.SPRING + "=" + db.getInt(db.SPRING) + "\n";
			dbs_text += db.SUMMER + "=" + db.getInt(db.SUMMER) + "\n";
			dbs_text += db.AUTUMN + "=" + db.getInt(db.AUTUMN) + "\n";
			dbs_text += db.WINTER + "=" + db.getInt(db.WINTER) + "\n";
			dbs_text += db.MORNING + "=" + db.getInt(db.MORNING) + "\n";
			dbs_text += db.EVENING + "=" + db.getInt(db.EVENING) + "\n";
			dbs_text += db.DAYTIME + "=" + db.getInt(db.DAYTIME) + "\n";
			dbs_text += db.NIGHT + "=" + db.getInt(db.NIGHT) + "\n";
			dbs_text += db.SUNNY + "=" + db.getInt(db.SUNNY) + "\n";
			dbs_text += db.CLOUDY + "=" + db.getInt(db.CLOUDY) + "\n";
			dbs_text += db.RAIN + "=" + db.getInt(db.RAIN) + "\n";
			dbs_text += db.SNOW + "=" + db.getInt(db.SNOW) + "\n";
			dbs_text += db.SEA + "=" + db.getInt(db.SEA) + "\n";
			dbs_text += db.MORNING + "=" + db.getInt(db.MORNING) + "\n";
			dbs_text += db.FOREST + "=" + db.getInt(db.FOREST) + "\n";
			dbs_text += db.CITY + "=" + db.getInt(db.CITY) + "\n";
			dbs_text += db.BPM + "=" + db.getInt(db.BPM) + "\n";
			dbs_text += "\n";
		}
		dbs_text = dbs_text.substring(0, dbs_text.length() - 1);
		String result = FileWriterUtility.writePublicFile(context, FILENAME, dbs_text);
		if (result == null) {
			dbs = null;
		}
		return dbs;
	}

	/**
	 * 
	 * アセットファイルを読み出して、リストを返す
	 * 
	 * @param context Activityあたりから取得できるコンテキスト
	 * @return 読み出したSongSituationDBのリスト
	 */
	public static List<SongSituationDB> getSongSituationDBsByAsset(Context context){
		List<SongSituationDB> dbs = new ArrayList<SongSituationDB>();
		String dbs_text = FileWriterUtility.readAssetsFile(context, "SongSituationDB2.txt");
		if (dbs_text != null) {
			String[] rows = dbs_text.split("\n\n");
			for (int i = 0; i < rows.length; i++) {
				SongSituationDB db = new SongSituationDB();
				String[] columns = rows[i].split("\n");
				for (int j = 0; j < columns.length; j++) {
					String[] keyvalue = columns[j].split("="); 
					String key = keyvalue[0];
					String value;
					try{
						value = keyvalue[1];
					}catch(ArrayIndexOutOfBoundsException e){
						value = "";
					}
					if (key.equals("durationid")) {
						long db_id;
						try {
							db_id = Long.parseLong(value);
						} catch (NumberFormatException e) {
							db_id = 0;
						}
						db.setID(db_id);
					}else{
						
						int value_int;
						try{
							value_int = Integer.parseInt(value);
						}catch(NumberFormatException e){
							value_int = UNKNOWN_SPRING;
						}
						db.setInt(key, value_int);				
					}
				}
				dbs.add(db);
			}
		}
		return dbs;
	}
}
