package com.example.libsick;

import static com.example.libsick.MainActivity.songfiles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SongsFragment extends Fragment {


        SongAdapter songAdapter;


    public SongsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
       RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
       if(!(songfiles.size() < 1)){
           songAdapter = new SongAdapter(getContext(), songfiles);
           recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
           recyclerView.setAdapter(songAdapter);

       }


        return view;
    }
}