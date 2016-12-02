package com.example.calendar_1394020_1394040;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeekFragment extends Fragment{

    private TextView weekday1, weekday2, weekday3, weekday4, weekday5, weekday6, weekday7,dateTextView;
    private ArrayList<String> dayList;
    private ArrayList<String> todayList;
    private static int weekNumber = 0;
    Calendar c;
    MyDBHelper helper;
    String today;
    Cursor cursor;
    SimpleCursorAdapter adapter;
    ListView list;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setTitle(Html.fromHtml("<font color='#808080'>Weekly</font>"));
        super.onCreate(savedInstanceState);
        final View rootview = inflater.inflate(R.layout.fragment_week, container, false);

        weekday1=(TextView)rootview.findViewById(R.id.sunDateTextView);
        weekday2=(TextView)rootview.findViewById(R.id.monDateTextView);
        weekday3=(TextView)rootview.findViewById(R.id.tueDateTextView);
        weekday4=(TextView)rootview.findViewById(R.id.wedDateTextView);
        weekday5=(TextView)rootview.findViewById(R.id.thuDateTextView);
        weekday6=(TextView)rootview.findViewById(R.id.friDateTextView);
        weekday7=(TextView)rootview.findViewById(R.id.satDateTextView);
        dateTextView=(TextView)rootview.findViewById(R.id.dateTextView);
        list = (ListView) rootview.findViewById(R.id.weekListView);

        dayList = new ArrayList<String>();
        todayList = new ArrayList<String>();

        calculateWeek(weekNumber);

        weekday1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTextView.setText(todayList.get(0));
                today = todayList.get(0);
                loadDB();
            }
        });
        weekday2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                today = todayList.get(1);
                dateTextView.setText(today);
                loadDB();
            }
        });
        weekday3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                today = todayList.get(2);
                dateTextView.setText(today);
                loadDB();
            }
        });
        weekday4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                today = todayList.get(3);
                dateTextView.setText(today);
                loadDB();
            }
        });
        weekday5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                today = todayList.get(4);
                dateTextView.setText(today);
                loadDB();
            }
        });
        weekday6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                today = todayList.get(5);
                dateTextView.setText(today);
                loadDB();
            }
        });
        weekday7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                today = todayList.get(6);
                dateTextView.setText(today);
                loadDB();
            }
        });

       ImageButton prBtn = (ImageButton) rootview.findViewById(R.id.week_prBtn);
        prBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekNumber = weekNumber - 1;
                calculateWeek(weekNumber);
                dateTextView.setText("");
                loadDB();
            }
        });
        ImageButton ntBtn = (ImageButton) rootview.findViewById(R.id.week_ntBtn);
        ntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekNumber = weekNumber +1;
                calculateWeek(weekNumber);
                dateTextView.setText("");
                loadDB();
            }
        });

        return rootview;
    }
    public void loadDB (){
        helper = new MyDBHelper(getActivity().getApplicationContext(), "Today.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        cursor = db.rawQuery(
                "SELECT * FROM today WHERE date = '" + today + "'", null);

        adapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, cursor, new String[] {
                "title", "time" }, new int[] { android.R.id.text1,
                android.R.id.text2 });

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent = new Intent(getActivity().getApplicationContext(), DetailActivity.class);
                cursor.moveToPosition(position);
                intent.putExtra("ParamID", cursor.getInt(0));
                startActivityForResult(intent, 0);
            }
        });

        adapter.notifyDataSetChanged();
        helper.close();
    }
    private void calculateWeek(int weekFromToday) {
        Calendar c = Calendar.getInstance();
        dayList.clear();
        todayList.clear();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        c.set(Calendar.WEEK_OF_YEAR,
                c.get(Calendar.WEEK_OF_YEAR)+weekFromToday);

        SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dfMonth = new SimpleDateFormat("M");
        SimpleDateFormat dfDay = new SimpleDateFormat("d");

        for (int i = 0; i < 7; i++) {
            dayList.add(dfDay.format(c.getTime()));
            todayList.add(dfYear.format(c.getTime())+"/"+dfMonth.format(c.getTime())+"/"+dfDay.format(c.getTime()));
            c.add(Calendar.DATE, 1);
        }
        weekday1.setText(dayList.get(0));
        weekday2.setText(dayList.get(1));
        weekday3.setText(dayList.get(2));
        weekday4.setText(dayList.get(3));
        weekday5.setText(dayList.get(4));
        weekday6.setText(dayList.get(5));
        weekday7.setText(dayList.get(6));

    }
}


