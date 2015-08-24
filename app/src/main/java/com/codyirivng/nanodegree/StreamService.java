package com.codyirivng.nanodegree;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

public class StreamService extends Service implements MediaPlayer.OnPreparedListener {
    private final IBinder mBinder = new LocalBinder();
    MediaPlayer mediaPlayer;

    public StreamService() {
    }
    public class LocalBinder extends Binder {
        StreamService getService() {
            return StreamService.this;
        }
    }
    String[] trackUrlStrings;
    String[] thumbUrlStrings;
    String[] albumNameStrings;
    String[] trackNameStrings;
    int position;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            if (intent != null) {
                trackUrlStrings = intent.getExtras().getStringArray("trackUrlStrings");
                thumbUrlStrings = intent.getExtras().getStringArray("thumbUrlStrings");
                albumNameStrings = intent.getExtras().getStringArray("albumNameStrings");
                trackNameStrings = intent.getExtras().getStringArray("trackNameStrings");
                position = intent.getExtras().getInt("position");
                mediaPlayer = SpotifyStreamFragment.sInfo.getMediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(trackUrlStrings[position]);
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        this.stopService(intent);
        return super.onUnbind(intent);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        mp.start();
    }

    @Override
    public boolean stopService(Intent name) {

        return super.stopService(name);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }
    public void startStop() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        else if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }
    public void next() {
        if(position < trackNameStrings.length-1) {  // FIXME: 8/5/15 Hardcoding instead of wraparound
            try {
                mediaPlayer.reset();

                mediaPlayer.setDataSource(trackUrlStrings[Math.abs(++position % 10)]);
                SpotifyStreamFragment.newThumb(position % 10);
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void prev() {
        if(position > 0) {  // FIXME: 8/5/15 Hardcoding instead of wrapping around
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(trackUrlStrings[Math.abs(--position % 10)]);
                SpotifyStreamFragment.newThumb(position % 10);
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.prepareAsync(); // might take long! (for buffering, etc)
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void resetPlayer() {
        if(mediaPlayer != null) mediaPlayer.reset();
    }
    @Override
    public void sendBroadcast(Intent intent) {
        super.sendBroadcast(intent);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

}
