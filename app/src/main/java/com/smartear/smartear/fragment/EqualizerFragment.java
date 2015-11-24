package com.smartear.smartear.fragment;

import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.smartear.smartear.R;
import com.smartear.smartear.databinding.FragmentEqualizerBinding;
import com.smartear.smartear.widget.VisualizerView;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 24.11.2015
 */
public class EqualizerFragment extends Fragment {
    private static final String TAG = "EqualizerFragment";
    FragmentEqualizerBinding binding;
    Equalizer equalizer;
    BassBoost bassBoost;
    private Visualizer visualizer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEqualizerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public static EqualizerFragment newInstance() {
        Bundle args = new Bundle();
        EqualizerFragment fragment = new EqualizerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initVisualizer() {
        binding.visualizerContainer.removeAllViews();
        final VisualizerView visualizerView = new VisualizerView(getActivity());
        visualizerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.visuzlizerHeight)));
        binding.visualizerContainer.addView(visualizerView);
        visualizer = new Visualizer(0);
        visualizer.setEnabled(false);
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        visualizer.setEnabled(true);
        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                visualizerView.updateVisualizer(waveform);
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        equalizer = new Equalizer(0, 0);
        bassBoost = new BassBoost(0, 0);
        initBandLevels();
        initVisualizer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (visualizer != null)
            visualizer.release();
        if (equalizer != null)
            equalizer.release();
    }

    private void initBandLevels() {
        Short level = equalizer.getNumberOfBands();
        Short[] bandsList = new Short[level];
        for (short i = 0; i < level; i++) {
            bandsList[i] = equalizer.getBandLevel(i);
        }
        final short minEQLevel = equalizer.getBandLevelRange()[0];
        final short maxEQLevel = equalizer.getBandLevelRange()[1];
        String[] bandStringArray = getResources().getStringArray(R.array.bandLevels);
        binding.equalizerSeekBars.removeAllViews();
        for (int i = 0; i < bandsList.length; i++) {
            Short band = bandsList[i];

            LinearLayout seekbarLinear = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.equalizer_seekbar, binding.equalizerSeekBars, false);
            seekbarLinear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView freqTextView = (TextView) seekbarLinear.findViewById(R.id.equalizerHZ);
            freqTextView.setText(bandStringArray[i]);

            TextView minDbTextView = (TextView) seekbarLinear.findViewById(R.id.equalizerMin);
            minDbTextView.setText(getString(R.string.db, minEQLevel / 100));

            TextView maxDbTextView = (TextView) seekbarLinear.findViewById(R.id.equalizerMax);
            maxDbTextView.setText(getString(R.string.db, maxEQLevel / 100));

            SeekBar bar = (SeekBar) seekbarLinear.findViewById(R.id.equalizerSeek);
            bar.setMax(maxEQLevel - minEQLevel);
            bar.setProgress((maxEQLevel - minEQLevel) / 2 + equalizer.getBandLevel(band));
            binding.equalizerSeekBars.addView(seekbarLinear);
        }

        binding.bassBoost.setMax(200);
        binding.bassBoost.setProgress(bassBoost.getRoundedStrength() / 5);
    }
}
