package com.example.libsickv00;

import static com.example.libsickv00.playerActivity.currentDuration;
import static com.example.libsickv00.playerActivity.formatDuration;
import static com.example.libsickv00.playerActivity.musicService;
import static com.example.libsickv00.playerActivity.seekBar;
import static com.example.libsickv00.playerActivity.totalDuration;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.IOException;

public class MusicService extends Service {

    private IBinder myBinder =  new MyBinder();
    MediaPlayer mediaPlayer = null;
    MediaSessionCompat mediaSessionCompat;
    Runnable runnable;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        mediaSessionCompat = new MediaSessionCompat(this, "My music");

        mediaSessionCompat.setMetadata(new MediaMetadataCompat.Builder().build());
        mediaSessionCompat.setActive(true);
        return myBinder;
    }


    // MYBINDER CLASS:-
    public class MyBinder extends Binder{
        public MusicService currentService(){
                return MusicService.this;
        }
    }


    public void showNotification(int playPauseBtn){


        Intent prevIntent = new Intent(getBaseContext(), NotificationReciever.class).setAction(ApplicationClass.PREVIOUS);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, prevIntent , PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(getBaseContext(), NotificationReciever.class).setAction(ApplicationClass.NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, nextIntent , PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(getBaseContext(), NotificationReciever.class).setAction(ApplicationClass.PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, playIntent , PendingIntent.FLAG_UPDATE_CURRENT);

        Intent exitIntent = new Intent(getBaseContext(), NotificationReciever.class).setAction(ApplicationClass.EXIT);
        PendingIntent exitPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, exitIntent , PendingIntent.FLAG_UPDATE_CURRENT);

        // ALBUM ART
        byte[] imageArt = Songs.getAlbumImage(playerActivity.songlistPA.get(playerActivity.position).getPath());
        Bitmap image = null;
        if(imageArt != null){
             image =  BitmapFactory.decodeByteArray(imageArt, 0, imageArt.length);
        }
        else{
             image = BitmapFactory.decodeResource(getResources(), R.drawable.note);
        }


        Notification notification = new NotificationCompat.Builder(this, ApplicationClass.CHANNEL_ID )
                .setContentTitle(playerActivity.songlistPA.get(playerActivity.position).getTitle())
                .setContentText(playerActivity.songlistPA.get(playerActivity.position).getArtistName())
                .setSmallIcon(R.drawable.note)
                .setLargeIcon(image)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .setOngoing(true)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .addAction(R.drawable.ic_baseline_skip_previous_24, "Previous", prevPendingIntent)
                .addAction(playPauseBtn, "Play", playPendingIntent)
                .addAction(R.drawable.ic_baseline_skip_next_24, "Next", nextPendingIntent)
                .addAction(R.drawable.ic_baseline_close_24, "Exit", exitPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSessionCompat.getSessionToken()))
                .build();


        startForeground(1, notification);

    }

    public  void createMediaPlayer(){

        if(musicService.mediaPlayer == null) musicService.mediaPlayer = new MediaPlayer();

        try {
            musicService.mediaPlayer.reset();
            musicService.mediaPlayer.setDataSource(playerActivity.songlistPA.get(playerActivity.position).getPath());
            musicService.mediaPlayer.prepare();
            playerActivity.pausePlay.setIconResource(R.drawable.ic_baseline_pause_24);
            musicService.showNotification(R.drawable.ic_baseline_pause_24);
            currentDuration.setText(formatDuration( (long) musicService.mediaPlayer.getCurrentPosition()));
            totalDuration.setText(formatDuration((long) musicService.mediaPlayer.getDuration()));
            seekBar.setProgress(0);
            seekBar.setMax(musicService.mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void seekBarSetup(){
        runnable = new Runnable() {
            @Override
            public void run() {
                currentDuration.setText(formatDuration( (long) musicService.mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(musicService.mediaPlayer.getCurrentPosition());
                new Handler(Looper.getMainLooper()).postDelayed(runnable, 200);
            }
        };
        new Handler(Looper.getMainLooper()).postDelayed(runnable, 0);
    }
}
