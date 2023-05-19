package com.example.libsickv00;


import static com.example.libsickv00.MainActivity.albumFiles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumFragment extends Fragment {

    public AlbumFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_album, container, false);


        recyclerView = view.findViewById(R.id.recylerViewForAlbums);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(13);

        if(!albumFiles.isEmpty()){
            AlbumAdapter adapter = new AlbumAdapter(getContext(), albumFiles);
            recyclerView.setAdapter(adapter);

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }

        return view;
    }
}