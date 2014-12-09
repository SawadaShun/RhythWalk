package com.example.android.rhythwalk;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ConfigActivity extends Activity implements
		OnClickListener {

	// MainActivity ma = new MainActivity();

	static SeasonAnalyze sa = new SeasonAnalyze();
	static TimeAnalyze ta = new TimeAnalyze();
	static WeatherAnalyze wa = new WeatherAnalyze();
	static PlaceAnalyze pa = new PlaceAnalyze();

	Calendar calendar = Calendar.getInstance();
	public SeekBar Tsb;

	static boolean timeSwitch = false;
	static boolean weatherSwitch = false;
	static boolean seasonSwitch = false;
	static boolean placeSwitch = false;
	static boolean bpmSwitch = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);

		// BPMのON・OFFの切り替え
		ToggleButton tb1 = (ToggleButton) findViewById(R.id.toggleButton1);
		tb1.setOnClickListener(this);
		tb1.setChecked(bpmSwitch);// 初期状態

		final TextView Ttv = (TextView) findViewById(R.id.TtvSeek);
		final TextView Ttx = (TextView) findViewById(R.id.Timetext);
		Tsb = (SeekBar) findViewById(R.id.Time);

		Ttx.setText("Time: " + ta.getTimeString());
		Ttv.setText(String.valueOf(Tsb.getProgress()));
		if (timeSwitch) {
			Tsb.setProgress(100);
		} else {
			Tsb.setProgress(0);
		}

		Tsb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Ttv.setText("トラッキング終了");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				Ttv.setText("トラッキング開始");
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				Ttv.setText(String.valueOf(progress));

			}
		});

		final TextView Wtv = (TextView) findViewById(R.id.WtvSeek);
		final TextView Wtx = (TextView) findViewById(R.id.Weathertext);
		final SeekBar Wsb = (SeekBar) findViewById(R.id.Weather);
		Wtv.setText(String.valueOf(Wsb.getProgress()));
		
		Wtx.setText("Weather: " + wa.getWeatherString());
		
		if (weatherSwitch) {
			Wsb.setProgress(100);
		} else {
			Wsb.setProgress(0);
		}
		Wsb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Wtv.setText("トラッキング終了");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				Wtv.setText("トラッキング開始");
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				Wtv.setText(String.valueOf(progress));
			}
		});

		final TextView Stv = (TextView) findViewById(R.id.StvSeek);
		final TextView Stx = (TextView) findViewById(R.id.Seasontext);
		final SeekBar Ssb = (SeekBar) findViewById(R.id.Season);
		Stv.setText(String.valueOf(Ssb.getProgress()));

		Stx.setText("Season: " + sa.getSeasonString());
		if (seasonSwitch) {
			Ssb.setProgress(100);
		} else {
			Ssb.setProgress(0);
		}
		Ssb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Stv.setText("トラッキング終了");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				Stv.setText("トラッキング開始");
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				Stv.setText(String.valueOf(progress));
			}
		});

		final TextView Ptv = (TextView) findViewById(R.id.PtvSeek);
		final TextView Ptx = (TextView) findViewById(R.id.Placetext);
		final SeekBar Psb = (SeekBar) findViewById(R.id.Place);
		Ptv.setText(String.valueOf(Psb.getProgress()));
		
		Ptx.setText("Place: " + pa.getPlaceString());
		
		if (placeSwitch) {
			Psb.setProgress(100);
		} else {
			Psb.setProgress(0);
		}
		Psb.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Ptv.setText("トラッキング終了");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				Ptv.setText("トラッキング開始");
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				Ptv.setText(String.valueOf(progress));
			}
		});

		View button1 = findViewById(R.id.button1);
		button1.setOnClickListener(this);
		View button2 = findViewById(R.id.button2);
		button2.setOnClickListener(this);
	}

	public void spinnerSelect(String itemm) {

	}

	// ボタンを押した時の処理
	public void onClick(View v) {
		switch (v.getId()) {

		// "Done"ボタンを押した時の処理
		case R.id.button1:

			// 設定画面(ConfigActivity)の終了
			this.finish();

			break;

		// "Debug"ボタンを押した時の処理
		case R.id.button2:

			Intent i = new Intent(this, DebugActivity.class);
			startActivity(i);
			this.finish();

			break;

		case R.id.toggleButton1:
			if (bpmSwitch) {
				bpmSwitch = false;
			} else {
				bpmSwitch = true;
			}
			break;
		}
	}

}
