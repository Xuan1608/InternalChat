package com.example.internalchat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter_1 extends FragmentStatePagerAdapter {

    public ViewPagerAdapter_1(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OnbroardingFragment1();
            case 1:
                return new OnbroardingFragment2();
            case 2:
                return new OnbroardingFragment3();
            default:
                return new OnbroardingFragment1();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
