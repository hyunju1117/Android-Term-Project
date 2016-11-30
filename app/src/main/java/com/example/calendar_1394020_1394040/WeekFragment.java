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

public class WeekFragment extends Fragment {

    private TextView date;//년월 텍스트뷰
    private TextView weekday1, weekday2, weekday3, weekday4, weekday5, weekday6, weekday7;
    private ArrayList<String> dayList; //일 저장 리스트
    private Calendar mCal;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootview = inflater.inflate(R.layout.fragment_week, container, false);

        date = (TextView)rootview.findViewById(R.id.date_text);
        weekday1=(TextView)rootview.findViewById(R.id.sunDateTextView);
        weekday2=(TextView)rootview.findViewById(R.id.monDateTextView);
        weekday3=(TextView)rootview.findViewById(R.id.tueDateTextView);
        weekday4=(TextView)rootview.findViewById(R.id.wedDateTextView);
        weekday5=(TextView)rootview.findViewById(R.id.thuDateTextView);
        weekday6=(TextView)rootview.findViewById(R.id.friDateTextView);
        weekday7=(TextView)rootview.findViewById(R.id.satDateTextView);

       // 오늘 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        //현재 날짜 텍스트뷰에 넣기
        this.date.setText(curYearFormat.format(date) + "/" + curMonthFormat.format(date));

       //일요일에 날짜 맞춤
        Calendar mCal = Calendar.getInstance();
        mCal.setFirstDayOfWeek(Calendar.SUNDAY);

        int dayOfWeek = mCal.get(Calendar.DAY_OF_WEEK);
        mCal.add(Calendar.DAY_OF_MONTH, (-(dayOfWeek - 1)));

        dayList = new ArrayList();
        for ( int i = 0; i < 7; i++ ) {
            dayList.add(curDayFormat.format(mCal.getTime()));
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        this.weekday1.setText(dayList.get(0));
        this.weekday2.setText(dayList.get(1));
        this.weekday3.setText(dayList.get(2));
        this.weekday4.setText(dayList.get(3));
        this.weekday5.setText(dayList.get(4));
        this.weekday6.setText(dayList.get(5));
        this.weekday7.setText(dayList.get(6));

       ImageButton prBtn = (ImageButton) rootview.findViewById(R.id.week_prBtn);
        prBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Calendar mCal = Calendar.getInstance();
                mCal.setFirstDayOfWeek(Calendar.SUNDAY);

                int dayOfWeek = mCal.get(Calendar.DAY_OF_WEEK);
                mCal.add(Calendar.DAY_OF_MONTH, (-(dayOfWeek - 1 )));

                mCal.add(Calendar.DAY_OF_MONTH,-7);
                dayList = new ArrayList();
                for ( int i = 0; i < 7; i++ ) {
                    dayList.add(curDayFormat.format(mCal.getTime()));
                    mCal.add(Calendar.DAY_OF_MONTH, 1);
                }
                weekday1.setText(dayList.get(0));
                weekday2.setText(dayList.get(1));
                weekday3.setText(dayList.get(2));
                weekday4.setText(dayList.get(3));
                weekday5.setText(dayList.get(4));
                weekday6.setText(dayList.get(5));
                weekday7.setText(dayList.get(6));
            }
        });
        ImageButton ntBtn = (ImageButton) rootview.findViewById(R.id.week_ntBtn);
        ntBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar mCal = Calendar.getInstance();
                mCal.setFirstDayOfWeek(Calendar.SUNDAY);

                int dayOfWeek = mCal.get(Calendar.DAY_OF_WEEK);
                mCal.add(Calendar.DAY_OF_MONTH, (-(dayOfWeek - 1)));

                mCal.add(Calendar.DAY_OF_MONTH,7);
                dayList = new ArrayList();
                for ( int i = 0; i < 7; i++ ) {
                    dayList.add(curDayFormat.format(mCal.getTime()));
                    mCal.add(Calendar.DAY_OF_MONTH, 1);
                }
                weekday1.setText(dayList.get(0));
                weekday2.setText(dayList.get(1));
                weekday3.setText(dayList.get(2));
                weekday4.setText(dayList.get(3));
                weekday5.setText(dayList.get(4));
                weekday6.setText(dayList.get(5));
                weekday7.setText(dayList.get(6));
            }
        });

        return rootview;
    }

}


