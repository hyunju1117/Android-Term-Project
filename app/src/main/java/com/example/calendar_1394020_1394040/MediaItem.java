package com.example.calendar_1394020_1394040;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;


public class MediaItem {
    final static int SDCARD=0;
    final static int AUDIO=1;
    final static int VIDEO=2;
    final static int IMAGE=3;

    int source;
    String name;
    Uri uri;
    int type;
    Bitmap bitmap;

    public MediaItem(int src, String name, Uri uri) {
        source = src;
        this.name = name;
        this.uri = uri;
        type = AUDIO;
    }

    public MediaItem(int src, String name, Uri uri, int type) {
        source = src;
        this.name = name;
        this.uri = uri;
        this.type = type;
        if (type == IMAGE)
            bitmap = BitmapFactory.decodeFile(uri.getPath());
        else bitmap = null;
    }

}
