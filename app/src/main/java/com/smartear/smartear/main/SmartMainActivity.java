package com.smartear.smartear.main;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionInflater;
import android.view.View;

import com.smartear.smartear.R;
import com.smartear.smartear.databinding.ActivitySmartMainBinding;
import com.smartear.smartear.fragment.BaseFragment;
import com.smartear.smartear.main.fragments.BaseSmartFragment;
import com.smartear.smartear.main.fragments.SmartMainFragment;
import com.smartear.smartear.main.viewmodel.SmartMainModel;
import com.smartear.smartear.utils.MediaPlayerHelper;
import com.smartear.smartear.utils.commands.CommandHelper;
import com.smartear.smartear.utils.firebase.MessageHelper;
import com.smartear.smartear.voice.VoiceRecognizer;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 14.12.2015
 */
public class SmartMainActivity extends AppCompatActivity implements MessageHelper.OnNewMessageListener {
    private ActivitySmartMainBinding binding;
    private CommandHelper commandHelper;

    private MessageHelper messageHelper = new MessageHelper();
    VoiceRecognizer voiceRecognizer;
    MediaPlayerHelper mediaPlayerHelper = new MediaPlayerHelper();

    public MessageHelper getMessageHelper() {
        return messageHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commandHelper = new CommandHelper(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_smart_main);
        SmartMainModel smartMainModel = new SmartMainModel();
        binding.setData(smartMainModel);
        binding.samsungStatusBarFix.setVisibility((isSamsung() && isKitkatAndAbove()) ? View.VISIBLE : View.GONE);
        if (savedInstanceState == null) {
            replaceFragment(SmartMainFragment.newInstance(), false);
            voiceRecognizer = new VoiceRecognizer();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.voiceContainer, voiceRecognizer)
                    .commit();
        } else {
            voiceRecognizer = (VoiceRecognizer) getSupportFragmentManager().findFragmentById(R.id.voiceContainer);
        }

        messageHelper.addNewMessageListener(this);
    }

    private boolean isSamsung() {
        return Build.MANUFACTURER.toLowerCase().contains("samsung");
    }

    private boolean isKitkatAndAbove() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public void replaceFragment(BaseSmartFragment fragment, boolean addToBackStack) {
        Bundle arguments;
        if (fragment.getArguments() == null) {
            arguments = new Bundle();
        } else {
            arguments = fragment.getArguments();
        }
        arguments.putBoolean(BaseFragment.ADD_TO_BACK_STACK, addToBackStack);
        fragment.setArguments(arguments);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 21) {
            fragment.setEnterTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.slide_right));
            fragment.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.fade));
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        }
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getFragmentTag());
        }
        fragmentTransaction.replace(R.id.fragmentContainer, fragment, fragment.getFragmentTag());
        fragmentTransaction.commitAllowingStateLoss();
    }

    public VoiceRecognizer getVoiceRecognizer() {
        return voiceRecognizer;
    }

    private BaseSmartFragment getLastFragment() {
        return (BaseSmartFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
    }

    @Override
    public void onBackPressed() {
        BaseSmartFragment last = getLastFragment();
        if (last != null && last.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    public CommandHelper getCommandHelper() {
        return commandHelper;
    }

    @Override
    public void onNewMessage(final String url) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.newMessage)
                .setPositiveButton(R.string.play, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mediaPlayerHelper.startPlaying(url);
                    }
                }).show();
    }
}
