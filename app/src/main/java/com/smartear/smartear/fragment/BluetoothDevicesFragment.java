package com.smartear.smartear.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.smartear.smartear.R;
import com.smartear.smartear.databinding.FragmentDevicesListBinding;
import com.smartear.smartear.databinding.ItemBluetoothDeviceBinding;
import com.smartear.smartear.widget.RecyclerViewAdapterBase;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 10.11.2015
 */
public class BluetoothDevicesFragment extends BaseBluetoothFragment {
    private static final String TAG = "BluetoothDevicesFragment";
    FragmentDevicesListBinding binding;
    RecyclerViewAdapterBase<BluetoothDevice, ItemBluetoothDeviceBinding> adapter;


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
        adapter = new RecyclerViewAdapterBase<BluetoothDevice, ItemBluetoothDeviceBinding>() {
            @Override
            public ItemBluetoothDeviceBinding inflateItem(LayoutInflater inflater, ViewGroup parent, int viewType) {
                return ItemBluetoothDeviceBinding.inflate(inflater, parent, false);
            }
        };
        adapter.setOnItemClickListener(new RecyclerViewAdapterBase.OnItemClickListener<BluetoothDevice>() {
            @Override
            public void onItemClick(BluetoothDevice device) {
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    unPairDevice(device);
                } else {
                    pairDevice(device);
                }
            }
        });
        adapter.setItems(new ArrayList<BluetoothDevice>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(adapter);

        checkBluetoothEnabled();
        requestDevices();
    }

    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unPairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
    }

    private void requestDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.startDiscovery();
        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
            adapter.addItem(device);
        }
    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {
        if (!adapter.getItems().contains(device)) {
            adapter.addItem(device);
        }
    }

    @Override
    public void onDevicePaired(BluetoothDevice device) {
        Toast.makeText(getActivity(), R.string.paired, Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDeviceUnPaired(BluetoothDevice device) {
        Toast.makeText(getActivity(), R.string.unPaired, Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }
}
