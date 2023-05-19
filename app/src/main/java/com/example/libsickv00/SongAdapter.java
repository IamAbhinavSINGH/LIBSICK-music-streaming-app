package com.example.libsickv00;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public  class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {

    private  Context context;
    private  ArrayList<Songs> songFiles;


    public SongAdapter(Context context, ArrayList<Songs> songFiles){
        this.context = context;
        this.songFiles = songFiles;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewforSongItem;
        TextView SongName, ArtistName, Duration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewforSongItem = itemView.findViewById(R.id.imageViewForSongItem);
            SongName = itemView.findViewById(R.id.SongName);
            ArtistName = itemView.findViewById(R.id.ArtistName);
            Duration = itemView.findViewById(R.id.Duration);

        }
    }




    //methods for arranging the songs into recycler view of SongFragment
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.song_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.SongName.setText(songFiles.get(position).getTitle());
        holder.ArtistName.setText(songFiles.get(position).getArtistName());

        String duration = formatDuration(songFiles.get(position).getDuration());
        holder.Duration.setText(duration);

        // applying albumarts into respective songs
        Glide.with(context)
                .load(songFiles.get(position).getArtUri())
                .apply(new RequestOptions().placeholder(R.drawable.note))
                .into(holder.imageViewforSongItem);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(context, playerActivity.class);
                intent.putExtra("index", holder.getAdapterPosition());
                intent.putExtra("class" , "MusicAdapter");
                ContextCompat.startActivity(context, intent, null);
            }
        });

    }


    @Override
    public int getItemCount() {
        return songFiles.size();
    }


    public String formatDuration(Long duration){

        long minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
        long seconds = (TimeUnit.SECONDS.convert(duration,TimeUnit.MILLISECONDS) -
                minutes*TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES));

        return String.format("%02d:%02d", minutes, seconds);

    }

}
