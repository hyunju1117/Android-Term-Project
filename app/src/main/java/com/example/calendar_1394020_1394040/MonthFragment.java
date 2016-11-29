package com.example.calendar_1394020_1394040;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class MonthFragment extends Fragment {
	ArrayList<String> mItems;
	ArrayAdapter<String> adapter;
	TextView textYear;
	TextView textMon;

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View rootview = inflater.inflate(R.layout.fragment_month,container,false);

		textYear = (TextView) rootview.findViewById(R.id.edit1);
		textMon = (TextView) rootview.findViewById(R.id.edit2);
		GridView grid = (GridView) rootview.findViewById(R.id.grid1);

		mItems = new ArrayList<String>(); //dayList
		adapter = new ArrayAdapter<String>(grid.getContext(),android.R.layout.simple_list_item_1, mItems);

		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (mItems.get(arg2).equals("")) {
					;
				} else {
					Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
					intent.putExtra("Param1", textYear.getText().toString() + "/"
							+ textMon.getText().toString() + "/" + mItems.get(arg2));
					startActivity(intent);
				}
			}
		});

		Date date = new Date();// ���ÿ� ��¥�� ���� ���ش�.
		int year = date.getYear() + 1900;
		int mon = date.getMonth() + 1;
		textYear.setText(year + "");
		textMon.setText(mon + "");

		fillDate(year, mon);

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

		current.setDate(32);// 32�ϱ��� �Է��ϸ� 1�Ϸ� �ٲ��ش�.
		int last = 32 - current.getDate();

		for (int i = 1; i <= last; i++) {
			mItems.add(i + "");
		}
		adapter.notifyDataSetChanged();

	}
}
