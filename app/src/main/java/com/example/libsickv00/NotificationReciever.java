package com.example.libsickv00;

import static com.example.libsickv00.MainActivity.songFiles;
import static com.example.libsickv00.playerActivity.albumArt;
import static com.example.libsickv00.playerActivity.artistName;
import static com.example.libsickv00.playerActivity.musicService;
import static com.example.libsickv00.playerActivity.pausePlay;
import static com.example.libsickv00.playerActivity.position;
import static com.example.libsickv00.playerActivity.songName;
import static com.example.libsickv00.playerActivity.songlistPA;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class NotificationReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()){
            case ApplicationClass.PREVIOUS: prevNextSong(false, context); break;
            case ApplicationClass.PLAY: if(playerActivity.isPlaying) pauseMusic(); else playMusic(); break;
            case ApplicationClass.NEXT: prevNextSong(true, context); break;

            case ApplicationClass.EXIT:
                musicService.stopForeground(true);
                musicService.mediaPlayer.release();
                musicService = null;

                // Exit the app
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
        }
    }

    private void playMusic(){
        playerActivity.isPlaying = true;
        musicService.mediaPlayer.start();
        musicService.showNotification(R.drawable.ic_baseline_pause_24);
        pausePlay.setIconResource(R.drawable.ic_baseline_pause_24);
    }

    private void pauseMusic(){
        playerActivity.isPlaying = false;
        musicService.mediaPlayer.pause();
        musicService.showNotification(R.drawable.ic_baseline_play_arrow_24);
        pausePlay.setIconResource(R.drawable.ic_baseline_play_arrow_24);
    }

    private void prevNextSong(boolean increment, Context context)  {
        playerActivity.changeSong(increment);
        musicService.createMediaPlayer();
        Glide.with(context)
                .load(songlistPA.get(position).getArtUri())
                .apply(new RequestOptions().placeholder(R.drawable.note))
                .into(albumArt);

        songName.setText(songFiles.get(position).getTitle());
        artistName.setText(songlistPA.get(position).getArtistName());
        playMusic();
    }
}
