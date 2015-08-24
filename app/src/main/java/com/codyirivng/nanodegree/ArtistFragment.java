package com.codyirivng.nanodegree;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArtistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArtistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private ArtistSearchResults ASR;
    private ListView artistList;
    private int listPosition = -1;

    private String artistName;
    private OnFragmentInteractionListener mListener;
    private View rootView;

    public ArtistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Artist name to search for.
     * @return A new instance of fragment ArtistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistFragment newInstance(String param1) {
        ArtistFragment fragment = new ArtistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("ASR", ASR);
        if(artistList != null)outState.putInt("ListPosition", artistList.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            ASR = savedInstanceState.getParcelable("ASR");
            listPosition = savedInstanceState.getInt("ListPosition");
            setResultsList(ASR);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    private void setResultsList(final ArtistSearchResults data) {  //set custom array adapter and onclick listener
        //this.ASR = data;
        if (data.getTrackNames() != null && data.getAlbumNames() != null) {
            artistList = (ListView) rootView.findViewById(R.id.listViewArtist);
            ArtistArrayAdapter mListAdapter = new ArtistArrayAdapter(getActivity(), R.layout.list_view_artist, R.id.list_item_artist_song_textview, data.getTrackNames(), data.getThumbNamesSmall(), data.getAlbumNames());
            artistList.setAdapter(mListAdapter);
            if (listPosition != -1) {
                artistList.setSelection(listPosition);
            }
            artistList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    boolean showAsDialog = true;
                    if(getActivity().findViewById(R.id.artist_search_container) == null) showAsDialog = false;  //check 2pane case
                    mListener.onFragment3Interaction(data.getPlayurls(),data.getThumbNamesLarge(),data.getAlbumNames(),data.getTrackNames(), position, showAsDialog); //Start Player
                }
            });
        } else {
            System.err.println("RESULTS NULL...");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artistName = getArguments().getString(ARG_PARAM1);  //artist name
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        android.app.ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            //actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.drawable.spotifylogo);
            actionBar.setTitle("Top 10 Tracks");
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_artist, container, false);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            //onViewStateRestored called
        } else {
            if (savedInstanceState != null) {
                ASR = savedInstanceState.getParcelable("ASR");
                listPosition = savedInstanceState.getInt("ListPosition");
                setResultsList(ASR);
            }
        }
        if (savedInstanceState == null) {
            rootView = inflater.inflate(R.layout.fragment_artist, container, false);
            ArtistSearch query = new ArtistSearch(artistName);
            query.setResultListener(new ArtistSearch.ResultsListener() {
                @Override
                public void resultsSuccess(ArtistSearchResults data) {
                    setResultsList(data);
                }

                @Override
                public void resultsFail() {

                }
            });
            query.execute();
        }
        return rootView;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragment3Interaction(String[] trackUrlStrings, String[] thumbUrlStrings, String[] albumNameStrings, String[] tracknameStrings, int position, boolean showAsDialog);
    }
}


//Custom ArrayAdapter
class ArtistArrayAdapter extends ArrayAdapter<Object> {
    private final Context context;
    private final Object[] values2; //smallthumbnames
    private final Object[] values3; //album

