package com.udacity.myappportfolio.fragment;

import android.app.Fragment;

import butterknife.ButterKnife;

public class BaseFragment extends Fragment{

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.unbind(this);
    }
}
