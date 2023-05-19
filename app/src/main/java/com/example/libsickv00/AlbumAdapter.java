package com.example.libsickv00;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Songs> albumFiles;


    public AlbumAdapter(Context context, ArrayList<Songs> albumFiles){
        this.context = context;
        this.albumFiles = albumFiles;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView imageViewForAlbumitem;
        TextView textViewForAlbumItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewForAlbumitem = itemView.findViewById(R.id.imageViewForAlbumItem);
            textViewForAlbumItem = itemView.findViewById(R.id.textViewforAlbumItem);
            cardView = itemView.findViewById(R.id.cardViewforAlbum);
        }
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.album_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.textViewForAlbumItem.setText(albumFiles.get(position).getAlbum());

        // applying albumarts into respective songs
        Glide.with(context)
                .load(albumFiles.get(position).getArtUri())
                .apply(new RequestOptions().placeholder(R.drawable.note))
                .into(holder.imageViewForAlbumitem);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra("albumName" , albumFiles.get(holder.getAdapterPosition()).getAlbum());
                ContextCompat.startActivity(context,intent, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumFiles.size();
    }




}
