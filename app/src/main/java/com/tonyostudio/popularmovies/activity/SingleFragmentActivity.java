package com.tonyostudio.popularmovies.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.tonyostudio.popularmovies.R;

/**
 * Created by tonyofrancis on 3/22/16.
 * An Abstract class that can be extended by an activity class
 * that will only hold a single fragment.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @LayoutRes
    protected int getLayoutId() {
        return R.layout.main_activity_layout;
    }

    @IdRes
    private int getFragmentId() {
        return R.id.fragment_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        /*Commit a new instance of fragment and add it to
        * the Activity when it is created*/
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(getFragmentId()) == null) {
            fragmentManager.beginTransaction()
                    .add(getFragmentId(), createFragment())
                    .commit();
        }
    }
}
