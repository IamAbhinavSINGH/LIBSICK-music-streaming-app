package com.example.libsickv00;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    public static ArrayList<Songs> songFiles = new ArrayList<>();
     public static ArrayList<Songs> albumFiles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initalizingIDs();

       if(checkPermission()){
           settingUpViewPager();
        }

    }



    private void initalizingIDs(){
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPagerforMainActivity);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("LIBSICK");
    }

    private void settingUpViewPager(){
        songFiles = getSongsFromDevice();
        ViewPagerAdapterForMainActivity viewPageradapter = new ViewPagerAdapterForMainActivity(getSupportFragmentManager());

        viewPager.setAdapter(viewPageradapter);
        tabLayout.setupWithViewPager(viewPager);
    }



    // checking permission
    private boolean checkPermission(){

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this, permission, 1);

            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                settingUpViewPager();
            }
        }
        else{
                finish();
                System.out.println("It is not working");
        }
    }



    // accessing all the audio files from the device
    private ArrayList<Songs> getSongsFromDevice(){

        ArrayList<Songs> tempList = new ArrayList<>();
        ArrayList<String> duplicate = new ArrayList<>();

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        String[] projection = {MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST
                ,MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM
                ,MediaStore.Audio.Media.DATE_ADDED, MediaStore.Audio.Media.ALBUM_ID};


        Cursor cursor = this.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection,
                null, MediaStore.Audio.Media.DATE_ADDED + " DESC", null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    @SuppressLint("Range") String titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    @SuppressLint("Range") String idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                    @SuppressLint("Range") String artistC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    @SuppressLint("Range") String albumC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    @SuppressLint("Range") String pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    @SuppressLint("Range") long durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                    @SuppressLint("Range") String albumIDC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                    Uri uri = Uri.parse("content://media/external/audio/albumart");
                    String uriC = Uri.withAppendedPath(uri, albumIDC).toString();

                    Songs songFiles = new Songs(idC,titleC, artistC, pathC, durationC, albumC, uriC);

                    Log.e("Path : " + songFiles.getPath(), "Album: "+ albumC);


                    tempList.add(songFiles);

                    if(!duplicate.contains(albumC)){
                            albumFiles.add(songFiles);
                            duplicate.add(albumC);
                    }

                }while(cursor.moveToNext());
            }
            cursor.close();
        }

        return tempList;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!playerActivity.isPlaying && playerActivity.musicService != null){
            playerActivity.musicService.stopForeground(true);
            playerActivity.musicService.mediaPlayer.release();
            playerActivity.musicService.mediaPlayer = null;
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        else if(playerActivity.musicService != null){
            playerActivity.pauseMusic();
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Exit")
                .setMessage("Do you want to close app?")
                .setIcon(R.drawable.ic_baseline_exit_to_app_24)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(playerActivity.musicService != null) {
                            playerActivity.musicService.stopForeground(true);
                            playerActivity.musicService.mediaPlayer.release();
                            playerActivity.musicService.mediaPlayer = null;
                        }
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
}