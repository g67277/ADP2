package com.android.nazirshuqair.lastpick.detailsfiles;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by nazirshuqair on 12/16/14.
 */
public class MapDetailFragment extends MapFragment {

    public static final String TAG = "MapDetailFragment.TAG";

    GoogleMap mMap;
    double cLatitude;
    double cLongitude;

    public static MapDetailFragment newInstance(double _lat, double _lng, String _name){

        MapDetailFragment frag = new MapDetailFragment();

        Bundle args = new Bundle();
        args.putDouble("lat", _lat);
        args.putDouble("lng", _lng);
        args.putString("name", _name);
        frag.setArguments(args);

        return frag;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mMap = getMap();
        mMap.getUiSettings().setZoomControlsEnabled(false);


        final Bundle args = getArguments();
        if (args != null && args.containsKey("lat")){
            updateMarker(args.getDouble("lat"),
                    args.getDouble("lng"),
                    args.getString("name"));
        }
    }

    public void updateMarker(double _lat, double _lng, String _name){

        mMap.clear();
        if (mMap != null){
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(_lat, _lng))
                    .title(_name));
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(_lat, _lng), 15));

    }

}
