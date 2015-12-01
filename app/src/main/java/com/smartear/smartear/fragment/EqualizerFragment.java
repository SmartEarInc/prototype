package com.smartear.smartear.fragment;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.smartear.smartear.R;
import com.smartear.smartear.databinding.FragmentEqualizerBinding;
import com.smartear.smartear.services.EqService;
import com.smartear.smartear.widget.VisualizerView;


/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 24.11.2015
 */
public class EqualizerFragment extends BaseFragment {
    private static final String TAG = "EqualizerFragment";
    private static final int MAX_BASS_BOOST = 1000;
    FragmentEqualizerBinding binding;
    private Visualizer visualizer;
    private boolean bound = false;
    private EqService eqService;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bound = true;
            eqService = ((EqService.EqBinder) service).getService();
            initEqualizerUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    private void initEqualizerUI() {
        if (eqService == null)
            return;
        initBandLevels();
        initVisualizer();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEqualizerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEqService();
    }

    private void initEqService() {
        EqService.start(getActivity());
        EqService.bind(getActivity(), serviceConnection);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (bound) {
            getActivity().unbindService(serviceConnection);
            bound = false;
        }
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
//        visualizer.setEnabled(false);
        visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
                visualizerView.updateVisualizer(waveform);
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
        visualizer.setEnabled(true);
    }


    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public String getTitle() {
        return getString(R.string.equalizer);
    }

    @Override
    public void onResume() {
        super.onResume();
        initEqualizerUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (visualizer != null)
            visualizer.release();
    }

    private void initBandLevels() {
        final Equalizer equalizer = eqService.getEqualizer();
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
            final Short band = bandsList[i];

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
            try {
                bar.setProgress((maxEQLevel - minEQLevel) / 2 + equalizer.getBandLevel((short) i));
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            final int finalI = i;
            bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        try {
                            equalizer.setBandLevel((short) finalI, (short) (progress - (maxEQLevel - minEQLevel) / 2));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            binding.equalizerSeekBars.addView(seekbarLinear);
        }
        try {
            final BassBoost bassBoost = eqService.getBassBoost();
            BassBoost.Settings bassSettings = bassBoost.getProperties();
            BassBoost.Settings newSettings = new BassBoost.Settings(bassSettings.toString());
            newSettings.strength = MAX_BASS_BOOST;
            bassBoost.setProperties(newSettings);
            binding.bassBoost.setMax(MAX_BASS_BOOST);
            binding.bassBoost.setProgress(bassBoost.getRoundedStrength() / 5);
            binding.bassBoost.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        bassBoost.setStrength((short) progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        } catch (Exception e) {
            binding.bassBoost.setEnabled(false);
        }
    }
}
