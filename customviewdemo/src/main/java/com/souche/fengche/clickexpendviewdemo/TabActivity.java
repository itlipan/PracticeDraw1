package com.souche.fengche.clickexpendviewdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.souche.fengche.clickexpendviewdemo.tab.TabFragment;
import com.souche.fengche.clickexpendviewdemo.tab.widget.ReDefinedTabLayout;

public class TabActivity extends AppCompatActivity {

    private final String[] tabs = new String[]{"AAA", "BBBBBBBBBB", "C", "DD", "EEEEE", "FFFF"};

    private ViewPager mViewPager;
    private ReDefinedTabLayout mTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        mViewPager = findViewById(R.id.pager);
        mTab = findViewById(R.id.tab);
        initViewPagerAdapter();
    }

    private void initViewPagerAdapter() {
        mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        mTab.setupWithViewPager(mViewPager);
    }

    private final class TabPagerAdapter extends FragmentPagerAdapter {

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TabFragment.getInstance();
        }

        @Override
        public int getCount() {
            return tabs.length;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

}
