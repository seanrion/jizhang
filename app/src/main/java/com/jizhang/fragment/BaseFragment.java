package com.jizhang.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {
    Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getResId(), container, false);
        unbinder = ButterKnife.bind(getSubFragment(), view);
        return view;
    }

    protected Fragment getSubFragment() {
        return this;
    }

    protected abstract int getResId();

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}