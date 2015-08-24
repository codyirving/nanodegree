package com.codyirivng.nanodegree;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;

public class MainActivity extends Activity {
    static MainFragment mainFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFragment = MainFragment.newInstance(); //empty arguments for future params
        getFragmentManager().beginTransaction().replace(R.id.layout1, mainFragment, "mainFragment").commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mainFragment != null && getFragmentManager().findFragmentByTag("mainFragment") != null) {
            getFragmentManager().putFragment(outState, "mainFragment", mainFragment);
        }
    }
}

