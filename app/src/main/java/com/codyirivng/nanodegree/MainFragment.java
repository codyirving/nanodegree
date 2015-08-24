package com.codyirivng.nanodegree;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        android.app.ActionBar actionBar = getActivity().getActionBar();
        if(actionBar !=null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Nanodegree Apps");
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        Button buttonSpotify = (Button) v.findViewById(R.id.buttonSpotifyStreamer);
        buttonSpotify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onButtonPressed(null);
            }
        });
        Button buttonScores = (Button) v.findViewById(R.id.buttonScoresApp);
        buttonScores.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CharSequence toastText = "This button will launch: Scores App!";
                Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
            }
        });
        Button buttonLibrary = (Button) v.findViewById(R.id.buttonLibraryApp);
        buttonLibrary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CharSequence toastText = "This button will launch: Library App!";
                Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
            }
        });
        Button buttonBuild = (Button) v.findViewById(R.id.buttonBuildItBigger);
        buttonBuild.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CharSequence toastText = "This button will launch: Build IT BIGGER App!";
                Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
            }
        });
        Button buttonXYZ = (Button) v.findViewById(R.id.buttonXYZReader);
        buttonXYZ.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CharSequence toastText = "This button will launch: XYZ Reader App!";
                Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
            }
        });
        Button buttonMyOwn = (Button) v.findViewById(R.id.buttonMyOwnApp);
        buttonMyOwn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CharSequence toastText = "This button will launch: MY APP!";
                Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        //just one case for now
        Intent spotifyActvityIntent = new Intent(getActivity(), SpotifyActivity.class);
        startActivity(spotifyActvityIntent);

    }


}
