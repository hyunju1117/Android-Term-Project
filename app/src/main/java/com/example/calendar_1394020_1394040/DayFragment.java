package com.example.calendar_1394020_1394040;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class DayFragment extends Fragment{
    TextView date;
    MyDBHelper helper;
    String today;
    Cursor cursor;
    SimpleCursorAdapter adapter;
    ListView list;
    private static int dayNumber = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootview = inflater.inflate(R.layout.fragment_day, container, false);
        date = (TextView)rootview.findViewById(R.id.dayTitle);

        caculateDay(dayNumber);

        today = date.getText().toString();
        date.setText(today);

        helper = new MyDBHelper(getActivity().getApplicationContext(), "Today.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        cursor = db.rawQuery(
                "SELECT * FROM today WHERE date = '" + today + "'", null);

        adapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_2, cursor, new String[] {
                "title", "time" }, new int[] { android.R.id.text1,
                android.R.id.text2 });

        ListView list = (ListView) rootview.findViewById(R.id.dayListView);
        list.setAdapter(adapter);
        // list.setOnItemClickListener(this);

        helper.close();


        ImageButton prBtn = (ImageButton) rootview.findViewById(R.id.day_prBtn);
        prBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayNumber = dayNumber -1 ;
                caculateDay(dayNumber);
            }
        });
        ImageButton ntBtn = (ImageButton) rootview.findViewById(R.id.day_ntBtn);
        ntBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayNumber = dayNumber +1 ;
                caculateDay(dayNumber);
            }
        });

        return rootview;
    }

    public void caculateDay(int dayNumber){
        Calendar c = Calendar.getInstance();

        c.add(Calendar.DATE, dayNumber);

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/d",Locale.KOREA);
        String formattedDate = df.format(c.getTime());

        date.setText(formattedDate);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
            case 1:
                if (resultCode == RESULT_OK) {
                    // adapter.notifyDataSetChanged();
                    SQLiteDatabase db = helper.getWritableDatabase();
                    cursor = db.rawQuery("SELECT * FROM today WHERE date = '"
                            + today + "'", null);
                    adapter.changeCursor(cursor);
                    helper.close();
                }
                break;
        }
    }
}