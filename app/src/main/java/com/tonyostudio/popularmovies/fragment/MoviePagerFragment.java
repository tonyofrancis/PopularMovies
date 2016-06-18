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
public final class MoviePagerFragment extends Fragment {

    public static MoviePagerFragment newInstance() {
        return new MoviePagerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        final View view = inflater.inflate(R.layout.fragment_movie_list_view_pager,container,false);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.movie_view_pager);
        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.movie_tab_layout);

        /*Setup ViewPager with a state adapter. The adapter saves the state
        * of the fragments*/
        viewPager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                //Get new instance of the view pager fragments
                switch (position) {
                    case 0:
                        return PopularMovieListFragment.newInstance();
                    case 1:
                        return TopRatedMovieListFragment.newInstance();
                    case 2:
                        return FavoriteMovieListFragment.newInstance();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {

                switch (position) {
                    case 0: return getString(R.string.most_popular_label);
                    case 1: return getString(R.string.top_rated_label);
                    case 2: return getString(R.string.favorite_label);
                    default: return null;
                }
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                return super.instantiateItem(container,position);
            }
        });

        /*Setup TabLayout with ViewPager*/
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }


}
