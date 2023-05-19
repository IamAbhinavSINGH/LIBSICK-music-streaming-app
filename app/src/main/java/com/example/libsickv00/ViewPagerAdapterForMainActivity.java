package com.example.libsickv00;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class ViewPagerAdapterForMainActivity extends FragmentPagerAdapter {


    public ViewPagerAdapterForMainActivity(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0 ){
            return new SongFragment();
        }
        else{
            return new AlbumFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "Songs";
        }
        else{
            return "Albums";
        }
    }
}
