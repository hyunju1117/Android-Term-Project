package com.example.calendar_1394020_1394040;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class ImageActivity extends AppCompatActivity {

    private ImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        String IMAGE_URL = getIntent().getStringExtra("image_uri");

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageURI(Uri.parse(IMAGE_URL));
    }
}
