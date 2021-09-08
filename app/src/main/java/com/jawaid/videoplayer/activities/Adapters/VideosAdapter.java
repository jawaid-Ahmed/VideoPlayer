package com.jawaid.videoplayer.activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.jawaid.videoplayer.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class VideosAdapter extends Adapter<VideosAdapter.SongHolder> implements Filterable {
    Context context;
    ArrayList<String> videos;
    OnItemClickListen listener;
    ArrayList<String> videosAll;

    @Override
    public Filter getFilter() {
        return myFilter;
    }
    Filter myFilter = new Filter() {

        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            ArrayList<String> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(videosAll);
            } else {
                for (String movie: videosAll) {
                    if (movie.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(movie);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            videos.clear();
            videos.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListen{
        void OnItemClickListen(int position);
        void OnMenuClickListen(int position);
    }

    public void setOnItemClickListen(OnItemClickListen listener){
        this.listener=listener;
    }

    class SongHolder extends ViewHolder {
        ImageButton menubtn;
        LinearLayout rootlayout;
        ImageView thumb;
        TextView title;
        //TextView duration;

        public SongHolder(final View myV, OnItemClickListen listen) {
            super(myV);
            this.title =myV.findViewById(R.id.titleText);
            this.menubtn =myV.findViewById(R.id.menuBtn);
            this.thumb =myV.findViewById(R.id.thumbnail);
            this.rootlayout =myV.findViewById(R.id.rootItemlayout);
            //this.duration=myV.findViewById(R.id.myImageViewText);

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
            menubtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null){
                        int position=getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION){
                            listener.OnMenuClickListen(position);
                        }
                    }
                }
            });
        }
    }

    public VideosAdapter(Context context2, ArrayList<String> videos2) {
        this.context = context2;
        this.videos = videos2;
        this.videosAll=new ArrayList<>();
        this.videosAll.addAll(videos2);
    }

    public SongHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SongHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_video_item, parent, false),listener);
    }

    public void onBindViewHolder(SongHolder holder, final int position) {
        final String path = this.videos.get(holder.getAdapterPosition());
        holder.title.setText(new File(path).getName());
        Glide.with(this.context).load(path).centerCrop().into(holder.thumb);

      /*  //metaDataRetriever

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(context, Uri.parse(path));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time );

        /holder.duration.setText(convertSecondsToHMmSs(timeInMillisec));
        retriever.release();
*/



    }
    public static String convertSecondsToHMmSs(long millis) {
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60)) % 60;
        long hours = millis / (1000 * 60 * 60);

        StringBuilder b = new StringBuilder();
        b.append(hours == 0 ? "" : hours < 10 ? String.valueOf("0" + hours) :
                String.valueOf(hours));
        b.append(hours==0 ? "":":");
        b.append(minutes == 0 ? "" : minutes < 10 ? String.valueOf("0" + minutes) :
                String.valueOf(minutes));
        b.append(minutes==0 ? "":":");
        b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) :
                String.valueOf(seconds));
        return b.toString();
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
