package com.hc.zhdaily.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hc.zhdaily.R;

/**
 * Created by Administrator on 2016-07-14.
 */
public class BaseFragment extends Fragment {
    protected View view;

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_datalist, container, false);
        return view;
    }
}
