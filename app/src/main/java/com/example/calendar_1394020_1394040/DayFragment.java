package com.example.calendar_1394020_1394040;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DayFragment extends Fragment{
    TextView date;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootview = inflater.inflate(R.layout.fragment_day, container, false);
        date = (TextView)rootview.findViewById(R.id.weekTitle);

        final Calendar c = Calendar.getInstance();
        final SimpleDateFormat df = new SimpleDateFormat("yyyy / MM / dd",Locale.KOREA);
        String formattedDate = df.format(c.getTime());

        this.date.setText(formattedDate);

    ImageButton prBtn = (ImageButton) rootview.findViewById(R.id.day_prBtn);
        prBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.DATE, -1);
                String formattedDate = df.format(c.getTime());

                Log.v("PREVIOUS DATE : ", formattedDate);
                date.setText(formattedDate);
            }
        });
        ImageButton ntBtn = (ImageButton) rootview.findViewById(R.id.day_ntBtn);
        ntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.DATE, 1);
                String formattedDate = df.format(c.getTime());

                Log.v("NEXT DATE : ", formattedDate);
                date.setText(formattedDate);
            }
        });

        return rootview;
    }
}