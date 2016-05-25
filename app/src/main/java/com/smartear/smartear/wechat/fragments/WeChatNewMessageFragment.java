package com.smartear.smartear.wechat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartear.smartear.R;

public class WeChatNewMessageFragment extends WeChatBaseFragment {
    private static final String ARG_URL = "arg_url";

    @Override
    public String getTitle() {
        return null;
    }

    public static WeChatNewMessageFragment newInstance(String url) {

        Bundle args = new Bundle();

        args.putString(ARG_URL, url);
        WeChatNewMessageFragment fragment = new WeChatNewMessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wechat_new_message_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWeChatActivity().playUrl(getArguments().getString(ARG_URL));
            }
        });
    }
}
