package com.codyirivng.nanodegree;


import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;


public class SpotifyActivity extends Activity implements SpotifyActivityFragment.OnFragmentInteractionListener, ArtistFragment.OnFragmentInteractionListener, SpotifyStreamFragment.OnFragmentInteractionListener {

    static ArtistFragment artistFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);
        if(findViewById(R.id.artist_search_container) != null) {  //if not null, using 2 pane mode
            if (savedInstanceState == null) {
                ArtistFragment spotifyArtistFragment = ArtistFragment.newInstance("");
                getFragmentManager().beginTransaction().replace(R.id.artist_search_container, spotifyArtistFragment, "spotifyArtistFragment").commit();
            }
        }else { //single fragment case
            if (savedInstanceState == null) {
                SpotifyActivityFragment spotifyActivityFragment = SpotifyActivityFragment.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.activity_spotify_layout, spotifyActivityFragment, "spotifyActivityFragment").commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_spotify, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }
    @Override
    public void onFragment2Interaction(String artistID) {

        artistFragment = ArtistFragment.newInstance(artistID);
        if(findViewById(R.id.artist_search_container) !=null ) {
            getFragmentManager().beginTransaction().replace(R.id.artist_search_container, artistFragment, "artistFragment").commit();
        }else{
            getFragmentManager().beginTransaction().replace(R.id.activity_spotify_layout, artistFragment, "artistFragment").addToBackStack("artistFragment").commit();
        }

    }

    @Override
    public void onFragment3Interaction(String[] trackUrlStrings, String[] thumbUrlStrings, String[] albumNameStrings, String[] trackNameStrings, int position, boolean showAsDialog) {

            if(showAsDialog) {

                DialogFragment spotifyStreamFragment = SpotifyStreamFragment.newInstance(trackUrlStrings, thumbUrlStrings, albumNameStrings, trackNameStrings, position, showAsDialog);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                spotifyStreamFragment.show(ft, "spotifyStreamFragment");
            }else{
                Intent spotifyStreamActivityIntent = new Intent(this, StreamActivity.class);
                spotifyStreamActivityIntent.putExtra("trackUrlStrings", trackUrlStrings);
                spotifyStreamActivityIntent.putExtra("thumbUrlStrings", thumbUrlStrings);
                spotifyStreamActivityIntent.putExtra("albumNameStrings", albumNameStrings);
                spotifyStreamActivityIntent.putExtra("trackNameStrings", trackNameStrings);
                spotifyStreamActivityIntent.putExtra("position", position);
                spotifyStreamActivityIntent.putExtra("showAsDialog", showAsDialog);
                startActivity(spotifyStreamActivityIntent);
            }
    }

    @Override
    public void onFragment4Interaction(int newPosition) {

    }
}
