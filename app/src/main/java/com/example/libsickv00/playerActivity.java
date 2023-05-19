package com.example.libsickv00;

import static com.example.libsickv00.AlbumActivity.albumSongsforPA;
import static com.example.libsickv00.MainActivity.songFiles;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class playerActivity extends AppCompatActivity implements ServiceConnection, MediaPlayer.OnCompletionListener {

    ExtendedFloatingActionButton  previous, next, back, favourite, options;
    ImageButton shuffle, loop;

    static SeekBar seekBar;
    static TextView currentDuration, totalDuration;
    static ImageView albumArt;
    static TextView songName, artistName;
    static ExtendedFloatingActionButton pausePlay;
    static int position = 0;
    static boolean isPlaying = false;
    static boolean repeat = false, mix = false;
    static MusicService musicService ;
    public static ArrayList<Songs> songlistPA = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.Gradient_Dark, this.getTheme()));
        } else {
            getWindow().setStatusBarColor(getResources().getColor(R.color.Gradient_Dark));
        }

        //starting service
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
        startService(intent);


        initalizeid();
        getSongsfromClass();

        buttonListener();

    }

    private void buttonListener(){
        pausePlay.setOnClickListener(v -> {
            if(isPlaying) pauseMusic();
            else playMusic();
        });
       previous.setOnClickListener(v -> {
           changeSong(false);
           setLayout();
           createMediaPlayer();
       });
        next.setOnClickListener(v -> {
            changeSong(true);
            setLayout();
            createMediaPlayer();
        });
        shuffle.setOnClickListener(v -> shuffle());
        loop.setOnClickListener(v -> {
            if(!repeat){
                repeat = true;
                loop.setImageResource(R.drawable.ic_baseline_repeat_on_24);
                loop.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
            }
            else{
                repeat = false;
                loop.setImageResource(R.drawable.ic_baseline_repeat_24);
            }
        });

        back.setOnClickListener(v -> {
            finish();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) musicService.mediaPlayer.seekTo(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        options.setOnClickListener(v -> showOptionsMenu());
    }

    private void showOptionsMenu() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.options_layout_pa);
        dialog.show();
        dialog.findViewById(R.id.timer).setOnClickListener(v -> {
            Toast.makeText(playerActivity.this, "Timer enabled ", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        dialog.findViewById(R.id.equalizer).setOnClickListener(v -> {
            Toast.makeText(playerActivity.this, "Equalizer mode on", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }


    private void initalizeid(){
        albumArt = findViewById(R.id.albumArtforPlayerActivity);
        songName = findViewById(R.id.SongNameforPlayerActivity);
        artistName = findViewById(R.id.artistNameforPlayerActivity);
        pausePlay = findViewById(R.id.pausePlayforPlayerActivity);
        previous = findViewById(R.id.previousBtnforPlayerActivity);
        next = findViewById(R.id.nextBtnforPlayerActivity);
        shuffle = findViewById(R.id.shuffleBtnforPlayerActivity);
        loop = findViewById(R.id.loopforPlayerActivity);
        seekBar = findViewById(R.id.seekBarforPlayerActivity);
        currentDuration = findViewById(R.id.currentDuration);
        totalDuration = findViewById(R.id.totalDuration);
        back = findViewById(R.id.backForPlayerActivity);
        favourite = findViewById(R.id.favouriteForPlayerActivity);
        options = findViewById(R.id.options);
    }

    private void getSongsfromClass() {

        position = getIntent().getIntExtra("index" , 0);

        switch(getIntent().getStringExtra("class")){
            case"MusicAdapter":
                songlistPA = songFiles;
                setLayout();
                break;
            case"AlbumAdapter":
                songlistPA = albumSongsforPA;
                setLayout();
                break;
        }
    }

    private  void setLayout(){
        Glide.with(this)
                .load(songlistPA.get(position).getArtUri())
                .apply(new RequestOptions().placeholder(R.drawable.note))
                .into(albumArt);

        songName.setText(songlistPA.get(position).getTitle());
        artistName.setText(songlistPA.get(position).getArtistName());
    }

    private  void createMediaPlayer(){

        if(musicService.mediaPlayer == null) musicService.mediaPlayer = new MediaPlayer();

        try {
            musicService.mediaPlayer.reset();
            musicService.mediaPlayer.setDataSource(songlistPA.get(position).getPath());
            musicService.mediaPlayer.prepare();
            musicService.mediaPlayer.start();
            isPlaying = true;
            pausePlay.setIconResource(R.drawable.ic_baseline_pause_24);
            musicService.showNotification(R.drawable.ic_baseline_pause_24);
            currentDuration.setText(formatDuration( (long) musicService.mediaPlayer.getCurrentPosition()));
            totalDuration.setText(formatDuration((long) musicService.mediaPlayer.getDuration()));
            seekBar.setProgress(0);
            seekBar.setMax(musicService.mediaPlayer.getDuration());
            musicService.mediaPlayer.setOnCompletionListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void playMusic(){
        pausePlay.setIconResource(R.drawable.ic_baseline_pause_24);
        isPlaying = true;
        try {
            musicService.mediaPlayer.start();
            musicService.showNotification(R.drawable.ic_baseline_pause_24);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void pauseMusic(){
        pausePlay.setIconResource(R.drawable.ic_baseline_play_arrow_24);
        isPlaying = false;
        try{
            musicService.mediaPlayer.pause();
            musicService.showNotification(R.drawable.ic_baseline_play_arrow_24);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void shuffle(){
        if(!mix) {
            mix = true;
            shuffle.setImageResource(R.drawable.ic_baseline_shuffle_on_24);
            shuffle.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }
        else{
            mix = false;
            shuffle.setImageResource(R.drawable.ic_baseline_shuffle_24);
        }
    }

    public static void changeSong(Boolean increment){
        if(!repeat){
            if(increment){
                position = (position+1)%songlistPA.size();
            }
            else{
                if(position == 0){
                    position = songlistPA.size()-1;
                }
                else{
                    --position;
                }
            }
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        changeSong(true);
        createMediaPlayer();
        setLayout();
    }

    // MUSIC SERVICE RELATED:
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.currentService();
        createMediaPlayer();
        musicService.seekBarSetup();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {musicService = null;}


    // For Formatting Time
    public static  String formatDuration(Long duration){

        long minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
        long seconds = (TimeUnit.SECONDS.convert(duration,TimeUnit.MILLISECONDS) -
                minutes*TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES));

        return String.format("%02d:%02d", minutes, seconds);

    }

}