package com.example.calendar_1394020_1394040;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class DayFragment extends Fragment{
    TextView date;
    MyDBHelper mDBHelper;
    String today;
    Cursor cursor;
    SimpleCursorAdapter adapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootview = inflater.inflate(R.layout.fragment_day, container, false);
        date = (TextView)rootview.findViewById(R.id.date_text);

        // 오늘 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        final Date date = new Date();

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.KOREA);
        //현재 날짜 텍스트뷰에 넣기

       this.date.setText(sdf.format(date));


    ImageButton prBtn = (ImageButton) rootview.findViewById(R.id.day_prBtn);
        prBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView date = (TextView)rootview.findViewById(R.id.date_text);
                Calendar pm = Calendar.getInstance();
                pm.add(Calendar.DATE,-1);
                int pYear   = pm.get(Calendar.YEAR);
                int pMonth  = pm.get(Calendar.MONTH) + 1;
                int pCurDay = pm.get(Calendar.DATE);
                date.setText(pYear+"/"+pMonth+"/"+pCurDay);
            }
        });
        ImageButton ntBtn = (ImageButton) rootview.findViewById(R.id.day_ntBtn);
        ntBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                TextView date = (TextView)rootview.findViewById(R.id.date_text);
                Calendar nm = Calendar.getInstance();
                nm.add(Calendar.DATE,1);
                int nYear   = nm.get(Calendar.YEAR);
                int nMonth  = nm.get(Calendar.MONTH) + 1;
                int nCurDay = nm.get(Calendar.DATE);
                date.setText(nYear+"/"+nMonth+"/"+nCurDay);
            }
        });

        return rootview;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getActivity(), EditActivity.class);
        cursor.moveToPosition(position);
        intent.putExtra("ParamID", cursor.getInt(0));
        startActivityForResult(intent, 0);
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(getActivity(), EditActivity.class);
        intent.putExtra("ParamDate", today);
        startActivityForResult(intent, 1);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
            case 1:
                if (resultCode == RESULT_OK) {
                    // adapter.notifyDataSetChanged();
                    SQLiteDatabase db = mDBHelper.getWritableDatabase();
                    cursor = db.rawQuery("SELECT * FROM today WHERE date = '"
                            + today + "'", null);
                    adapter.changeCursor(cursor);
                    mDBHelper.close();
                }
                break;
        }
    }

    public String getDate ( int iDay )
    {
        Calendar temp = Calendar.getInstance ( );
        StringBuffer sbDate=new StringBuffer ( );

        temp.add ( Calendar.DAY_OF_MONTH, iDay );

        int nYear = temp.get ( Calendar.YEAR );
        int nMonth = temp.get ( Calendar.MONTH ) + 1;
        int nDay = temp.get ( Calendar.DAY_OF_MONTH );

        sbDate.append ( nYear );
        if ( nMonth < 10 )
            sbDate.append ( "0" );
        sbDate.append ( nMonth );
        if ( nDay < 10 )
            sbDate.append ( "0" );
        sbDate.append ( nDay );


        return sbDate.toString ( );
    }

}