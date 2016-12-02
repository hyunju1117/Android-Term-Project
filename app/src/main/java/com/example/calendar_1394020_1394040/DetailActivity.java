package com.example.calendar_1394020_1394040;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity{
    MyDBHelper mDBHelper;
    int mId;
    String today;
    TextView textTitle,textDate,textTime,textLocation,textMemo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textTitle= (TextView)findViewById(R.id.detailTitle);
        textDate= (TextView)findViewById(R.id.detailDate);
        textTime= (TextView)findViewById(R.id.detailTime);
        textLocation= (TextView)findViewById(R.id.detailLocation);
        textMemo= (TextView)findViewById(R.id.detailMemo_text);

        Intent intent = getIntent();
        mId = intent.getIntExtra("ParamID", -1);
        today = intent.getStringExtra("ParamDate");

        mDBHelper = new MyDBHelper(this, "Today.db", null, 1);

        if (mId == -1) {
            textDate.setText(today);
        } else {
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM today WHERE _id='" + mId
                    + "'", null);

            if (cursor.moveToNext()) {
                textTitle.setText(cursor.getString(1));
                textDate.setText(cursor.getString(2));
                textTime.setText(cursor.getString(3));
                textLocation.setText(cursor.getString(4));
                textMemo.setText(cursor.getString(5));
            }
            mDBHelper.close();
        }

        final Button deleteBtn = (Button) findViewById(R.id.deleteBtn) ;
        Button editBtn = (Button) findViewById(R.id.editBtn) ;

        deleteBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view){
                deleteDialog();
            }
        });
        editBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view){
                Intent intent=new Intent(DetailActivity.this,EditActivity.class);
                startActivity(intent);
            }
        });

    }

    protected Dialog deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);     // 여기서 this는 Activity의 this

// 알림창의 속성 설정
        builder.setMessage("일정을 삭제 하시겠습니까?")        // 메세지 설정
                .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){
                        deleteSchedule();
                        finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    // 취소 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
        return dialog;
    }

    public void deleteSchedule(){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (mId != -1) {
        db.execSQL("DELETE FROM today WHERE _id='" + mId + "';");
        mDBHelper.close();
    }
        setResult(RESULT_OK);
    }

}
