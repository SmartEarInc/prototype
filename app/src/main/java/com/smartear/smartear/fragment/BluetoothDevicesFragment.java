package com.smartear.smartear.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.smartear.smartear.R;
import com.smartear.smartear.databinding.FragmentDevicesListBinding;
import com.smartear.smartear.databinding.ItemBluetoothDeviceBinding;
import com.smartear.smartear.utils.VersionUtils;
import com.smartear.smartear.viewmodels.BluetoothDeviceWrapper;
import com.smartear.smartear.widget.RecyclerViewAdapterBase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 10.11.2015
 */
public class BluetoothDevicesFragment extends BaseBluetoothFragment {
    private static final String TAG = "BluetoothDevicesFragment";
    FragmentDevicesListBinding binding;
    RecyclerViewAdapterBase<BluetoothDeviceWrapper, ItemBluetoothDeviceBinding> adapter;


    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public String getTitle() {
        return getString(R.string.bluetoothDevices);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDevicesListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setIsEmpty(true);
        adapter = new RecyclerViewAdapterBase<BluetoothDeviceWrapper, ItemBluetoothDeviceBinding>() {
            @Override
            public ItemBluetoothDeviceBinding inflateItem(LayoutInflater inflater, ViewGroup parent, int viewType) {
                return ItemBluetoothDeviceBinding.inflate(inflater, parent, false);
            }
        };
        adapter.setOnItemClickListener(new RecyclerViewAdapterBase.OnItemClickListener<BluetoothDeviceWrapper>() {
            @Override
            public void onItemClick(BluetoothDeviceWrapper wrapper) {
                BluetoothDevice device = wrapper.device.get();
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    unPairDevice(device);
                } else {
                    pairDevice(device);
                }
            }
        });
        adapter.setHasStableIds(true);
        adapter.setItems(new ArrayList<BluetoothDeviceWrapper>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isBluetoothEnabled()) {
            requestDevices();
        }
    }

    @Override
    protected void onBluetoothConnected() {
        requestDevices();
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            ProgressDialogFragment.showCancelable(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unPairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            ProgressDialogFragment.showCancelable(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void requestDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();
        if (VersionUtils.lollipopOrHigher()) {
            bluetoothAdapter.getBluetoothLeScanner().startScan(new ArrayList<ScanFilter>(), new ScanSettings.Builder()
                    .build(), new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    onDeviceFound(result.getDevice());
                }
            });
        }
        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
            addItem(device, false);
            updateConnectedDevices();
        }
        restartDiscoveryHandler.removeCallbacks(restartDiscoveryRunnable);
        restartDiscoveryHandler.postDelayed(restartDiscoveryRunnable, 3000);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        restartDiscoveryHandler.removeCallbacks(restartDiscoveryRunnable);
    }

    private Handler restartDiscoveryHandler = new Handler();
    private Runnable restartDiscoveryRunnable = new Runnable() {
        @Override
        public void run() {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.cancelDiscovery();
            bluetoothAdapter.startDiscovery();
            restartDiscoveryHandler.postDelayed(restartDiscoveryRunnable, 3000);
        }
    };

    @Override
    protected void onDeviceDisconnected(BluetoothDevice device) {
        for (BluetoothDeviceWrapper wrapper : adapter.getItems()) {
            if (wrapper.device.get().toString().equals(device.toString())) {
                wrapper.isConnected.set(false);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }


    private void addItem(BluetoothDevice device, boolean isConnected) {
        if (TextUtils.isEmpty(device.getName())) {
            return;
        }
        boolean alreadyHas = false;
        for (BluetoothDeviceWrapper wrapper : adapter.getItems()) {
            if (wrapper.device.get().toString().equals(device.toString())) {
                wrapper.device.set(device);
                wrapper.isConnected.set(isConnected);
                adapter.notifyDataSetChanged();
                alreadyHas = true;
                break;
            }
        }

        if (!alreadyHas) {
            BluetoothDeviceWrapper wrapper = new BluetoothDeviceWrapper();
            wrapper.device.set(device);
            wrapper.isConnected.set(isConnected);
            adapter.addItem(wrapper);
        }


    }

    @Override
    protected void onDeviceConnected(BluetoothDevice device) {
        for (BluetoothDeviceWrapper wrapper : adapter.getItems()) {
            if (wrapper.device.get().toString().equals(device.toString())) {
                wrapper.isConnected.set(true);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {
        addItem(device, false);
        binding.setIsEmpty(false);
        updateConnectedDevices();
    }

    @Override
    public void onDevicePaired(BluetoothDevice device) {
        Toast.makeText(getActivity(), R.string.paired, Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
        ProgressDialogFragment.hide(getActivity());
    }

    @Override
    public void onDeviceUnPaired(BluetoothDevice device) {
        Toast.makeText(getActivity(), R.string.unPaired, Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
        ProgressDialogFragment.hide(getActivity());
    }

    @Override
    protected void updateConnectedDevices(List<BluetoothDevice> connectedDevices) {
        for (BluetoothDevice connectedDevice : connectedDevices) {
            for (BluetoothDeviceWrapper wrapper : adapter.getItems()) {
                if (wrapper.device.get().toString().equals(connectedDevice.toString())) {
                    wrapper.isConnected.set(true);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
