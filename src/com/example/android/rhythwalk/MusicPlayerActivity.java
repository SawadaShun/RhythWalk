package com.example.android.rhythwalk;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MediaPlayer を直接使用する音楽プレイヤー。
 */
public class MusicPlayerActivity extends Activity implements View.OnClickListener,
		MediaPlayer.OnPreparedListener, MediaPlayer.OnInfoListener, MediaPlayer.OnCompletionListener {
	private static final String TAG = "MusicPlayerActivity";
	private MediaPlayer mMediaPlayer;
	private ImageButton mButtonPlayPause;
	private ImageButton mButtonSkip;
	private ImageButton mButtonRewind;
	private ImageButton mButtonStop;
	private ImageButton mButtonConfig;
	private TextView mTextViewArtist;
	private TextView mTextViewAlbum;
	private TextView mTextViewTitle;
	private Chronometer mChronometer;
	private Handler mHandler = new Handler();
	private List<Item> mItems;
	private int mIndex;

	WalkCounterMaster ad;
	Timer mTimer;
	static long nowBPM;
	long startCounter = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_player);

		mButtonPlayPause = (ImageButton) findViewById(R.id.playpause);
		mButtonSkip = (ImageButton) findViewById(R.id.skip);
		mButtonRewind = (ImageButton) findViewById(R.id.rewind);
		mButtonStop = (ImageButton) findViewById(R.id.stop);
		mButtonConfig = (ImageButton) findViewById(R.id.config);
		mTextViewArtist = (TextView) findViewById(R.id.artist);
		mTextViewAlbum = (TextView) findViewById(R.id.album);
		mTextViewTitle = (TextView) findViewById(R.id.title);
		mChronometer = (Chronometer) findViewById(R.id.chronometer);
		final TextView bpmTxt = (TextView) findViewById(R.id.BPMText);
		
		mButtonPlayPause.setOnClickListener(this);
		mButtonSkip.setOnClickListener(this);
		mButtonRewind.setOnClickListener(this);
		mButtonStop.setOnClickListener(this);
		mButtonConfig.setOnClickListener(this);
		
		setEnabledButton(false);
		
		// センサーマネージャからサービスを取得
				SensorManager manager = (SensorManager) getSystemService(SENSOR_SERVICE);

				// 加速度センサーアダプタを生成
				ad = new WalkCounterMaster(manager);

				// タイマーを生成
				mTimer = new Timer(true);
								
				// 周期的に処理
				mTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						// mHandlerを通じてUI Threadへ処理をキューイング
						mHandler.post(new Runnable() {
							public void run() {
					
								if (ConfigActivity.bpmSwitch) {

									// 歩くBPMの計算式
									nowBPM = 6 * (ad.getCounter() - startCounter);

									// BPMの表示
									
									bpmTxt.setText("" + nowBPM);

								/**	 ここに歩くBPMと一致したBPMの音楽の再生する処理　*/

								}else{
									bpmTxt.setText(" - ");
								}
							
								// 計測初めの歩数の書き換え
								startCounter = ad.getCounter();
		
							}
						});
					}
				}, 0, 10000); // 0msから 10000ms(10s)間隔で繰り返す

				
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		
		mIndex = 0; //再開時ソートの頭から表示
		mItems = Item.getItems(getApplicationContext());
		
		
		if (mItems.size() != 0) {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnInfoListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			prepare();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
		if (mMediaPlayer != null) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
			mChronometer.stop();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	@Override
	public void onClick(View v) {
		boolean isPlaying = mMediaPlayer.isPlaying();
		if (v == mButtonPlayPause) {
			if (isPlaying) {
				mMediaPlayer.pause();
				mChronometer.stop();
				mButtonPlayPause.setImageResource(R.drawable.media_play);
				mButtonPlayPause.setContentDescription(getResources().getText(R.string.play));
			} else {
				mMediaPlayer.start();
				mChronometer.setBase(SystemClock.elapsedRealtime() - mMediaPlayer.getCurrentPosition());
				mChronometer.start();
				mButtonPlayPause.setImageResource(R.drawable.media_pause);
				mButtonPlayPause.setContentDescription(getResources().getText(R.string.pause));
			}
		} else if (v == mButtonSkip) {
			mIndex = (mIndex + 1) % mItems.size();
			onClick(mButtonStop);
			if (isPlaying) {
				onClick(mButtonPlayPause);
			}
		} else if (v == mButtonRewind) {
			//mMediaPlayer.seekTo(0);
			mChronometer.setBase(SystemClock.elapsedRealtime());
			
		    if(mIndex == 0){
		    mIndex = mItems.size();
		    }
		    
			if(mIndex > 0){
			mIndex = (mIndex - 1) % mItems.size();
			}
			
			Toast.makeText(this, "debug: " + mIndex, Toast.LENGTH_LONG).show();
			
			onClick(mButtonStop);
			if (isPlaying) {
				onClick(mButtonPlayPause);
			}
			
		} else if (v == mButtonStop) {
			mMediaPlayer.stop();
			mMediaPlayer.reset();
			mChronometer.stop();
			mChronometer.setBase(SystemClock.elapsedRealtime());
			prepare();
		} else if(v == mButtonConfig){
		
			Intent i = new Intent(this, ConfigActivity.class);
			startActivity(i);
			Toast.makeText(this, "Config", Toast.LENGTH_LONG).show();
		}
	}

	private void prepare() {
		setEnabledButton(false);

		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		Item playingItem = mItems.get(mIndex);
		try {
			mMediaPlayer.setDataSource(getApplicationContext(), playingItem.getURI());
			mMediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (SecurityException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IllegalStateException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		mTextViewArtist.setText(playingItem.artist);
		mTextViewAlbum.setText(playingItem.album);
		mTextViewTitle.setText(playingItem.title);
		mButtonPlayPause.setImageResource(R.drawable.media_play);
		mButtonPlayPause.setContentDescription(getResources().getText(R.string.play));
		mChronometer.setBase(SystemClock.elapsedRealtime());
	}

	private void setEnabledButton(final boolean enabled) {
		Log.d(TAG, "setEnabledButton:" + enabled);
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mButtonPlayPause.setEnabled(enabled);
				mButtonSkip.setEnabled(enabled);
				mButtonRewind.setEnabled(enabled);
				mButtonStop.setEnabled(enabled);
				mButtonConfig.setEnabled(enabled);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
		case KeyEvent.KEYCODE_HEADSETHOOK:
			onClick(mButtonPlayPause);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.d(TAG, "onPrepared");
		setEnabledButton(true);
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		Log.d(TAG, "onInfo:" + (what == MediaPlayer.MEDIA_INFO_UNKNOWN ? "MEDIA_INFO_UNKNOWN" :
			what == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING ? "MEDIA_INFO_VIDEO_TRACK_LAGGING" :
			what == MediaPlayer.MEDIA_INFO_BUFFERING_START ? "MEDIA_INFO_BUFFERING_START" :
			what == MediaPlayer.MEDIA_INFO_BUFFERING_END ? "MEDIA_INFO_BUFFERING_END" :
			what == MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING ? "MEDIA_INFO_BAD_INTERLEAVING" :
			what == MediaPlayer.MEDIA_INFO_NOT_SEEKABLE ? "MEDIA_INFO_NOT_SEEKABLE" :
			what == MediaPlayer.MEDIA_INFO_METADATA_UPDATE ? "MEDIA_INFO_METADATA_UPDATE" :
			"Unknown"));
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.d(TAG, "onCompletion");
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				onClick(mButtonSkip);
				while (!mButtonPlayPause.isEnabled()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
				onClick(mButtonPlayPause);
			}
		});
	}
}
