package com.jawaid.videoplayer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.jawaid.videoplayer.R;
import com.jawaid.videoplayer.activities.Models.Tost;

import java.io.File;
import java.util.ArrayList;

public class ExoPlayerActivity extends AppCompatActivity {

    private static final String TAG = ExoPlayerActivity.class.getName();
    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;

    private Uri uri;
    private ArrayList<String> videos;
    private int videoPosition=0;
    private String currentVideoName;
    private int uiImmersiveOptions;
    private View decorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);

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
        
        
        mPlayerView = (PlayerView) findViewById(R.id.playerView);


        videos=new ArrayList<>();


        videos = getIntent().getStringArrayListExtra("videoFiles");
        videoPosition=getIntent().getIntExtra("pos",0);

        currentVideoName=new File(videos.get(videoPosition)).getName();
        uri= Uri.fromFile(new File(videos.get(videoPosition)));

        Tost.showLong(this,"Sorry Video Controls Not Supported For this");

    }

    @Override
    protected void onStart() {
        super.onStart();

        mExoPlayer=ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        mPlayerView.setPlayer(mExoPlayer);

        DefaultDataSourceFactory defaultDataSourceFactory=new DefaultDataSourceFactory(this,
                Util.getUserAgent(this,"exo-demo"));
        ExtractorMediaSource mediaSource=new ExtractorMediaSource.Factory(defaultDataSourceFactory).createMediaSource(uri);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    protected void onStop() {
        mPlayerView.setPlayer(null);
        mExoPlayer.release();
        mExoPlayer=null;
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        mExoPlayer.stop();
        super.onBackPressed();
    }
}