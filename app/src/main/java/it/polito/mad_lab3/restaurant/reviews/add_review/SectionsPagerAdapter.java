package it.polito.mad_lab3.restaurant.reviews.add_review;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import it.polito.mad_lab3.R;
import it.polito.mad_lab3.data.restaurant.ReviewFood;

/**
 * Created by Giovanna on 09/05/2016.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    Context context;
    ArrayList<ArrayList<ReviewFood>> data;

    public SectionsPagerAdapter(FragmentManager fm, Context context, ArrayList<ArrayList<ReviewFood>> data) {
        super(fm);
        this.context=context;
        this.data=data;

    }


    @Override
    public Fragment getItem(int position) {

            return SectionFragment.newInstance(position, context, data.get(position));

    }

    @Override
    public int getCount() {
        return 5;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.offers);
            case 1:
                return context.getResources().getString(R.string.first);
            case 2:
                return context.getResources().getString(R.string.second);
            case 3:
                return context.getResources().getString(R.string.dessert);
            case 4:
                return context.getResources().getString(R.string.other);
        }
        return null;
    }
}