package com.smartear.smartear.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

        binding.eqPanel.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(SlidingUpPanelLayout.PanelState.EXPANDED.equals(binding.eqPanel.getPanelState())){
                    binding.eqTransparentPanel.setAlpha(1f);
                }
            }
        }, 100);

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
        if (binding.fragmentViewPager.getAdapter() == null) {
            binding.fragmentViewPager.setAdapter(new FragmentBottomAdapter(getChildFragmentManager()));
            binding.fragmentViewPager.setOffscreenPageLimit(3);
            binding.fragmentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    getTopPanelFragment().setActiveButton(position);
                    if (position == 2) {
                        hideEqPanel();
                        binding.eqFab.setVisibility(View.GONE);
                    } else {
                        binding.eqFab.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private TopPanelFragment getTopPanelFragment() {
        return ((TopPanelFragment) getChildFragmentManager().findFragmentById(R.id.smartMainButtons));
    }

    private void hideEqPanel() {
        binding.eqPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        binding.eqTransparentPanel.animate().alpha(0).start();
    }

    private void showEqPanel() {
        binding.eqPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        binding.eqTransparentPanel.animate().alpha(1f).start();
    }

    @Override
    public void showPlayer() {
        binding.fragmentViewPager.setCurrentItem(0, true);
    }


    @Override
    public void showSmartEarSettings() {
        binding.fragmentViewPager.setCurrentItem(1, true);
    }

    @Override
    public void showGeneralSettings() {
        binding.fragmentViewPager.setCurrentItem(2, true);
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

    public class FragmentBottomAdapter extends FragmentPagerAdapter {

        public FragmentBottomAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PlayerFragment.newInstance();
                case 1:
                    return BionicFragment.newInstance();
                case 2:
                    return SettingsFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
