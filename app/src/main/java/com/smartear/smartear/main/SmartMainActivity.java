package com.smartear.smartear.main;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;

import com.smartear.smartear.BaseActivity;
import com.smartear.smartear.R;
import com.smartear.smartear.databinding.ActivitySmartMainBinding;
import com.smartear.smartear.main.fragments.BionicFragment;
import com.smartear.smartear.main.fragments.EqFragment;
import com.smartear.smartear.main.fragments.PlayerFragment;
import com.smartear.smartear.main.fragments.SettingsFragment;
import com.smartear.smartear.main.fragments.TopPanelFragment;
import com.smartear.smartear.main.viewmodel.SmartMainModel;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 14.12.2015
 */
public class SmartMainActivity extends BaseActivity implements TopPanelFragment.TopPanelListener {
    private ActivitySmartMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_smart_main);
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

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.eqContainer, EqFragment.newInstance())
                .commitAllowingStateLoss();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.smartMainButtons, TopPanelFragment.newInstance())
                .commitAllowingStateLoss();

    }

    private void hideEqPanel() {
        binding.eqPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    private void showEqPanel() {
        binding.eqPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    @Override
    public void onBackPressed() {
        if (SlidingUpPanelLayout.PanelState.EXPANDED.equals(binding.eqPanel.getPanelState())) {
            hideEqPanel();
            return;
        }
        super.onBackPressed();

    }

    @Override
    public void showPlayer() {
        if (!(getLastBottomFragment() instanceof PlayerFragment)) {
            showBottomFragment(PlayerFragment.newInstance(), PlayerFragment.TAG);
        }
    }

    public void showBottomFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss();
    }

    public Fragment getLastBottomFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
    }

    @Override
    public void showSmartEarSettings() {
        if (!(getLastBottomFragment() instanceof BionicFragment)) {
            showBottomFragment(BionicFragment.newInstance(), BionicFragment.TAG);
        }
    }

    @Override
    public void showGeneralSettings() {
        if (!(getLastBottomFragment() instanceof SettingsFragment)) {
            showBottomFragment(SettingsFragment.newInstance(), SettingsFragment.TAG);
        }
    }
}
