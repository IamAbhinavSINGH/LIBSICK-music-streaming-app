package com.example.libsickv00;

import static com.example.libsickv00.MainActivity.songFiles;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity {

    ImageView imageView;
    RecyclerView recyclerView;
    ArrayList<Songs> albumSongs = new ArrayList<>();
    static ArrayList<Songs> albumSongsforPA = new ArrayList<>();
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        recyclerView = findViewById(R.id.recylerViewForAlbumActivity);
        imageView = findViewById(R.id.imageViewforAlbumActivity);
        textView = findViewById(R.id.AlbumName);

        getSongsFromAlbum();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!albumSongs.isEmpty()){
            AlbumActivityAdapter albumActivityAdapter = new AlbumActivityAdapter(this, albumSongs);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(13);
            recyclerView.setAdapter(albumActivityAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }
    }

    private void getSongsFromAlbum() {

        String albumName = getIntent().getStringExtra("albumName");

        for(int i = 0; i< songFiles.size(); i++){
            if(albumName.equals(songFiles.get(i).getAlbum())){
                albumSongs.add(songFiles.get(i));
            }
        }

        Log.e("albumName :" + albumName, "albumSize " + albumSongs.size());

        Glide.with(getApplicationContext())
                .load(albumSongs.get(0).getArtUri())
                .apply(new RequestOptions().placeholder(R.drawable.note))
                .into(imageView);

        textView.setText(albumName);

        albumSongsforPA = albumSongs;
    }
}