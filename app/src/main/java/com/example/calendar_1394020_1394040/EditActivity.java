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
    FragmentTransaction transaction = getFragmentManager().beginTransaction();


    /**
     * Called when the activity is first created.
     */
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
        Button multibtn = (Button) findViewById(R.id.multibtn);
       multibtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(EditActivity.this,MemoActivity.class);
                startActivity(intent);
            }
        });

        if (mId == -1) {
            btn2.setVisibility(View.INVISIBLE);

        }

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
                } else {
                    try {
                        db.execSQL("INSERT INTO schedule VALUES(null, '"
                                + editTitle.getText().toString() + "', '"
                                + editDate.getText().toString() + "', '"
                                + editTime.getText().toString() + "', '"
                                + editlocate.getText().toString() + "', '"
                                + editMemo.getText().toString() + "');");
                    } catch (SQLException e) {
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
