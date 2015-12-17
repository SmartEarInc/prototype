package com.smartear.smartear.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartear.smartear.R;
import com.smartear.smartear.databinding.FragmentSmartMainBinding;
import com.smartear.smartear.main.viewmodel.SmartMainModel;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 17.12.2015
 */
public class SmartMainFragment extends BaseSmartFragment implements TopPanelFragment.TopPanelListener {
    private static final String TAG = "SmartMainFragment";
    FragmentSmartMainBinding binding;

    public static SmartMainFragment newInstance() {

        Bundle args = new Bundle();

        SmartMainFragment fragment = new SmartMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSmartMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SmartMainModel smartMainModel = new SmartMainModel();
        binding.setData(smartMainModel);
        binding.eqPanel.setDragView(binding.eqFab);
        binding.eqPanel.setTouchEnabled(false);
        binding.eqPanel.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelCollapsed(View panel) {

            }

            @Override
            public void onPanelExpanded(View panel) {

            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });
        binding.eqFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SlidingUpPanelLayout.PanelState.EXPANDED.equals(binding.eqPanel.getPanelState())) {
                    showEqPanel();
                } else {
                    hideEqPanel();
                }
            }
        });

        getChildFragmentManager().beginTransaction()
                .replace(R.id.eqContainer, EqFragment.newInstance())
                .commitAllowingStateLoss();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.smartMainButtons, TopPanelFragment.newInstance())
                .commitAllowingStateLoss();
    }

    private void hideEqPanel() {
        binding.eqPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    private void showEqPanel() {
        binding.eqPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void showBottomFragment(Fragment fragment, String tag) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss();
    }

    public Fragment getLastBottomFragment() {
        return getChildFragmentManager().findFragmentById(R.id.fragmentContainer);
    }

    @Override
    public void showPlayer() {
        if (!(getLastBottomFragment() instanceof PlayerFragment)) {
            showBottomFragment(PlayerFragment.newInstance(), PlayerFragment.TAG);
            binding.eqFab.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void showSmartEarSettings() {
        if (!(getLastBottomFragment() instanceof BionicFragment)) {
            showBottomFragment(BionicFragment.newInstance(), BionicFragment.TAG);
            binding.eqFab.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showGeneralSettings() {
        if (!(getLastBottomFragment() instanceof SettingsFragment)) {
            showBottomFragment(SettingsFragment.newInstance(), SettingsFragment.TAG);
            binding.eqFab.setVisibility(View.GONE);
            hideEqPanel();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (SlidingUpPanelLayout.PanelState.EXPANDED.equals(binding.eqPanel.getPanelState())) {
            hideEqPanel();
            return true;
        }
        return false;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}
