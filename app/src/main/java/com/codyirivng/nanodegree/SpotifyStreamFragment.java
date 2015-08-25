package com.codyirivng.nanodegree;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpotifyStreamFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpotifyStreamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SpotifyStreamFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TRACK_URL_STRINGS = "trackUrlStrings";
    private static final String THUMB_URL_STRINGS = "thumbUrlStrings";
    private static final String ALBUM_NAME_STRINGS = "albumNameStrings";
    private static final String TRACK_NAME_STRINGS = "trackNameStrings";
    private static final String POSITION_INT = "position";
    private static final String SHOW_AS_DIALOG = "showAsDialog";
    public static StreamPlayerInfo sInfo;
    static boolean stopRunnable = false;
    static View rootView;
    private static String[] thumbUrlStrings;
    int position;
    boolean showAsDialog;
    StreamService streamService;
    boolean mBound = false;

    int playbackPosition;
    Handler handler;
    boolean progressing = false;

    Runnable r = new Runnable() {  //Keep track of seekbar
        @Override
        public void run() {
            if(getStopRunnable()) {
                progressing = false;
                return;
            }
            if(streamService.mediaPlayer.isPlaying()){
                playbackPosition = streamService.mediaPlayer.getCurrentPosition();
                int totalDuration = streamService.mediaPlayer.getDuration();
                setCurrentPosition(playbackPosition, totalDuration);
            }

            handler.postDelayed(r, 500);
        }
    };

    private OnFragmentInteractionListener mListener;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StreamService.LocalBinder binder = (StreamService.LocalBinder) service;
            streamService = binder.getService();
            mBound = true;
            autoProgressSeekbar();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };
    public SpotifyStreamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param trackUrlStrings Parameter 1.
     * @param thumbUrlStrings Parameter 2.
     * @param albumNameStrings Parameter 3.
     * @param trackNameStrings Parameter 4.
     * @param position Parameter 5.
     * @return A new instance of fragment SpotifyStreamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpotifyStreamFragment newInstance(String[] trackUrlStrings, String[] thumbUrlStrings, String[] albumNameStrings, String[] trackNameStrings, int position, boolean showAsDialog) {
        SpotifyStreamFragment fragment = new SpotifyStreamFragment();
        Bundle args = new Bundle();
        args.putStringArray(TRACK_URL_STRINGS, trackUrlStrings);
        args.putStringArray(THUMB_URL_STRINGS, thumbUrlStrings);
        args.putStringArray(ALBUM_NAME_STRINGS, albumNameStrings);
        args.putStringArray(TRACK_NAME_STRINGS, trackNameStrings);
        args.putInt(POSITION_INT, position);
        args.putBoolean(SHOW_AS_DIALOG, showAsDialog);
        fragment.setArguments(args);
        sInfo=null;
        return fragment;
    }

    public static View getRootView() {
        return rootView;
    }

     public static void newThumb(int position) {
        ImageView albumImageView = (ImageView)getRootView().findViewById(R.id.streamer_album_thumbnail_imageview);
        albumImageView.setImageResource(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
        try {
            URL url = new URL(thumbUrlStrings[position]);

            Picasso picasso = new Picasso.Builder(rootView.getContext()).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                }
            }).build();
            Picasso.with(rootView.getContext()).load(url.toString()).placeholder(R.drawable.abc_btn_rating_star_off_mtrl_alpha).into(albumImageView, new Callback() {
                @Override
                public void onSuccess() {
                    ImageButton playpause = (ImageButton) rootView.findViewById(R.id.button_playpause);
                    playpause.setImageResource(android.R.drawable.ic_media_pause);

                }
                @Override
                public void onError(){

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopPlayingTrack() {

        stopRunnable = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!mBound) {
            if (getArguments() != null) {
                String[] trackUrlStrings = getArguments().getStringArray(TRACK_URL_STRINGS);
                thumbUrlStrings = getArguments().getStringArray(THUMB_URL_STRINGS);
                String[] albumNameStrings = getArguments().getStringArray(ALBUM_NAME_STRINGS);
                String[] trackNameStrings = getArguments().getStringArray(TRACK_NAME_STRINGS);
                position = getArguments().getInt(POSITION_INT);
                showAsDialog = getArguments().getBoolean(SHOW_AS_DIALOG);
                setShowsDialog(showAsDialog);
                handler = new Handler();
                sInfo = new StreamPlayerInfo(trackUrlStrings[position],new MediaPlayer());
                Intent streamStarterIntent = new Intent(getActivity(), StreamService.class);
                streamStarterIntent.putExtra("trackUrlStrings", trackUrlStrings);
                streamStarterIntent.putExtra("thumbUrlStrings", thumbUrlStrings);
                streamStarterIntent.putExtra("albumNameStrings", albumNameStrings);
                streamStarterIntent.putExtra("trackNameStrings", trackNameStrings);
                streamStarterIntent.putExtra("position", position);

                getActivity().startService(streamStarterIntent);
                getActivity().bindService(streamStarterIntent, mConnection, Context.BIND_AUTO_CREATE);
            }
        }
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onStop() {

        super.onStop();
    }

    private void setCurrentPosition(final int playbackPosition, final int totalTrackTime) {
        final SeekBar sb = (SeekBar)rootView.findViewById(R.id.streamer_seekBar);
        final TextView currentTime = (TextView)rootView.findViewById(R.id.currentTime);
        final TextView totalTime = (TextView)rootView.findViewById(R.id.totalTime);
        Double dcp = Math.ceil(playbackPosition);
        int cp = dcp.intValue();

        if((cp>=(totalTrackTime -500)) && cp > 0) { // subtrack .5 second from total for some reason
            progressing = false;
            stopRunnable = true;

            handler.removeCallbacksAndMessages(null);
            sb.setMax(0);
            sb.setProgress(0);
            String format = "mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
            String currentPos = sdf.format(new Date(0));
            String totalLen = sdf.format(new Date(0));
            totalTime.setText(totalLen);
            currentTime.setText(currentPos);
            streamService.startStop();
            streamService.mediaPlayer.seekTo(1);
            ImageButton playButton = (ImageButton) rootView.findViewById(R.id.button_playpause); //edge case pause while seeking
            playButton.setImageResource(android.R.drawable.ic_media_play);
        }else {
            String format = "mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
            String currentPos = sdf.format(new Date(cp));
            String totalLen = sdf.format(new Date(totalTrackTime));

            totalTime.setText(totalLen);
            currentTime.setText(currentPos);
            sb.setMax(totalTrackTime);
            sb.setProgress(cp);
        }
    }

    private boolean getStopRunnable() {


        return stopRunnable;
    }

    private void autoProgressSeekbar() {

        if(!progressing){
            stopRunnable = false;
            r.run();
            progressing = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_spotify_stream, container, false);
        ImageView albumImageView = (ImageView)rootView.findViewById(R.id.streamer_album_thumbnail_imageview);
        albumImageView.setImageResource(R.drawable.abc_btn_rating_star_off_mtrl_alpha);
        try {
            URL url = new URL(thumbUrlStrings[position]);

            Picasso picasso = new Picasso.Builder(getActivity()).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    exception.printStackTrace();
                }
            }).build();
            Picasso.with(getActivity()).load(url.toString()).placeholder(R.drawable.abc_btn_rating_star_off_mtrl_alpha).into(albumImageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        SeekBar sb = (SeekBar) rootView.findViewById(R.id.streamer_seekBar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) return;
                streamService.mediaPlayer.seekTo(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                stopRunnable = true;
                streamService.mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setCurrentPosition(streamService.mediaPlayer.getCurrentPosition(), streamService.mediaPlayer.getDuration());

                streamService.mediaPlayer.start();
                ImageButton playButton = (ImageButton) rootView.findViewById(R.id.button_playpause); //edge case pause while seeking
                playButton.setImageResource(android.R.drawable.ic_media_pause);
                stopRunnable = false;
                autoProgressSeekbar();
            }
        });

        ImageButton backButton = (ImageButton)rootView.findViewById(R.id.button_back);
        final ImageButton playButton = (ImageButton)rootView.findViewById(R.id.button_playpause);
        ImageButton nextButton = (ImageButton)rootView.findViewById(R.id.button_forward);
        playButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if(streamService.mediaPlayer.isPlaying()) {

                    stopRunnable = true;
                    progressing = true;
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                    streamService.startStop();
                } else {

                    stopRunnable = false;
                    progressing = false;
                    autoProgressSeekbar();
                    playButton.setImageResource(android.R.drawable.ic_media_pause);
                    streamService.startStop();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRunnable = true;
                handler.removeCallbacksAndMessages(null);
                progressing = false;
                streamService.prev();
                autoProgressSeekbar();

            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRunnable = true;
                handler.removeCallbacksAndMessages(null);
                progressing = false;
                streamService.next();
                autoProgressSeekbar();
            }
        });
        return rootView;
    }

    @Override
    public void onDestroy() {
       super.onDestroy();
        if(mBound) {
            if(!getActivity().isChangingConfigurations()) { //don't call if orientation changing
                if(streamService != null) {
                    streamService.resetPlayer();
                }

                getActivity().unbindService(mConnection);
                stopRunnable = true; //stops autoProgress seekbar
                mBound=false;
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragment4Interaction(int position);
    }


}
class StreamPlayerInfo implements Parcelable {
    public static final Parcelable.Creator<StreamPlayerInfo> CREATOR
            = new Parcelable.Creator<StreamPlayerInfo>() {
        public StreamPlayerInfo createFromParcel(Parcel in) {
            return new StreamPlayerInfo(in);
        }

        public StreamPlayerInfo[] newArray(int size) {
            return new StreamPlayerInfo[size];
        }
    };
    public MediaPlayer mediaPlayer;
    String trackUrl;

    StreamPlayerInfo(String trackUrl, MediaPlayer mp) {
        this.trackUrl = trackUrl;
        if(mediaPlayer == null) this.mediaPlayer = mp;
    }

    StreamPlayerInfo(Parcel in) {
    }

    public String getTrackUrl() {
        return trackUrl;
    }

    public void setTrackUrl(String trackUrl) {
        this.trackUrl = trackUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}