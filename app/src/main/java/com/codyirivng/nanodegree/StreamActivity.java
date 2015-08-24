package com.codyirivng.nanodegree;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class StreamActivity extends Activity implements SpotifyStreamFragment.OnFragmentInteractionListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_stream);
        Intent intent = getIntent();
        String[] trackUrlStrings = intent.getStringArrayExtra("trackUrlStrings");
        String[] thumbUrlStrings = intent.getStringArrayExtra("thumbUrlStrings");
        String[] albumNameStrings = intent.getStringArrayExtra("albumNameStrings");
        String[] trackNameStrings = intent.getStringArrayExtra("trackNameStrings");
        int position = intent.getIntExtra("position",1);

        if (savedInstanceState == null) {

            boolean showAsDialog = false;
            if(findViewById(R.id.artist_search_container) !=null )  showAsDialog = true;
            SpotifyStreamFragment spotifyStreamFragment = SpotifyStreamFragment.newInstance(trackUrlStrings, thumbUrlStrings, albumNameStrings, trackNameStrings, position, showAsDialog);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.fragment_stream_placeholder, spotifyStreamFragment, "spotifyStreamFragment").commit();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_stream, menu);
        android.app.ActionBar actionBar = getActionBar();
        if(actionBar !=null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Track Preview");
            actionBar.setIcon(R.drawable.spotifylogo);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragment4Interaction(int newPosition) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
