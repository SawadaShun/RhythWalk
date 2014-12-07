package com.example.android.rhythwalk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.content.Context;
import android.util.Log;

/**
 * 
 * ファイル読み書きいろいろ<br>
 * ぜんぶstaticメソッド。FileWriterUtil.methodname(～);で使える
 * 
 * @author mikankari
 *
 */
public class FileWriterUtility {
	
	/**
	 * 
	 * 誰でもアクセスできるファイルを読み出します<br>
	 * 読み出し場所は/mnt/sdcard/Android/data/パッケージ名/files/ファイル名<br>
	 * 
	 * @param context Activityあたりから取得できるコンテキスト
	 * @param filename ファイル名
	 * @return 読み出した内容。失敗のときはnull
	 */
	public static String readPublicFile(Context context, String filename){
		String text;
		try{					
			File file = new File(context.getExternalFilesDir(null), filename);
			FileInputStream in = new FileInputStream(file);
			BufferedInputStream stream = new BufferedInputStream(in);
			text = "";
			byte[] temp = new byte[stream.available()];
			while(stream.read(temp) != -1){
				text += new String(temp, "UTF-8");
			}
			stream.close();
		}catch(Exception exception){
			text = null;
			Log.e("", exception.toString() + " on reading");
		}
		return text;
	}

	/**
	 * 
	 * 誰でもアクセスできるファイルを書き込みます<br>
	 * 書き込み場所は/mnt/sdcard/Android/data/パッケージ名/files/ファイル名<br>
	 * 
	 * @param context Activityあたりから取得できるコンテキスト
	 * @param filename ファイル名
	 * @param text 書き込む内容
	 * @return 書き込んだ内容。失敗のときはnull
	 */
	public static String writePublicFile(Context context, String filename, String text){
		try{
			
					File file = new File(context.getExternalFilesDir(null), filename);
			FileOutputStream output = new FileOutputStream(file);
			BufferedOutputStream stream = new BufferedOutputStream(output);
			stream.write(text.getBytes("UTF-8"));
			stream.close();
			
		}catch(Exception exception){
			text = null;
			Log.e("", exception.toString() + "on writing");
		}				
		return text;
	}

	/**
	 * 
	 * アプリだけがアクセスできるファイルを読み出します<br>
	 * 読み出し場所は/data/パッケージ名/files/ファイル名<br>
	 * 
	 * @param context Activityあたりから取得できるコンテキスト
	 * @param filename ファイル名
	 * @return 読み出した内容。失敗のときはnull
	 */
	public static String readPrivateFile(Context context, String filename){
		String text;
		try{					
			File file = new File(context.getExternalFilesDir(null), filename);
			FileInputStream in = context.openFileInput(file.getName());
			BufferedInputStream stream = new BufferedInputStream(in);
			text = "";
			byte[] temp = new byte[stream.available()];
			while(stream.read(temp) != -1){
				text += new String(temp, "UTF-8");
			}
			stream.close();
		}catch(Exception exception){
			text = null;
			Log.e("", exception.toString() + " on reading");
		}
		return text;
	}

	/**
	 * 
	 * アプリだけがアクセスできるファイルを書きだします<br>
	 * 書き出し場所は/data/パッケージ名/files/ファイル名<br>
	 * 
	 * @param context Activityあたりから取得できるコンテキスト
	 * @param filename ファイル名
	 * @param text 書き込む内容
	 * @return 書き込んだ内容。失敗のときはnull
	 */
	public static String writePrivateFile(Context context, String filename, String text){
		try{
			
			File file = new File(context.getExternalFilesDir(null), filename);
			FileOutputStream output = context.openFileOutput(file.getName(), context.MODE_PRIVATE);
			BufferedOutputStream stream = new BufferedOutputStream(output);
			stream.write(text.getBytes("UTF-8"));
			stream.close();
			
		}catch(Exception exception){
			text = null;
			Log.e("", exception.toString() + "on writing");
		}
		return text;
	}


}
