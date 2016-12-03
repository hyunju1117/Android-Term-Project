package com.example.calendar_1394020_1394040;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentTransaction;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.Locale;

public class EditActivity extends Activity implements OnClickListener {
    MyDBHelper helper;
    int mId;
    String today,currentTimeStamp;
    public EditText editDate, editTitle, editTime, editlocate, editMemo;
    TextView tttt;
    private MediaItemAdapter mAdapter;
    private MediaPlayer mMediaPlayer;
    private MediaRecorder mMediaRecorder;
    private String recFileN ;
    private File mPhotoFile =null;
    private String mPhotoFileName = null;
    private File mVideoFile =null;
    private String mVideoFileName = null;
    private int mPlaybackPosition = 0;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if (savedInstanceState != null) // 액티비티가 재시작되는 경우, 기존에 저장한 상태 복구
            mPhotoFileName = savedInstanceState.getString("mPhotoFileName");

        editDate = (EditText) findViewById(R.id.editdate);
        editTitle = (EditText) findViewById(R.id.edittitle);
        editTime = (EditText) findViewById(R.id.edittime);
        editlocate = (EditText) findViewById(R.id.editlocate);
        editMemo = (EditText) findViewById(R.id.editmemo);
        tttt = (TextView)findViewById(R.id.tttt);

        Intent intent = getIntent();
        mId = intent.getIntExtra("ParamID", -1);
        today = intent.getStringExtra("ParamDate");
        tttt.setText(recFileN);

        helper = new MyDBHelper(this, "todaydb.db", null, 1);
        if (mId == -1) {
            editDate.setText(today);
        } else {
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM todaydb WHERE _id='" + mId
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
        title = editTitle.getText().toString();

        checkDangerousPermissions();

        Button btn1 = (Button) findViewById(R.id.btnsave);
        btn1.setOnClickListener(this);
        Button btn2 = (Button) findViewById(R.id.btndel);
        btn2.setOnClickListener(this);
        Button btn3 = (Button) findViewById(R.id.btncancel);
        btn3.setOnClickListener(this);

        final Button voiceRecBtn = (Button) findViewById(R.id.voiceRecBtn);
        Button videoRecBtn = (Button) findViewById(R.id.videoRecBtn);
        Button imageCaptureBtn = (Button) findViewById(R.id.imageCaptureBtn);

        voiceRecBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (mMediaRecorder == null) {
                    startAudioRec();
                    voiceRecBtn.setText("녹음중지");
                } else {
                    stopAudioRec();
                    voiceRecBtn.setText("음성녹음");
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

        if (mId == -1) {
            btn2.setVisibility(View.INVISIBLE);

        }

    }

    private void checkDangerousPermissions() {
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
    private void startAudioRec()  {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        recFileN = "VOICE" + editDate.getText().toString() + editTitle.getText().toString()+".mp4";
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
        Toast.makeText(getApplicationContext(), "녹음이 중지되었습니다.", Toast.LENGTH_SHORT).show();


    }

    public String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d", Locale.KOREA);
        currentTimeStamp = dateFormat.format(new Date());
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
            mPhotoFileName = "IMG"+editDate.getText().toString() + editTitle.getText().toString()+".jpg";
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
            mVideoFileName = "VIDEO"+editDate.getText().toString() + editTitle.getText().toString()+".mp4";
            mVideoFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),mVideoFileName);

            if (mVideoFile !=null) {
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mVideoFile));
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            } else
                ;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (mPhotoFileName != null) {
                mPhotoFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), mPhotoFileName);
                //mAdapter.addItem(new MediaItem(MediaItem.SDCARD, mPhotoFileName, Uri.fromFile(mPhotoFile), MediaItem.IMAGE));
            } else
                Toast.makeText(getApplicationContext(), "mPhotoFile is null", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri sourceUri = null;
            if (data != null)
                sourceUri = data.getData();
            if (sourceUri != null) {
                mVideoFileName = "VIDEO"+editDate.getText().toString() + editTitle.getText().toString()+".mp4";
                File destination = new File(Environment.getExternalStorageDirectory().getPath()+"/Movies/"+mVideoFileName);
                saveFile(sourceUri, destination);
                //mAdapter.addItem(new MediaItem(MediaItem.SDCARD, recFileN, Uri.fromFile(destination) ,MediaItem.VIDEO));
            } else
                Toast.makeText(getApplicationContext(), "savw", Toast.LENGTH_LONG).show();
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


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = helper.getWritableDatabase();

        switch (v.getId()) {
            case R.id.btnsave:
                if (mId != -1) {
                    db.execSQL("UPDATE todaydb SET title='" + editTitle.getText().toString()
                            + "',date='" + editDate.getText().toString()
                            + "', time='" + editTime.getText().toString()
                            + "', locate='" + editlocate.getText().toString()
                            + "', memo='" + editMemo.getText().toString()
                            + "', photo='" + mPhotoFileName
                            + "', video='" + mVideoFileName
                            + "', voice='" + recFileN
                            + "' WHERE _id='" + mId
                            + "';");
                } else {
                    try {
                        db.execSQL("INSERT INTO todaydb VALUES(null, '"
                                + editTitle.getText().toString() + "', '"
                                + editDate.getText().toString() + "', '"
                                + editTime.getText().toString() + "', '"
                                + editlocate.getText().toString() + "', '"
                                + editMemo.getText().toString() + "', '"
                                + mPhotoFileName + "', '"
                                + mVideoFileName + "', '"
                                + recFileN + "');");
                    } catch (SQLException e) {
                        Log.e("SQLite", "Error in updating recodes");
                    }
                }
                helper.close();
                setResult(RESULT_OK);
                break;
            case R.id.btndel:
                if (mId != -1) {
                    db.execSQL("DELETE FROM todaydb WHERE _id='" + mId + "';");
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

    protected void onStop() {
        super.onStop();
        killMediaPlayer();
    }

    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }



}
