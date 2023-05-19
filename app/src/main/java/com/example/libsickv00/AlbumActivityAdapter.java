package com.example.libsickv00;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AlbumActivityAdapter extends RecyclerView.Adapter<AlbumActivityAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Songs> albumFiles;

    public AlbumActivityAdapter(Context context,  ArrayList<Songs> albumFiles){
        this.context = context;
        this.albumFiles = albumFiles;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView SongName, ArtistName, Duration;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewForSongItem);
            SongName = itemView.findViewById(R.id.SongName);
            ArtistName = itemView.findViewById(R.id.ArtistName);
            Duration = itemView.findViewById(R.id.Duration);
            linearLayout = itemView.findViewById(R.id.linearLayoutofSongItem);

        }
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumActivityAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.song_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.SongName.setText(albumFiles.get(position).getTitle());
        holder.ArtistName.setText(albumFiles.get(position).getArtistName());

        String duration = formatDuration(albumFiles.get(position).getDuration());
        holder.Duration.setText(duration);

        Glide.with(context)
                .load(albumFiles.get(position).getArtUri())
                .apply(new RequestOptions().placeholder(R.drawable.note))
                .into(holder.imageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, playerActivity.class);
                intent.putExtra("index", holder.getAdapterPosition());
                intent.putExtra("class", "AlbumAdapter");
                ContextCompat.startActivity(context,intent, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }



    public String formatDuration(Long duration){

        long minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
        long seconds = (TimeUnit.SECONDS.convert(duration,TimeUnit.MILLISECONDS) -
                minutes*TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES));

        return String.format("%02d:%02d", minutes, seconds);

    }

}
