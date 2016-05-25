package com.smartear.smartear.wechat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartear.smartear.R;
import com.smartear.smartear.wechat.RecognizedState;

public class WeChatAuthFragment extends WeChatBaseFragment {
    private static final String TAG = "WeChatAuthFragment";

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wechat_morning_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sayText(RecognizedState.AUTH);
    }
}
