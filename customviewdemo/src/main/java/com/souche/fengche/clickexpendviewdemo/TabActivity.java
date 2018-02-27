package com.souche.fengche.clickexpendviewdemo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.souche.fengche.clickexpendviewdemo.tab.TabFragment;
import com.souche.fengche.clickexpendviewdemo.tab.widget.ReDefinedTabLayout;

public class TabActivity extends AppCompatActivity {

    private final String[] tabs = new String[]{"AAA", "BBBBBBBBBB", "C", "DD"};

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

    /**
     *  ViewPager 与  TabLayout 放置层次问题,可能导致 Tab 点击无效,ViewPager 拦截了 Tab 的点击事件
     */
    private void initViewPagerAdapter() {
        mViewPager.setAdapter(new TabPagerAdapter(getSupportFragmentManager()));
        for (String tabInfo : tabs) {
            ReDefinedTabLayout.Tab tab = mTab.newTab();
            tab.setText(tabInfo);
            mTab.addTab(tab);
        }
        mTab.addOnTabSelectedListener(new ReDefinedTabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.setOnPageChangeListener(new ReDefinedTabLayout.TabLayoutOnPageChangeListener(mTab));
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
