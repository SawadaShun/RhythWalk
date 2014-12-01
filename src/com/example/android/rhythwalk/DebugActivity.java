package com.example.android.rhythwalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class DebugActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_debug);

		// final TimeAnalyze ta = new TimeAnalyze();

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// アイテムを追加します
		adapter.add("Time");
		adapter.add("Night");
		adapter.add("Daytime");
		adapter.add("Evening");
		adapter.add("Morning");
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setAdapter(adapter);

		// スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Spinner spinner = (Spinner) parent;
				// 選択されたアイテムを取得します
				String item = (String) spinner.getSelectedItem();

				// Toast.makeText(ConfigActivity.this, "debug:" + item,
				// Toast.LENGTH_LONG).show();

				ConfigActivity.timeSwitch = true;
				ConfigActivity.weatherSwitch = false;
				ConfigActivity.seasonSwitch = false;
				ConfigActivity.placeSwitch = false;
				
				if (item.equals("Night")) {
					// MainActivity.cs.setSituationState(1);
					ConfigActivity.ta.setTimeNumber(4);
					// Toast.makeText(ConfigActivity.this, item,
					// Toast.LENGTH_LONG).show();

				} else if (item.equals("Daytime")) {
					// MainActivity.cs.setSituationState(2);
					ConfigActivity.ta.setTimeNumber(3);
					// Toast.makeText(ConfigActivity.this, item,
					// Toast.LENGTH_LONG).show();

				} else if (item.equals("Evening")) {
					// MainActivity.cs.setSituationState(2);
					ConfigActivity.ta.setTimeNumber(2);
					// Toast.makeText(ConfigActivity.this, item,
					// Toast.LENGTH_LONG).show();

				} else if (item.equals("Morning")) {
					// MainActivity.cs.setSituationState(3);
					ConfigActivity.ta.setTimeNumber(1);
					// Toast.makeText(ConfigActivity.this, "else:" + item,
					// Toast.LENGTH_LONG).show();

				} else {

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// アイテムを追加します
		adapter2.add("Season");
		adapter2.add("Spring");
		adapter2.add("Summer");
		adapter2.add("Autumn");
		adapter2.add("Winter");

		Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner2.setAdapter(adapter2);
		// スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
		spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Spinner spinner2 = (Spinner) parent;
				// 選択されたアイテムを取得します
				String item2 = (String) spinner2.getSelectedItem();

				ConfigActivity.timeSwitch = false;
				ConfigActivity.weatherSwitch = false;
				ConfigActivity.seasonSwitch = true;
				ConfigActivity.placeSwitch = false;
				
				if (item2.equals("Spring")) {
					//MainActivity.cs.setSituationState(4);
					ConfigActivity.sa.setSeasonNumber(1);
				} else if (item2.equals("Summer")) {
					//MainActivity.cs.setSituationState(5);
					ConfigActivity.sa.setSeasonNumber(2);
				} else if (item2.equals("Autumn")) {
					//MainActivity.cs.setSituationState(6);
					ConfigActivity.sa.setSeasonNumber(3);
				} else if (item2.equals("Winter")) {
					//MainActivity.cs.setSituationState(7);
					ConfigActivity.sa.setSeasonNumber(4);
				} else {

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// アイテムを追加します
		adapter3.add("Weather");
		adapter3.add("Sunny");
		adapter3.add("Cloudy");
		adapter3.add("Rain");
		adapter3.add("Snow");

		Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
		spinner3.setAdapter(adapter3);
		// スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
		spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Spinner spinner3 = (Spinner) parent;
				// 選択されたアイテムを取得します
				String item3 = (String) spinner3.getSelectedItem();

				ConfigActivity.timeSwitch = false;
				ConfigActivity.weatherSwitch = true;
				ConfigActivity.seasonSwitch = false;
				ConfigActivity.placeSwitch = false;
				
				if (item3.equals("Sunny")) {
					//MainActivity.cs.setSituationState(8);
					ConfigActivity.wa.setWeatherNumber(3);
				} else if (item3.equals("Cloudy")) {
					//MainActivity.cs.setSituationState(9);
					ConfigActivity.wa.setWeatherNumber(4);
				} else if (item3.equals("Rain")) {
					//MainActivity.cs.setSituationState(10);
					ConfigActivity.wa.setWeatherNumber(1);
				} else if (item3.equals("Snow")) {
					//MainActivity.cs.setSituationState(11);
					ConfigActivity.wa.setWeatherNumber(2);
				} else if (item3.equals("Weather")){

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// アイテムを追加します
		adapter4.add("Place");
		adapter4.add("Sea");
		adapter4.add("Mountain");
		adapter4.add("Forest");
		adapter4.add("City");

		Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
		spinner4.setAdapter(adapter4);
		// スピナーのアイテムが選択された時に呼び出されるコールバックリスナーを登録します
		spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Spinner spinner4 = (Spinner) parent;
				// 選択されたアイテムを取得します
				String item4 = (String) spinner4.getSelectedItem();

				ConfigActivity.timeSwitch = false;
				ConfigActivity.weatherSwitch = false;
				ConfigActivity.seasonSwitch = false;
				ConfigActivity.placeSwitch = true;
				
				if (item4.equals("Sea")) {
//					MainActivity.cs.setSituationState(12);
					ConfigActivity.pa.setPlaceNumber(1);
				} else if (item4.equals("Mountain")) {
//					MainActivity.cs.setSituationState(13);
					ConfigActivity.pa.setPlaceNumber(2);
				} else if (item4.equals("Forest")) {
//					MainActivity.cs.setSituationState(14);
					ConfigActivity.pa.setPlaceNumber(3);
				} else if (item4.equals("City")) {
//					MainActivity.cs.setSituationState(15);
					ConfigActivity.pa.setPlaceNumber(4);
				} else {

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		View button1 = findViewById(R.id.button1);
		button1.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {

		// "Done"ボタンを押した時の処理
		case R.id.button1:
			Intent i = new Intent(this, ConfigActivity.class);
			startActivity(i);
			// Debug画面(DebugActivity)の終了
			this.finish();

			break;
		}
	}

}
