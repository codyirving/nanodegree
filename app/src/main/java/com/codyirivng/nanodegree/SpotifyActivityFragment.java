package com.codyirivng.nanodegree;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.net.URL;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SpotifyActivityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SpotifyActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SpotifyActivityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    SearchResults SR;
    ListView spotifyList;
    int listPosition = -1;
    String artistString;
    private OnFragmentInteractionListener mListener;
    private View rootView;

    public SpotifyActivityFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SpotifyActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SpotifyActivityFragment newInstance() {
        SpotifyActivityFragment fragment = new SpotifyActivityFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("SR", SR);
        if (spotifyList == null) outState.putInt("ListPosition", listPosition);
        else outState.putInt("ListPosition", spotifyList.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            SR = savedInstanceState.getParcelable("SR");
            listPosition = savedInstanceState.getInt("ListPosition");
            setResultsList(SR);
        }
    }

    void setResultsList(final SearchResults data) {

        if (data != null && data.getArtistName() != null && data.getArtistThumb() != null) {
            spotifyList = (ListView) rootView.findViewById(R.id.listViewSpotify);
            SpotifyArrayAdapter mListAdapter = new SpotifyArrayAdapter(getActivity(), R.layout.list_view_spotify, R.id.list_item_spotify_textview, data.getArtistName(), data.getArtistThumb(), data.getArtistID());
            spotifyList.setAdapter(mListAdapter);
            if (listPosition != -1) {
                spotifyList.setSelection(listPosition);
            }
            spotifyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String artistID = data.getArtistID()[position];
                    mListener.onFragment2Interaction(artistID);
                }

            });

        }

    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_spotify_search, container, false);
        // seems hacky to make sure listview is populated across different devices
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setResultsList(SR);
        } else {

            if (savedInstanceState != null) {
                SR = savedInstanceState.getParcelable("SR");
                listPosition = savedInstanceState.getInt("ListPosition");
                setResultsList(SR);
            } else {
                setResultsList(SR);
            }
        }

        final EditText searchString = (EditText) rootView.findViewById(R.id.editTextSearchSpotify);
        searchString.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    artistString = searchString.getText().toString();
                    try {
                        SpotifySearch query = new SpotifySearch(artistString);
                        query.setResultListener(new SpotifySearch.ResultsListener() {
                            @Override
                            public void resultsSuccess(SearchResults searchResults) {
                                SR = searchResults;
                                System.out.println("search results: " + SR);
                                setResultsList(searchResults);
                            }

                            @Override
                            public void resultsFail() {
                                System.out.println("Results failed.");
                            }
                        });
                        query.execute();
                    } catch (Exception e) {

                        Log.d("error", e.toString());
                    }
                }
                return false;
            }
        });
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        android.app.ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setIcon(R.drawable.spotifylogo);
            actionBar.setTitle("Search Artists");
        }
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
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
        void onFragment2Interaction(String string);
    }
}

class SpotifySearch extends AsyncTask<Object[], Void, Object[]> {
    ResultsListener resultListener;
    String artist;

    SpotifySearch(String artist) {
        this.artist = artist;
    }

    public void setResultListener(ResultsListener resultListener) {
        this.resultListener = resultListener;
    }

    @Override
    protected Object[] doInBackground(Object[]... params) {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        ArtistsPager results;
        try {
            results = spotify.searchArtists(artist);
        } catch (retrofit.RetrofitError retrofitError) {
            results = null;
        }
        if (results != null) {
            int resultsLimitOrTotal = (results.artists.limit > results.artists.total) ? results.artists.total : results.artists.limit;
            if (resultsLimitOrTotal == 0) return null;
            String[] idArray = new String[resultsLimitOrTotal];
            String[] thumbArray = new String[resultsLimitOrTotal];
            String[] spotifyIdArray = new String[resultsLimitOrTotal];
            for (int i = 0; i < resultsLimitOrTotal; i++) {
                idArray[i] = results.artists.items.get(i).name;
                spotifyIdArray[i] = results.artists.items.get(i).id;
                if (results.artists.items.get(i).images.size() > 0) {
                    thumbArray[i] = results.artists.items.get(i).images.get(results.artists.items.get(i).images.size() - 1).url;
                }
            }
            return new Object[]{idArray, spotifyIdArray, thumbArray};
        } else {
            return null;
        }

    }

    @Override
    protected void onPostExecute(Object[] results) {
        if (results != null) {
            SearchResults searchResults = new SearchResults((String[]) results[0], (String[]) results[1], (String[]) results[2]);
            resultListener.resultsSuccess(searchResults);
        } else {
            Toast.makeText(MainActivity.mainFragment.getActivity(), "Sorry, no results found. Please enter refine your search", Toast.LENGTH_LONG).show();
            resultListener.resultsFail();
        }
    }

    public interface ResultsListener {
        void resultsSuccess(SearchResults SR);
        void resultsFail();
    }
}

class SpotifyArrayAdapter extends ArrayAdapter<Object> {
    private final Context context;
    private final Object[] values2;
    Object[] values1;
    Object[] values3;

    public SpotifyArrayAdapter(Context context, int i, int j, Object[] values1, Object[] values2, Object[] values3) {
        super(context, i, j, values1);
        this.context = context;
        this.values1 = values1;
        this.values2 = values2;
        this.values3 = values3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (values2[position] != null) {

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View rowView = inflater.inflate(R.layout.list_view_spotify, parent, false);
                convertView = rowView;

            }

            ImageView artistThumb = (ImageView) convertView.findViewById(R.id.artist_image_imageview);
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

class SearchResults implements Parcelable {
    public static final Parcelable.Creator<SearchResults> CREATOR
            = new Parcelable.Creator<SearchResults>() {
        public SearchResults createFromParcel(Parcel in) {
            return new SearchResults(in);
        }

        public SearchResults[] newArray(int size) {
            return new SearchResults[size];
        }
    };
    String[] artistName;
    String[] artistID;
    String[] artistThumb;

    SearchResults(String[] aName, String[] aID, String[] aThumb) {
        this.artistName = aName;
        this.artistID = aID;
        this.artistThumb = aThumb;
    }

    SearchResults(Parcel in) {
    }

    public String[] getArtistName() {
        return artistName;
    }

    public void setArtistName(String[] artistName) {
        this.artistName = artistName;
    }

    public String[] getArtistThumb() {
        return artistThumb;
    }

    public void setArtistThumb(String[] artistThumb) {
        this.artistThumb = artistThumb;
    }

    public String[] getArtistID() {
        return artistID;
    }

    public void setArtistID(String[] artistID) {
        this.artistID = artistID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}