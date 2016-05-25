package com.smartear.smartear.wechat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartear.smartear.R;
import com.smartear.smartear.wechat.RecognizedState;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeChatMeetingFragment extends WeChatBaseFragment {
    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wechat_meeting_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView dayOfWeek = (TextView) view.findViewById(R.id.dayofweek);
        TextView meetingDay = (TextView) view.findViewById(R.id.meetingDate);
        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        dayOfWeek.setText(getString(R.string.today, simpleDateFormat.format(today)));
        meetingDay.setText(new SimpleDateFormat("dd MMM, yyyy").format(today));

        sayText(RecognizedState.MEETING);
    }
}
