package com.jawaid.videoplayer.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jawaid.videoplayer.R;
import com.jawaid.videoplayer.activities.Models.Tost;
import com.khizar1556.mkvideoplayer.MKPlayer;
import com.khizar1556.mkvideoplayer.MKPlayerActivity;


import java.io.File;
import java.util.ArrayList;

public class MyPlayerActivity extends AppCompatActivity {

    MKPlayer mkplayer;

    private Uri uri;
    private ArrayList<String> videos;
    private int videoPosition=0;
    private String currentVideoName;

    private int uiImmersiveOptions;
    private View decorView;
    private ImageView backArrowPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_player);



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        uiImmersiveOptions = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        );
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiImmersiveOptions);

        videos=new ArrayList<>();
        backArrowPlayer=findViewById(R.id.app_video_finish);

        videos = getIntent().getStringArrayListExtra("videoFiles");
        videoPosition=getIntent().getIntExtra("pos",0);

        currentVideoName=new File(videos.get(videoPosition)).getName();
        uri= Uri.fromFile(new File(videos.get(videoPosition)));

        //MKPlayerActivity.configPlayer(this).play(url);
         mkplayer= new MKPlayer(this);
        mkplayer.play(uri.toString());
        //mkplayer.playInFullScreen(true);
        mkplayer.setTitle(currentVideoName);
        mkplayer.setScaleType(MKPlayer.SCALETYPE_FITPARENT);

        mkplayer.setPlayerCallbacks(new MKPlayer.playerCallbacks() {
            @Override
            public void onNextClick() {

                if (videoPosition==videos.size()-1){
                    videoPosition=0;
                    mkplayer.play(videos.get(videoPosition));
                }else {
                    videoPosition += 1;
                    mkplayer.play(videos.get(videoPosition));
                }
            }
            @Override
            public void onPreviousClick() {

                if (videoPosition==0){
                    videoPosition=videos.size()-1;
                    mkplayer.play(videos.get(videoPosition));
                }else {
                    videoPosition -= 1;
                    mkplayer.play(videos.get(videoPosition));
                }

            }
        });

        backArrowPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mkplayer.stop();
                onBackPressed();
            }
        });


    }


    @Override
    public void onBackPressed() {
        mkplayer.stop();
        mkplayer.onBackPressed();
        super.onBackPressed();
    }
}