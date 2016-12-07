package com.example.calendar_1394020_1394040;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    private VideoView videoView;
    Button finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        String VIDEO_URL = getIntent().getStringExtra("video_uri");
        finishBtn = (Button) findViewById(R.id.finishBtn);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                videoView.stopPlayback();
                finish();
            }
        });

        videoView = (VideoView) findViewById(R.id.videoView);

        videoView.setVideoURI(Uri.parse(VIDEO_URL));

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer player) {
                videoView.seekTo(0);
                videoView.start();
            }
        });

    }
}
