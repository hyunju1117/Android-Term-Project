package com.example.calendar_1394020_1394040;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonthFragment extends Fragment {
	ArrayList<String> mItems;
	ArrayAdapter<String> adapter;
	TextView textYear;
	TextView textMon;
	TextView dateText;
	MyDBHelper helper;
	String today;
	Cursor cursor;
	SimpleCursorAdapter cursorAdapter;
	ListView list;
	Date date = new Date();
	private static int year = new Date().getYear() + 1900;
	private static int mon = new Date().getMonth() + 1;
	final Calendar c = Calendar.getInstance();
	final SimpleDateFormat df = new SimpleDateFormat("yyyy / MM ",Locale.KOREA);


	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		((MainActivity)getActivity()).setTitle(Html.fromHtml("<font color='#808080'>Monthly</font>"));
		super.onCreate(savedInstanceState);
		final View rootview = inflater.inflate(R.layout.fragment_month,container,false);
		//getActivity().getActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + getString(R.string.app_name) + "</font>"));
		String formattedDate = df.format(date);
		dateText = (TextView)rootview.findViewById(R.id.datetext);
		dateText.setText(year + "/" + mon);
		GridView grid = (GridView) rootview.findViewById(R.id.grid1);
		list = (ListView) rootview.findViewById(R.id.scheduleListView);

		mItems = new ArrayList<String>(); //dayList
		adapter = new ArrayAdapter<String>(grid.getContext(),android.R.layout.simple_list_item_1, mItems);

		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (mItems.get(arg2).equals("")) {
					;
				} else {

					Intent intent = new Intent();
					//(getActivity().getApplicationContext(), MonthFragment.class);
					intent.putExtra("Param1", year + "/"
							+ mon + "/" + mItems.get(arg2));
					//startActivity(intent);
					today = intent.getStringExtra("Param1");
					loadDB();
				}

			}
		});

		//Date date = new Date();// ���ÿ� ��¥�� ���� ���ش�.
		/*int year = date.getYear() + 1900;
		int mon = date.getMonth() + 1;
		textYear.setText(year + "");
		textMon.setText(mon + "");*/



		fillDate(year, mon);

		ImageButton ntMonth = (ImageButton)rootview.findViewById(R.id.day_ntBtn);
		ImageButton prMonth = (ImageButton)rootview.findViewById(R.id.day_prBtn);
		ntMonth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mon = mon + 1;
				fillDate(year,mon);
				if(mon>12){
					mon = mon - 12;
					year = year + 1;
				}
				dateText.setText(year + "/" + mon);
			}
		});
		prMonth.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mon = mon - 1;
				fillDate(year,mon);
				if(mon<1){
					mon = mon + 12;
					year = year - 1;
				}
				dateText.setText(year + "/" + mon);
			}
		});

		/*Button btnmove = (Button) rootview.findViewById(R.id.bt1);
		btnmove.setOnClickListener(new AdapterView.OnClickListener() {
			public void onClick(View view) {
				if (mItems.get(arg).getId() == R.id.bt1) {
					int year = Integer.parseInt(textYear.getText().toString());
					int mon = Integer.parseInt(textMon.getText().toString());
					fillDate(year, mon);
				}
			}
		});*/


		return rootview;

	}


	private void fillDate(int year, int mon) {
		mItems.clear();
		mItems.add("S");
		mItems.add("M");
		mItems.add("T");
		mItems.add("W");
		mItems.add("T");
		mItems.add("F");
		mItems.add("S");

		Date current = new Date(year - 1900, mon - 1, 1);
		int day = current.getDay();

		for (int i = 0; i < day; i++) {
			mItems.add("");
		}

		current.setDate(32);
		int last = 32 - current.getDate();

		for (int i = 1; i <= last; i++) {
			mItems.add(i + "");
		}
		adapter.notifyDataSetChanged();

	}
	public void loadDB (){
		helper = new MyDBHelper(getActivity().getApplicationContext(), "Today.db", null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();

		cursor = db.rawQuery(
				"SELECT * FROM today WHERE date = '" + today + "'", null);

		cursorAdapter = new SimpleCursorAdapter(getActivity(),
				android.R.layout.simple_list_item_2, cursor, new String[] {
				"title", "time" }, new int[] { android.R.id.text1,
				android.R.id.text2 });

		list.setAdapter(cursorAdapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				Intent intent = new Intent(getActivity().getApplicationContext(), EditActivity.class);
				cursor.moveToPosition(position);
				intent.putExtra("ParamID", cursor.getInt(0));
				startActivityForResult(intent, 0);
			}
		});

		cursorAdapter.notifyDataSetChanged();
		helper.close();
	}
}
