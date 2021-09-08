package com.jawaid.videoplayer.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.jawaid.videoplayer.R;

import com.jawaid.videoplayer.activities.Adapters.FoldersAdapter;
import com.jawaid.videoplayer.activities.Models.Tost;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Tost tost=new Tost();
    private ArrayList<String> newList,videoFiles,folders;
    private HashSet foldersHashset;
    HashMap<String,Integer> foldershashMap=new HashMap<>();
    int noOfSongs=0;
    FoldersAdapter adapter;
    public static List<String> filesNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView =findViewById(R.id.recycleView);

        filesNames=new ArrayList<>();

        videoFiles=new ArrayList<>();
        newList=new ArrayList<>();
        folders=new ArrayList<>();
        foldersHashset=new HashSet();

       loadFilesAgain();
    }

    private void loadFilesAgain() {


        videoFiles=getAllMedia();
        filesNames=getFilesNames();

        if (videoFiles.size()>1) {
            for (String path : videoFiles) {
                File file=new File(path).getParentFile();
                foldersHashset.add(file.getPath());

            }
        }
        if (foldersHashset.size()>0) {
            folders = new ArrayList<>(foldersHashset);
        }
        if (folders.size()>0){
            for (String f:folders) {
                File file = new File(f);
                File[] files = file.listFiles();
                for (int i=0; i<files.length; i++) {
                    if (!files[i].isHidden() && files[i].getName().endsWith(".mp4") || files[i].getName().endsWith(".mkv")
                            || files[i].getName().endsWith(".avi")|| files[i].getName().endsWith(".webm")|| files[i].getName().endsWith(".MKV")) {

                        noOfSongs++;
                    }
                }
                foldershashMap.put(file.getName(), noOfSongs);
                noOfSongs=0;
            }
        }

        setUpRecyclerView();
    }


    private List<String> getFilesNames() {
        List<String> names=new ArrayList<>();
        for (String file:videoFiles){
            names.add(new File(file).getName());
        }
        return names;
    }


    private void setUpRecyclerView() {


        adapter = new FoldersAdapter(MainActivity.this, folders,foldershashMap);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);



        adapter.setOnItemClickListen(new FoldersAdapter.OnItemClickListen() {
            @Override
            public void OnItemClickListen(int position) {

                Intent intent=new Intent(MainActivity.this,VideosListActivity.class);
                //intent.putStringArrayListExtra("key", videoFiles);
                intent.putExtra("path",folders.get(position));
                startActivity(intent);
            }

            @Override
            public void OnMenuClickListen(int position) {
                File file= new File(folders.get(position));


                Toast.makeText(MainActivity.this,foldershashMap.get(file.getName())+" ",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.refresh_folder_menu:
                setUpRecyclerView();
                super.onSearchRequested();
                return true;
            case R.id.settings_folder_menu:
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                return true;
            case R.id.night_mode:
                if (AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ArrayList<String> getAllMedia() {
        HashSet<String> videoItemHashSet = new HashSet<>();

        String[] projection = { MediaStore.Video.VideoColumns.DATA ,MediaStore.Video.Media.DISPLAY_NAME};
        Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        try {
            cursor.moveToFirst();
            do{
                videoItemHashSet.add((cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))));
            }while(cursor.moveToNext());

            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<String> downloadedList = new ArrayList<>(videoItemHashSet);
        return downloadedList;
    }

}