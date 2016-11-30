package com.example.calendar_1394020_1394040;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
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

    private TextView weekday1, weekday2, weekday3, weekday4, weekday5, weekday6, weekday7;
    private ArrayList<String> dayList; //일 저장 리스트
    private static int weekNumber = -1;
    Calendar c;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootview = inflater.inflate(R.layout.fragment_week, container, false);

        weekday1=(TextView)rootview.findViewById(R.id.sunDateTextView);
        weekday2=(TextView)rootview.findViewById(R.id.monDateTextView);
        weekday3=(TextView)rootview.findViewById(R.id.tueDateTextView);
        weekday4=(TextView)rootview.findViewById(R.id.wedDateTextView);
        weekday5=(TextView)rootview.findViewById(R.id.thuDateTextView);
        weekday6=(TextView)rootview.findViewById(R.id.friDateTextView);
        weekday7=(TextView)rootview.findViewById(R.id.satDateTextView);

        dayList = new ArrayList<String>();

        calculateWeek(weekNumber);

       ImageButton prBtn = (ImageButton) rootview.findViewById(R.id.week_prBtn);
        prBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekNumber = weekNumber - 1;
                calculateWeek(weekNumber);
            }
        });
        ImageButton ntBtn = (ImageButton) rootview.findViewById(R.id.week_ntBtn);
        ntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekNumber = weekNumber +1;
                calculateWeek(weekNumber);
            }
        });

        return rootview;
    }
    private void calculateWeek(int weekFromToday) {
        Calendar c = Calendar.getInstance();
        dayList.clear();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        c.set(Calendar.WEEK_OF_YEAR,
                c.get(Calendar.WEEK_OF_YEAR)+weekFromToday);

        SimpleDateFormat df = new SimpleDateFormat("dd");
        for (int i = 0; i < 7; i++) {
            dayList.add(df.format(c.getTime()));
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


