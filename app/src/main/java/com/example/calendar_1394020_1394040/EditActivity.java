package com.example.calendar_1394020_1394040;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditActivity extends Activity implements OnClickListener {
    MyDBHelper helper;
    int mId;
    String today;
    EditText editDate, editTitle, editTime, editlocate, editMemo;
    private ListView mListView;
    private int mSelectePoistion;
    private MediaItemAdapter mAdapter;
    private MediaPlayer mMediaPlayer;
    private MediaRecorder mMediaRecorder;
    private String recFileN = null;
    private File mPhotoFile =null;
    private String mPhotoFileName = null;

    private int mPlaybackPosition = 0;   // media play 위치

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editDate = (EditText) findViewById(R.id.editdate);
        editTitle = (EditText) findViewById(R.id.edittitle);
        editTime = (EditText) findViewById(R.id.edittime);
        editlocate = (EditText) findViewById(R.id.editlocate);
        editMemo = (EditText) findViewById(R.id.editmemo);

        Intent intent = getIntent();
        mId = intent.getIntExtra("ParamID", -1);
        today = intent.getStringExtra("ParamDate");

        helper = new MyDBHelper(this, "schedule.db", null, 1);

        if (mId == -1) {
            editDate.setText(today);
        } else {
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM today WHERE _id='" + mId
                    + "'", null);

            if (cursor.moveToNext()) {
                editTitle.setText(cursor.getString(1));
                editDate.setText(cursor.getString(2));
                editTime.setText(cursor.getString(3));
                editlocate.setText(cursor.getString(4));
                editMemo.setText(cursor.getString(5));
            }
            helper.close();
        }

        Button btn1 = (Button) findViewById(R.id.btnsave);
        btn1.setOnClickListener(this);
        Button btn2 = (Button) findViewById(R.id.btndel);
        btn2.setOnClickListener(this);
        Button btn3 = (Button) findViewById(R.id.btncancel);
        btn3.setOnClickListener(this);

        if (mId == -1) {
            btn2.setVisibility(View.INVISIBLE);

        }

        if (savedInstanceState != null) // 액티비티가 재시작되는 경우, 기존에 저장한 상태 복구
            mPhotoFileName = savedInstanceState.getString("mPhotoFileName");

        final Button voiceRecBtn = (Button) findViewById(R.id.voiceRecBtn);
        Button videoRecBtn = (Button) findViewById(R.id.videoRecBtn);
        Button imageCaptureBtn = (Button) findViewById(R.id.imageCaptureBtn);

        checkDangerousPermissions();
        initListView();

        voiceRecBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mMediaRecorder == null) {
                    startAudioRec();
                    voiceRecBtn.setText("녹음중지");
                } else {
                    stopAudioRec();
                    voiceRecBtn.setText("음석녹음");
                }
            }
        });

        videoRecBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dispatchTakeVideoIntent();
            }
        });

        imageCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void checkDangerousPermissions() { //퍼미션체크
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    private void initListView() {
        mListView = (ListView) findViewById(R.id.multi_list);
        ArrayList<MediaItem> mediaList = prepareDataSource();
        mAdapter = new MediaItemAdapter(this, R.layout.item, mediaList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
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
                    switch (((MediaItem) mAdapter.getItem(position)).type) {
                        case MediaItem.AUDIO:
                            try {
                                playAudio(((MediaItem) mAdapter.getItem(position)).uri);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), "음악 파일 재생 시작됨.", Toast.LENGTH_SHORT).show();
                            break;
                        case MediaItem.VIDEO:
                            Intent intent = new Intent(EditActivity.this, VideoActivity.class);
                            intent.putExtra("video_uri", ((MediaItem) mAdapter.getItem(position)).uri.toString());
                            startActivity(intent);
                            break;
                        case MediaItem.IMAGE:
                            Intent intent2 = new Intent(EditActivity.this, ImageActivity.class);
                            intent2.putExtra("image_uri", ((MediaItem) mAdapter.getItem(position)).uri.toString());
                            startActivity(intent2);
                            break;
                    }
                    mSelectePoistion = position;
                }
            }


        });
    }

    private ArrayList<MediaItem> prepareDataSource() {

        ArrayList mediaList = new ArrayList<MediaItem>();

        // sdcard/Pictures 데이터 추가
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() + "/Pictures/" + f.getName());
                if (f.getName().contains(".jpg")) {
                    MediaItem item = new MediaItem(MediaItem.SDCARD, f.getName(), uri, MediaItem.IMAGE);
                    mediaList.add(item);
                }
            }
        }

        // sdcard/Music 데이터 추가
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                Log.i("TAG", "File name=" + f.getName());
                Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() + "/Music/" + f.getName());
                MediaItem item = new MediaItem(MediaItem.SDCARD, f.getName(), uri);
                mediaList.add(item);
            }
        }

        // sdcard/Movies 데이터 추가
        file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                Log.i("TAG", "File name=" + f.getName());
                Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() + "/Movies/" + f.getName());
                MediaItem item = new MediaItem(MediaItem.SDCARD, f.getName(), uri, MediaItem.VIDEO);
                mediaList.add(item);
            }
        }
        return mediaList;
    }

    private void playAudio(Uri uri) throws Exception {
        killMediaPlayer();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(getApplicationContext(), uri);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }


    private void startAudioRec()  {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        recFileN = "VOICE" + currentDateFormat() + ".mp4";
        mMediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getPath() + "/Music/" + recFileN);

        try {
            mMediaRecorder.prepare();
            Toast.makeText(getApplicationContext(), "녹음을 시작하세요.", Toast.LENGTH_SHORT).show();
            mMediaRecorder.start();
        } catch (Exception ex) {
            Log.e("SampleAudioRecorder", "Exception : ", ex);
        }
    }

    private void stopAudioRec()  {

        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;

        Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory().getPath() + "/Music/"+ recFileN);
        mAdapter.addItem(new MediaItem(MediaItem.SDCARD, recFileN,uri));
        Toast.makeText(getApplicationContext(), "녹음이 중지되었습니다.", Toast.LENGTH_SHORT).show();

    }

    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    private void killMediaPlayer() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            mPhotoFileName = "IMG"+currentDateFormat()+".jpg";
            mPhotoFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),mPhotoFileName);

            if (mPhotoFile !=null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else
                Toast.makeText(getApplicationContext(), "file null", Toast.LENGTH_SHORT).show();
        }
    }

    static final int REQUEST_VIDEO_CAPTURE = 2;

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (mPhotoFileName != null) {
                mPhotoFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), mPhotoFileName);
                mAdapter.addItem(new MediaItem(MediaItem.SDCARD, mPhotoFileName, Uri.fromFile(mPhotoFile), MediaItem.IMAGE));
            } else
                Toast.makeText(getApplicationContext(), "mPhotoFile is null", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri sourceUri = null;
            if (data != null)
                sourceUri = data.getData();
            if (sourceUri != null) {
                recFileN = "VIDEO"+currentDateFormat()+".mp4";
                File destination = new File(Environment.getExternalStorageDirectory().getPath()+"/Movies/"+recFileN);
                saveFile(sourceUri, destination);
                mAdapter.addItem(new MediaItem(MediaItem.SDCARD, recFileN, Uri.fromFile(destination) ,MediaItem.VIDEO));
            } else
                Toast.makeText(getApplicationContext(), "!!! null video.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("mPhotoFileName",mPhotoFileName);
        super.onSaveInstanceState(outState);
    }

    private void saveFile(Uri sourceUri, File destination) {
        try {
            InputStream in = getContentResolver().openInputStream(sourceUri);
            OutputStream out = new FileOutputStream(destination);

            BufferedInputStream bis = new BufferedInputStream(in);
            BufferedOutputStream bos = new BufferedOutputStream(out);
            byte[] buf = new byte[1024];
            bis.read(buf);

            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);

            if (bis != null) bis.close();
            if (bos != null) bos.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected void onStop() {
        super.onStop();
        killMediaPlayer();
    }

    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = helper.getWritableDatabase();

        switch (v.getId()) {
            case R.id.btnsave:
                if (mId != -1) {
                    db.execSQL("UPDATE schedule SET title='"
                            + editTitle.getText().toString() + "',date='"
                            + editDate.getText().toString() + "', time='"
                            + editTime.getText().toString() + "', locate='"
                            + editlocate.getText().toString() + "', memo='"
                            + editMemo.getText().toString() + "' WHERE _id='" + mId
                            + "';");
                }
                else {
                    try {
                        db.execSQL("INSERT INTO schedule VALUES(null, '"
                                + editTitle.getText().toString() + "', '"
                                + editDate.getText().toString() + "', '"
                                + editTime.getText().toString() + "', '"
                                + editlocate.getText().toString() + "', '"
                                + editMemo.getText().toString() + "');");
                    }catch (SQLException e) {
                        Log.e("SQLite", "Error in updating recodes");
                    }
                }
                helper.close();
                setResult(RESULT_OK);
                break;
            case R.id.btndel:
                if (mId != -1) {
                    db.execSQL("DELETE FROM schedule WHERE _id='" + mId + "';");
                    helper.close();
                }
                setResult(RESULT_OK);
                break;
            case R.id.btncancel:
                setResult(RESULT_CANCELED);
                break;
        }
        finish();
    }
}
