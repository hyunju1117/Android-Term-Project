package com.example.calendar_1394020_1394040;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity{
    MyDBHelper helper;
    private ListView list,plist,vlist;

    SimpleCursorAdapter mAdapter,pAdapter,vAdapter;
    //MediaItemAdapter mAdapter;
    private MediaPlayer mMediaPlayer;
    private int mSelectePoistion;
    private MediaRecorder mMediaRecorder;
    private File mPhotoFile =null;
    private String mPhotoFileName = null;
    private int mPlaybackPosition = 0;   // media play 위치
    int mId;
    int position;
    String today,title, date;
    Cursor cursor;
    TextView textTitle,textDate,textTime,textLocation,textMemo,textendTime;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        list = (ListView)findViewById(R.id.multi_list);
        vlist = (ListView)findViewById(R.id.vid_list);
        plist =(ListView)findViewById(R.id.pic_list);

        if (savedInstanceState != null) // 액티비티가 재시작되는 경우, 기존에 저장한 상태 복구
            mPhotoFileName = savedInstanceState.getString("mPhotoFileName");

        textTitle= (TextView)findViewById(R.id.detailTitle);
        textDate= (TextView)findViewById(R.id.detailDate);
        textTime= (TextView)findViewById(R.id.detailTime);
        textendTime = (TextView)findViewById(R.id.detailendTime);
        textLocation= (TextView)findViewById(R.id.detailLocation);
        textMemo= (TextView)findViewById(R.id.detailMemo_text);
        currentDateFormat();

        Intent intent = getIntent();
        mId = intent.getIntExtra("ParamID", -1);
        today = intent.getStringExtra("ParamDate");


        helper = new MyDBHelper(this, "todaydb.db", null, 1);

        if (mId == -1) {
            textDate.setText(today);
        } else {
            SQLiteDatabase db = helper.getWritableDatabase();
            cursor = db.rawQuery("SELECT * FROM todaydb WHERE _id='" + mId
                    + "'", null);

            if (cursor.moveToNext()) {
                textTitle.setText(cursor.getString(1));
                textDate.setText(cursor.getString(2));
                textTime.setText(cursor.getString(3));
                textendTime.setText(cursor.getString(4));
                textLocation.setText(cursor.getString(5));
                textMemo.setText(cursor.getString(6));
            }

            helper.close();
        }

        loadDB();
        loadVD();
        loadPIC();



        final Button deleteBtn = (Button) findViewById(R.id.deleteBtn) ;
        Button editBtn = (Button) findViewById(R.id.editBtn) ;

        deleteBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view){
                deleteDialog();
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
               editSchedule();
            }
        });

    }
    private void loadDB () {
        helper = new MyDBHelper(this, "todaydb.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        title = textTitle.getText().toString();
        date = textDate.getText().toString();
        cursor = db.rawQuery(
                "SELECT * FROM todaydb WHERE voice = 'VOICE"+ date + title +".mp4'", null);


        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor, new String[] {
                "voice" }, new int[] { android.R.id.text1
                 });

        list.setAdapter(mAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (mMediaPlayer != null && mSelectePoistion == position) {
                    if (mMediaPlayer.isPlaying()) { // 현재 재생 중인 미디어를 선택한 경우
                        mPlaybackPosition = mMediaPlayer.getCurrentPosition();
                        mMediaPlayer.pause();
                        Toast.makeText(getApplicationContext(), "음악 파일 재생 중지됨.", Toast.LENGTH_SHORT).show();
                    } else {
                        mMediaPlayer.start();
                        mMediaPlayer.seekTo(mPlaybackPosition);
                        Toast.makeText(getApplicationContext(), "음악 파일 재생 재시작됨.", Toast.LENGTH_SHORT).show();
                    }
                } else {     // 현재 재생중인 미디어가 없거나, 다른 미디어를 선택한 경우
                    String uriString = "file://" + Environment.getExternalStorageDirectory().getPath() +"/Music/VOICE" + date + title +".mp4";
                    Uri uri = Uri.parse(uriString);
                    try {
                        playAudio(uri);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                mAdapter.notifyDataSetChanged();
                helper.close();
            }

        });
    }

    public void loadVD(){
        helper = new MyDBHelper(this, "todaydb.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        title = textTitle.getText().toString();
        date = textDate.getText().toString();

        cursor = db.rawQuery(
                "SELECT * FROM todaydb WHERE video = 'VIDEO"+ date + title +".mp4'", null);


        vAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor, new String[] {
                "video" }, new int[] { android.R.id.text1
        });

        vlist.setAdapter(vAdapter);

        vlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(DetailActivity.this, VideoActivity.class);
                intent.putExtra("video_uri", "file://" + Environment.getExternalStorageDirectory().getPath() +"/Movies/VIDEO" + date + title +".mp4");
                startActivity(intent);
                vAdapter.notifyDataSetChanged();
                helper.close();
            }
        });
    }

    public void loadPIC(){
        helper = new MyDBHelper(this, "todaydb.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        title = textTitle.getText().toString();
        date = textDate.getText().toString();

        cursor = db.rawQuery(
                "SELECT * FROM todaydb WHERE photo = 'IMG"+ date + title +".jpg'", null);


        pAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor, new String[] {
                "photo" }, new int[] { android.R.id.text1
        });

        plist.setAdapter(pAdapter);

        plist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(DetailActivity.this, VideoActivity.class);
                intent.putExtra("image_uri","file://" + Environment.getExternalStorageDirectory().getPath() +"/Pictures/IMG" + date + title +".jpg" );
                startActivity(intent);
                pAdapter.notifyDataSetChanged();
                helper.close();
            }
        });
    }


    public void  currentDateFormat(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d", Locale.KOREA);
        String formattedDate = df.format(c.getTime());
    }

    private void playAudio(Uri uri) throws Exception {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(getApplicationContext(), uri);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
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
    public void editSchedule(){
        SQLiteDatabase db = helper.getWritableDatabase();
        Intent intent=new Intent(DetailActivity.this,EditActivity.class);
        intent.putExtra("ParamID",cursor.getInt(0));
        startActivityForResult(intent,0);
    }

    public void deleteSchedule(){
        SQLiteDatabase db = helper.getWritableDatabase();
        if (mId != -1) {
        db.execSQL("DELETE FROM todaydb WHERE _id='" + mId + "';");
        helper.close();
    }
        setResult(RESULT_OK);
    }

}
