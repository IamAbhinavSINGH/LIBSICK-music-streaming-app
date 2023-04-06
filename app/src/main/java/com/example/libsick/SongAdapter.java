package com.example.libsick;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<SongFiles> sFiles;


    SongAdapter(Context context, ArrayList<SongFiles> sFiles){
        this.context = context;
        this.sFiles  = sFiles;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.song_Name.setText(sFiles.get(i).getTitle());

//        byte[] image = getAlbumArt(sFiles.get(i).getPath());
//        if(image != null){
//                    Glide.with(context).asBitmap()
//                                      .load(image)
//                                      .into(myViewHolder.album_art);
//        }
//        else{
//            Glide.with(context).asBitmap()
//                    .load(R.drawable.image)
//                    .into(myViewHolder.album_art);

      //  }
    }

    @Override
    public int getItemCount() {
        return sFiles.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView song_Name;
        ImageView album_art;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            song_Name = itemView.findViewById(R.id.song_name);
            album_art = itemView.findViewById(R.id.song_image);

        }
    }

    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
