package com.tonyostudio.popularmovies.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tonyostudio.popularmovies.R;

/**
 * Created by tonyofrancis on 3/30/16.
 * The MoviePagerFragment implements a ViewPager and
 * a TabLayout to display Popular & Top Rated Movies
 * to the user.
 */
public class MoviePagerFragment extends Fragment {

    private MovieListBaseFragment[] mFragments;

    public static MoviePagerFragment newInstance() {
        return new MoviePagerFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Populate fragments for pager to display*/
        mFragments = new MovieListBaseFragment[]{
          PopularMovieListFragment.newInstance(getActivity()),
          TopRatedMovieListFragment.newInstance(getActivity())};
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_list_view_pager,container,false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.movie_view_pager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.movie_tab_layout);

        /*Setup ViewPager with a state adapter. The adapter saves the state
        * of the fragments*/
        viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragments[position].getTitle();
            }
        });

        /*Setup TabLayout with ViewPager*/
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
