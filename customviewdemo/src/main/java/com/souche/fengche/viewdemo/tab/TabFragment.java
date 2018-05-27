package com.souche.fengche.viewdemo.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.souche.fengche.viewdemo.R;

/**
 * Created by Lee on 2018/1/28.
 */

public class TabFragment extends Fragment {

    public static TabFragment getInstance() {
        return new TabFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_view, container, false);
    }
}
