package com.example.internalchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter_2 extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList<>();

    public ViewPagerAdapter_2(@NonNull FragmentManager fm) {
        super(fm);
        fragmentList.add(new Dang_Fragment1());
        fragmentList.add(new Dang_Fragment2());
        fragmentList.add(new Dang_Fragment3());
        fragmentList.add(new Dang_Fragment4());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        // Trả về tiêu đề của Fragment tương ứng với vị trí
        switch (position) {
            case 0:
                return "fragment_dang1";
            case 1:
                return "fragment_dang2";
            case 2:
                return "fragment_dang3";
            case 3:
                return "fragment_dang4";
            default:
                return "";
        }
    }
}






