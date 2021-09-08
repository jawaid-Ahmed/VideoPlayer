package com.jawaid.videoplayer.activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.jawaid.videoplayer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FoldersAdapter extends Adapter<FoldersAdapter.SongHolder> {
    Context context;
    ArrayList<String> videos;
    OnItemClickListen listener;
    HashMap<String,Integer> hashMap;
    private String str;

    public interface OnItemClickListen{
        void OnItemClickListen(int position);
        void OnMenuClickListen(int position);
    }

    public void setOnItemClickListen(OnItemClickListen listener){
        this.listener=listener;
    }

    class SongHolder extends ViewHolder {
        LinearLayout rootlayout;
        ImageView thumb;
        TextView title,noOfVideos;

        public SongHolder(final View myV, OnItemClickListen listen) {
            super(myV);
            this.title =myV.findViewById(R.id.titleTextfolder);
            this.thumb =myV.findViewById(R.id.thumbnailfolder);
            this.noOfVideos=myV.findViewById(R.id.noOfVideosfolder);
            this.rootlayout =myV.findViewById(R.id.rootItemlayoutfolder);

            myV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null){
                        int position=getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION){
                            listener.OnItemClickListen(position);
                        }
                    }
                }
            });
        }
    }



    public FoldersAdapter(Context context2, ArrayList<String> videos2, HashMap<String,Integer> hashMap) {
        this.context = context2;
        this.videos = videos2;
        this.hashMap=hashMap;
    }

    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.single_folder_item, parent, false);

        return new SongHolder(view,listener);
    }

    public void onBindViewHolder(SongHolder holder, final int position) {
        final String path = this.videos.get(holder.getAdapterPosition());
        holder.title.setText(new File(path).getName());


        if ((hashMap.get(new File(path).getName()) >1)){
            str=hashMap.get(new File(path).getName())+" Videos";
        }else
            str=hashMap.get(new File(path).getName())+" Video";

        holder.noOfVideos.setText(str);


    }

    public int getItemCount() {
        return this.videos.size();
    }

    public void updateList(ArrayList<String> newList) {
        this.videos = new ArrayList<>();
        this.videos.addAll(newList);
        notifyDataSetChanged();
    }
}
