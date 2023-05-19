package com.example.libsickv00;

import static com.example.libsickv00.MainActivity.songFiles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongFragment extends Fragment {

    public SongFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_song, container, false);

       ArrayList<Songs> list = songFiles;

        recyclerView = view.findViewById(R.id.recylerViewForSongs);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10);



        if(!list.isEmpty()){
            SongAdapter songAdapter = new SongAdapter(getContext(), list);
            recyclerView.setAdapter(songAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        }


        return view;
    }

}