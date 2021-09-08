package com.jawaid.videoplayer.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jawaid.videoplayer.R;
import com.jawaid.videoplayer.activities.Adapters.VideosAdapter;
import com.jawaid.videoplayer.activities.Models.Tost;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class VideosListActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<String> videoFiles;
    private VideosAdapter adapter;
    private BottomSheetDialog bottomSheetDialog;
    private TextView titleBottomSheet;
    private String currentFilepath;
    private int currentFilePosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycleViewvideolist);
        bottomSheetDialog = new BottomSheetDialog(VideosListActivity.this, R.style.AppBottomSheetDialogTheme);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);
        bottomSheetDialog.setContentView(bottomSheetView);


        View shareBs = bottomSheetView.findViewById(R.id.sharebottomsheet);
        View deleteBs = bottomSheetView.findViewById(R.id.deletebottomsheet);
        View renameBs = bottomSheetView.findViewById(R.id.renamebottomsheet);
        View propertiesBs = bottomSheetView.findViewById(R.id.propertiesbottomsheet);
        titleBottomSheet = bottomSheetView.findViewById(R.id.titlebottomSheet);

        shareBs.setOnClickListener(this);
        deleteBs.setOnClickListener(this);
        renameBs.setOnClickListener(this);
        propertiesBs.setOnClickListener(this);

        videoFiles = new ArrayList<>();

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");


        try {
            File directory = new File(path);
            getSupportActionBar().setTitle(directory.getName());

            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isHidden() && files[i].getName().endsWith(".mp4") || files[i].getName().endsWith(".mkv")
                        || files[i].getName().endsWith(".avi") || files[i].getName().endsWith(".webm") || files[i].getName().endsWith(".MKV")
                        || files[i].getName().endsWith(".MPEG") || files[i].getName().endsWith(".MPEG-4")
                        || files[i].getName().endsWith(".flv") || files[i].getName().endsWith(".FMP4") || files[i].getName().endsWith(".M4A"))
                    videoFiles.add(files[i].getPath());
            }

        }catch (Exception err){
            Tost.showLong(this,"loading error "+err.getMessage());
        }

        setUpRecyclerView(1);

    }

    private void setUpRecyclerView(int cols) {


        adapter = new VideosAdapter(VideosListActivity.this, videoFiles);
        recyclerView.setLayoutManager(new GridLayoutManager(this, cols));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListen(new VideosAdapter.OnItemClickListen() {
            @Override
            public void OnItemClickListen(int position) {

                if (getExtension(new File(videoFiles.get(position))).equals("mkv")
                        ||getExtension(new File(videoFiles.get(position))).equals("MKV")){

                    Intent intent=new Intent(VideosListActivity.this,ExoPlayerActivity.class);
                    intent.putStringArrayListExtra("videoFiles",videoFiles);
                    intent.putExtra("pos",position);
                    startActivity(intent);
                } else {
                    Intent intent=new Intent(VideosListActivity.this,MyPlayerActivity.class);
                    intent.putStringArrayListExtra("videoFiles",videoFiles);
                    intent.putExtra("pos",position);
                    startActivity(intent);
                }

            }

            @Override
            public void OnMenuClickListen(int position) {
                currentFilepath=videoFiles.get(position);
                titleBottomSheet.setText(new File(videoFiles.get(position)).getName());
                currentFilePosition=position;
                bottomSheetDialog.show();
            }
        });
    }

    private String getExtension(File file)
    {
        String fileName = file.getName();
        String[] ext = fileName.split("\\.");
        return ext[ext.length -1];
    }

    private String getFileInfo(String uri) throws IOException {
        File file = new File(uri);
        String info = "Name: \t" + file.getName() + "\n\n" +
                "Location: \t" + file.getPath() + "\n\n" +
                "Size: \t" + getFileSize(file.length()) + "\n\n";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            BasicFileAttributes attributes = Files.readAttributes(Paths.get(uri), BasicFileAttributes.class);
            String extra = "Creation Time: \t" + attributes.creationTime() + "\n\n" +
                    "Last Acces Time: \t" + attributes.lastAccessTime() + "\n\n";
            info = info + extra;
        }

        return info;

    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.video_menu, menu);

        MenuItem item=menu.findItem(R.id.search_m);
        SearchView searchView= (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.refresh_video_menu:
                setUpRecyclerView(1);
                return true;
            case R.id.settings_video_menu:
                startActivity(new Intent(VideosListActivity.this,SettingsActivity.class));
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sharebottomsheet:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("video/*");
                share.putExtra(Intent.EXTRA_STREAM,
                        Uri.parse(currentFilepath));
                startActivity(Intent.createChooser(share, "Share Video"));
                bottomSheetDialog.cancel();
                break;
            case R.id.deletebottomsheet:
                new AlertDialog.Builder(this, R.style.StyledDialog)
                        .setTitle("Do you really want to Delete")
                        .setMessage(titleBottomSheet.getText() + " ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            public void onClick(DialogInterface dialog, int whichButton) {

                                File file = new File(currentFilepath);
                                file.getAbsoluteFile();
                                if (file.delete()) {
                                    Toast.makeText(VideosListActivity.this, "Deleted Succesfully", Toast.LENGTH_SHORT).show();
                                    adapter.notifyItemRemoved(currentFilePosition);
                                    adapter.notifyDataSetChanged();
                                } else
                                    Toast.makeText(VideosListActivity.this, "Deleting failed", Toast.LENGTH_SHORT).show();

                                //Toast.makeText(VideosListActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

                bottomSheetDialog.cancel();
                break;
            case R.id.renamebottomsheet:
                bottomSheetDialog.cancel();
                showRenameDialoge();
                break;
            case R.id.propertiesbottomsheet:
                bottomSheetDialog.cancel();
                try {
                    new AlertDialog.Builder(this, R.style.StyledDialog)
                            .setTitle("Properties")
                            .setMessage(getFileInfo(currentFilepath))
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            }).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void showRenameDialoge() {
        final EditText editText = new EditText(this);
        editText.setTextColor(getResources().getColor(android.R.color.white));

        editText.setText(new File(currentFilepath).getName());
        new AlertDialog.Builder(this, R.style.StyledDialog)
                .setTitle("Rename")
                .setIcon(android.R.drawable.ic_menu_edit)
                .setView(editText)
                .setPositiveButton("Rename", new DialogInterface.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .setNegativeButton(android.R.string.cancel, null).show();
    }
}