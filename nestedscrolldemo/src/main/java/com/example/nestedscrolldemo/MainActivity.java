package com.example.nestedscrolldemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.nestedscrolldemo.view.RedCircleTextView;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    RedCircleTextView mRedCircleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewState();

    }

    private void initViewState() {
        mViewPager = (ViewPager) findViewById(R.id.vp_tab);
        mRedCircleTextView = (RedCircleTextView) findViewById(R.id.tv_content);
        mRedCircleTextView.drawRedCircle(100);
        final int length = 3;
        final Fragment[] mFragments = new Fragment[length];
        for (int i = 0; i < mFragments.length; i++) {
            mFragments[i] = new TabFragment();
        }
        PagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return length;
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
        };
        mViewPager.setAdapter(mAdapter);
    }
}
