package com.example.calendar_1394020_1394040;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class ImageActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        String IMAGE_URL = getIntent().getStringExtra("image_uri");

        try{
            final ImageView iv = (ImageView)findViewById(R.id.image);
            Uri uri = Uri.parse(IMAGE_URL);
            final Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
            iv.setImageBitmap(bitmap);
        } catch(Exception e){
        }
    }
}