    public ArtistArrayAdapter(Context context, int i, int j, Object[] values1, Object[] values2, Object[] values3) {
        super(context, i, j, values1);
        this.values2 = values2;
        this.values3 = values3;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (values2[position] != null) {

           if(convertView == null) {
               LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               View rowView = inflater.inflate(R.layout.list_view_artist, parent, false);  //custom list view layout
               convertView = rowView;
           }

            ImageView artistThumb = (ImageView) convertView.findViewById(R.id.list_item_artist_image_imageview);
            TextView albumName = (TextView) convertView.findViewById(R.id.list_item_artist_name_textview);
            albumName.setText(values3[position].toString());
            if (artistThumb == null) {
                artistThumb = new ImageView(context);
            }
            try {
                URL url = new URL(values2[position].toString());
                Picasso.with(context).load(url.toString()).placeholder(R.drawable.abc_btn_rating_star_on_mtrl_alpha).resize(60, 60).centerCrop().into(artistThumb);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.getView(position, convertView, parent);
    }

}
//AsyncTask for Spotify getArtistTopTracks
class ArtistSearch extends AsyncTask<Object[], Void, Object[]> {
    private ResultsListener resultListener;
    private String artist;

    ArtistSearch(String artist) {
        this.artist = artist;
    }

    public void setResultListener(ResultsListener resultListener) {
        this.resultListener = resultListener;
    }

    @Override
    protected Object[] doInBackground(Object[]... params) {

        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        String countryParam = "US"; //hardcode
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put(SpotifyService.COUNTRY, countryParam);
        int resultsLength = 0;
        Tracks results = null;
        try {
            results = spotify.getArtistTopTrack(artist, options);
            resultsLength = results.tracks.size();
        }catch (Exception e) {
            System.out.println("No results: Error 400");
            e.printStackTrace();
        }

        if (resultsLength > 0) {
            String[] trackNames = new String[resultsLength];
            String[] albumNames = new String[resultsLength];
            String[] thumbNamesLarge = new String[resultsLength];
            String[] thumbNamesSmall = new String[resultsLength];
            String[] playurls = new String[resultsLength];
            int i = 0;
            for (Track t : results.tracks) {
                trackNames[i] = t.name;
                albumNames[i] = t.album.name;

                playurls[i] = t.preview_url; //30 sec preview track

                if (t.album.images.size() > 1) {
                    thumbNamesLarge[i] = t.album.images.get(0).url;
                    thumbNamesSmall[i] = t.album.images.get(1).url;
                }
                i++;
            }
            return new Object[]{trackNames, albumNames, thumbNamesLarge, thumbNamesSmall, playurls};
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Object[] results) {
        if (results != null) {
            //ugly
            ArtistSearchResults ASR = new ArtistSearchResults((String[]) results[0], (String[]) results[1], (String[]) results[2], (String[]) results[3], (String[]) results[4]);
            resultListener.resultsSuccess(ASR);
        } else {
            resultListener.resultsFail();
        }
    }

    public interface ResultsListener {
        void resultsSuccess(ArtistSearchResults data);

        void resultsFail();
    }
}

class ArtistSearchResults implements Parcelable {
    public static final Parcelable.Creator<ArtistSearchResults> CREATOR
            = new Parcelable.Creator<ArtistSearchResults>() {
        public ArtistSearchResults createFromParcel(Parcel in) {
            return new ArtistSearchResults(in);
        }

        public ArtistSearchResults[] newArray(int size) {
            return new ArtistSearchResults[size];
        }
    };
    private String[] trackNames;
    private String[] albumNames;
    private String[] thumbNamesLarge;
    private String[] thumbNamesSmall;
    private String[] playurls;

    ArtistSearchResults(String[] trackNames, String[] albumNames, String[] thumbNamesLarge, String[] thumbNamesSmall, String[] playurls) {
        this.trackNames = trackNames;
        this.albumNames = albumNames;
        this.thumbNamesLarge = thumbNamesLarge;
        this.thumbNamesSmall = thumbNamesSmall;
        this.playurls = playurls;
    }

    private ArtistSearchResults(Parcel in) {
    }

    public String[] getTrackNames() {
        return trackNames;
    }

    public void setTrackNames(String[] trackNames) {
        this.trackNames = trackNames;
    }

    public String[] getThumbNamesLarge() {
        return thumbNamesLarge;
    }

    public void setThumbNamesLarge(String[] thumbNamesLarge) {
        this.thumbNamesLarge = thumbNamesLarge;
    }

    public String[] getAlbumNames() {
        return albumNames;
    }

    public void setAlbumNames(String[] albumNames) {
        this.albumNames = albumNames;
    }

    public String[] getThumbNamesSmall() {
        return thumbNamesSmall;
    }

    public void setThumbNamesSmall(String[] thumbNamesSmall) {
        this.thumbNamesSmall = thumbNamesSmall;
    }

    public String[] getPlayurls() {
        return playurls;
    }

    public void setPlayurls(String[] playurls) {
        this.playurls = playurls;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}