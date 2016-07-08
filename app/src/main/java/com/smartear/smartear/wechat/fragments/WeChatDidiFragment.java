package com.smartear.smartear.wechat.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.smartear.smartear.R;
import com.smartear.smartear.wechat.RecognizedState;
import com.smartear.smartear.wechat.util.GMapV2Direction;
import com.smartear.smartear.wechat.util.GMapV2DirectionAsyncTask;

import org.w3c.dom.Document;

import java.util.ArrayList;


public class WeChatDidiFragment extends WeChatBaseFragment implements OnMapReadyCallback {

    private View retrieving;
    private View didiRequest;
    private GoogleMap map;
    private View waiting;
    private View mapContainer;

    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wechat_didi_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        retrieving = view.findViewById(R.id.retrieving);
        waiting = view.findViewById(R.id.waitingDriver);
        didiRequest = view.findViewById(R.id.didiRequest);
        mapContainer = view.findViewById(R.id.mapContainer);
        sayText(getContext(), RecognizedState.DIDI);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startDriverSearch();
            }
        }, 8000);
    }

    private void startDriverSearch() {
        didiRequest.setVisibility(View.VISIBLE);
        retrieving.setVisibility(View.GONE);
        mapContainer.setVisibility(View.VISIBLE);
        sayText(getContext(), RecognizedState.CONFIRM_LOCATION);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sayText(getContext(), RecognizedState.CONFIRMED);
                driverFound();
            }
        }, 10000);
    }

    private void driverFound() {
        waiting.setVisibility(View.VISIBLE);
        didiRequest.setVisibility(View.GONE);

        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                try {
                    Document doc = (Document) msg.obj;
                    GMapV2Direction md = new GMapV2Direction();
                    ArrayList<LatLng> directionPoint = md.getDirection(doc);
                    PolylineOptions rectLine = new PolylineOptions().width(15).color(getActivity().getResources().getColor(R.color.colorPrimary));

                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }
                    Polyline polylin = map.addPolyline(rectLine);
                    md.getDurationText(doc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        LatLng sourcePosition = new LatLng(37.7899718, -122.4047921);
        LatLng destPosition = new LatLng(37.796328, -122.4024962);
        new GMapV2DirectionAsyncTask(handler, sourcePosition, destPosition, GMapV2Direction.MODE_DRIVING).execute();
        map.addMarker(new MarkerOptions().position(sourcePosition)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_from)));

        map.addMarker(new MarkerOptions().position(destPosition)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_to)));

        map.animateCamera(CameraUpdateFactory.zoomTo(15));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.7897527, -122.4087975), 13));
    }
}
