package com.example.calendar_1394020_1394040;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MonthFragment extends Fragment {

   // private ActionBar actionbar;
    private TextView date;//년월 텍스트뷰
    private GridAdapter gridAdapter; //그리드뷰어뎁터
    private ArrayList<String> dayList; //일 저장 리스트
    private GridView gridView; //그리드뷰
    private Calendar mCal; //캘린더변수
    private SimpleDateFormat FormatMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // actionbar.setTitle(FormatMonth.format(new Date(calendar.getDate())));
        super.onCreate(savedInstanceState);
        final View rootview = inflater.inflate(R.layout.fragment_month, container, false);
        date = (TextView)rootview.findViewById(R.id.date_text);
        gridView = (GridView) rootview.findViewById(R.id.gridview);


        // 오늘 날짜를 세팅 해준다.
        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        //연,월,일을 따로 저장
        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        //현재 날짜 텍스트뷰에 넣기
        this.date.setText(curYearFormat.format(date) + "/" + curMonthFormat.format(date));

        //gridview 요일 표시
        dayList = new ArrayList<String>();
        dayList.add("S");
        dayList.add("M");
        dayList.add("T");
        dayList.add("W");
        dayList.add("T");
        dayList.add("F");
        dayList.add("S");

        mCal = Calendar.getInstance();

        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)
        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);
        final int dayNum = mCal.get(Calendar.DAY_OF_WEEK);

        //요일 매칭 공백 add
        for (int i = 1; i < dayNum; i++) {dayList.add("");}
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);

        gridAdapter = new GridAdapter(getActivity().getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(dayList.get(position).equals("")) {
                    ;
                } else{
                    Intent intent = new Intent(MonthFragment.this.getActivity(),EditActivity.class);
                    startActivity(intent);
                }
                //요일까지 position 에 들어가므로 -7, 요일매칭 공백 빼주고 +1
                Toast.makeText(getActivity().getApplicationContext(), "position: " + (position-7-dayNum+1), Toast.LENGTH_SHORT).show();
            }
        });

    return rootview;

    }




     // 해당 월에 표시할 일 수 구함
    private void setCalendarDate(int month) {
        mCal.set(Calendar.MONTH, month - 1);
        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {dayList.add("" + (i + 1));}
    }

//그리드뷰 어댑터
    private class GridAdapter extends BaseAdapter {

        private final List<String> list;
        private final LayoutInflater inflater;

        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calendar_item, parent, false);
                holder = new ViewHolder();

                holder.item_text = (TextView)convertView.findViewById(R.id.item_text);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            holder.item_text.setText("" + getItem(position));

            //해당 날짜 텍스트 컬러,배경 변경
            mCal = Calendar.getInstance();

            //오늘 day 가져옴
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            String sToday = String.valueOf(today);
            Drawable background = getResources().getDrawable(R.drawable.circle);
            if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
                holder.item_text.setTextColor(getResources().getColor(R.color.colorPrimary));
                //빨간줄 상관 없음
                holder.item_text.setBackground(background);
            }
            return convertView;
        }
    }

    private class ViewHolder {
        TextView item_text;
    }

}


