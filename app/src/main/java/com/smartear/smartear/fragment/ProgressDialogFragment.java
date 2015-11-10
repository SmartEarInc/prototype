package com.smartear.smartear.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.smartear.smartear.R;


/**
 * Created by Belozerov on 05.02.2015.
 */
public class ProgressDialogFragment extends DialogFragment {

    private static final String ARG_MESSAGE = "message";
    private static final String TAG = "progress_dialog_fragment";

    public static void show(Activity activity) {
        DialogFragment progressDialog =
                (DialogFragment) activity.getFragmentManager().findFragmentByTag(TAG);
        if (progressDialog == null) {
            progressDialog = create();
            progressDialog.setCancelable(false);
            progressDialog.show(activity.getFragmentManager(), TAG);
        }
    }

    public static void showCancelable(Activity activity) {
        DialogFragment progressDialog =
                (DialogFragment) activity.getFragmentManager().findFragmentByTag(TAG);
        if (progressDialog == null) {
            progressDialog = create();
            progressDialog.show(activity.getFragmentManager(), TAG);
        }
    }

    public static void hide(Activity activity) {
        DialogFragment progressDialog =
                (DialogFragment) activity.getFragmentManager().findFragmentByTag(TAG);
        if (progressDialog != null) {
            progressDialog.dismissAllowingStateLoss();
        }
    }

    public static ProgressDialogFragment create(int message) {
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MESSAGE, message);
        progressDialogFragment.setArguments(args);
        return progressDialogFragment;
    }

    public static ProgressDialogFragment create() {
        return create(R.string.progress_message);
    }

    public static boolean isShowing(Activity activity) {
        DialogFragment progressDialog =
                (DialogFragment) activity.getFragmentManager().findFragmentByTag(TAG);
        return progressDialog != null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(getArguments().getInt(ARG_MESSAGE)));
        return progressDialog;
    }
}
